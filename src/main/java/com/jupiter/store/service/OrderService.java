package com.jupiter.store.service;

import com.jupiter.store.constant.OrderStatus;
import com.jupiter.store.constant.PaymentStatus;
import com.jupiter.store.constant.ShippingStatus;
import com.jupiter.store.dto.order.ConfirmOrderDTO;
import com.jupiter.store.dto.order.CreateOrderDTO;
import com.jupiter.store.exception.CustomException;
import com.jupiter.store.model.*;
import com.jupiter.store.repository.*;
import com.jupiter.store.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    
    @Autowired
    private PaymentOrderRepository paymentOrderRepository;
    
    @Autowired
    private ShippingInfoRepository shippingInfoRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductVariantRepository productVariantRepository;
    
    @Autowired
    private UserRepository userRepository;

    public static Long currentUserId(){
        return SecurityUtils.getCurrentUserId();
    }

    /**
     * Get order by ID
     */
    public ResponseEntity<Order> getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<List<Order>> getAllUserOrders() {
        List<Order> orders = orderRepository.findByUserId(currentUserId());
        return ResponseEntity.ok(orders);
    }

    @Transactional
    public ResponseEntity<Order> createOrderFromCart(CreateOrderDTO createOrderDTO) {

        // Get user's cart
        Cart cart = cartRepository.findByUserId(currentUserId());
        if (cart == null) {
            throw new CustomException("Cart not found", HttpStatus.NOT_FOUND);
        }
        
        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty() && cartItems.stream().anyMatch(CartItem::isSelected)) {
            throw new CustomException("Cart is empty", HttpStatus.BAD_REQUEST);
        }
        
        // Calculate total amount
        int totalAmount = 0;
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                ProductVariant variant = productVariantRepository.findById(item.getProductVariantId())
                    .orElseThrow(() -> new CustomException("Product variant not found", HttpStatus.NOT_FOUND));
                
                // Check if enough quantity
                if (variant.getQuantity() < item.getQuantity()) {
                    throw new CustomException("Not enough quantity for product variant: " + variant.getId(), 
                        HttpStatus.BAD_REQUEST);
                }
                
                totalAmount += item.getTotalAmount();
            }
        }
        
        // Add total cost
        totalAmount += createOrderDTO.getShippingInfo().getShippingCost();
        
        // Create order
        Order order = new Order();
        order.setUserId(currentUserId());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setCreatedBy(currentUserId());
        order = orderRepository.save(order);

        // Create shipping info
        ShippingInfo shippingInfo = ShippingInfo.builder()
                .orderId(order.getId())
                .receiverName(createOrderDTO.getShippingInfo().getReceiverName())
                .receiverPhone(createOrderDTO.getShippingInfo().getReceiverPhone())
                .receiverAddress(createOrderDTO.getShippingInfo().getReceiverAddress())
                .status(ShippingStatus.PENDING)
                .shippingProvider(createOrderDTO.getShippingInfo().getShippingProvider())
                .shippingCost(createOrderDTO.getShippingInfo().getShippingCost())
                .createdBy(currentUserId())
                .build();
        shippingInfoRepository.save(shippingInfo);
        
        // Create order details
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                ProductVariant variant = productVariantRepository.findById(item.getProductVariantId())
                    .orElseThrow(() -> new CustomException("Product variant not found", HttpStatus.NOT_FOUND));
                
                // Create order detail
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(order.getId());
                orderDetail.setProductVariantId(variant.getId());
                orderDetail.setSoldQuantity(item.getQuantity());
                orderDetail.setSoldPrice(variant.getPrice());
                orderDetail.setCreatedBy(currentUserId());
                orderDetailRepository.save(orderDetail);
                
                // Update product variant quantity
                variant.setQuantity(variant.getQuantity() - item.getQuantity());
                productVariantRepository.save(variant);
            }
        }

        // Create payment order
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setOrderId(order.getId());
        paymentOrder.setPaymentMethod(createOrderDTO.getPaymentMethod());
        paymentOrder.setStatus(PaymentStatus.PENDING);
        paymentOrder.setDate(LocalDateTime.now());
        paymentOrder.setCreatedBy(currentUserId());
        paymentOrderRepository.save(paymentOrder);

        // Clear cart items that were ordered
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                cartItemRepository.delete(item);
            }
        }
        
        return ResponseEntity.ok(order);
    }



    public void confirmOrder(Long orderId, ConfirmOrderDTO confirmOrderDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));

        // Create order history
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderId(orderId);
        orderHistory.setDate(LocalDateTime.now());
        orderHistory.setStatus(OrderStatus.PENDING);
        orderHistory.setTotalAmount(order.getTotalAmount());
        orderHistory.setCreatedBy(currentUserId());
        orderHistoryRepository.save(orderHistory);
    }

    @Transactional
    public ResponseEntity<Order> updateOrderStatus(Long orderId, OrderStatus status) {
        Long userId = SecurityUtils.getCurrentUserId();
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        
        // Check if user is authorized to update this order
        if (!order.getUserId().equals(userId)) {
            throw new CustomException("Not authorized to update this order", HttpStatus.FORBIDDEN);
        }
        
        order.setOrderStatus(status);
        order.setLastModifiedBy(userId);
        order = orderRepository.save(order);
        
        // Create order history entry
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderId(orderId);
        orderHistory.setDate(LocalDateTime.now());
        orderHistory.setStatus(status);
        orderHistory.setTotalAmount(order.getTotalAmount());
        orderHistory.setCreatedBy(userId);
        orderHistoryRepository.save(orderHistory);
        
        // Update shipping info status if order is delivered
        if (status == OrderStatus.DELIVERED) {
            ShippingInfo shippingInfo = shippingInfoRepository.findByOrderId(orderId)
                .orElseThrow(() -> new CustomException("Shipping info not found", HttpStatus.NOT_FOUND));
            shippingInfo.setStatus(ShippingStatus.DELIVERED);
            shippingInfo.setDeliveryDate(LocalDateTime.now());
            shippingInfo.setLastModifiedBy(userId);
            shippingInfoRepository.save(shippingInfo);
        }
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * Cancel order
     */
    @Transactional
    public ResponseEntity<Order> cancelOrder(Long orderId) {
        Long userId = SecurityUtils.getCurrentUserId();
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        
        // Check if user is authorized to cancel this order
        if (!order.getUserId().equals(userId)) {
            throw new CustomException("Not authorized to cancel this order", HttpStatus.FORBIDDEN);
        }
        
        // Check if order can be cancelled
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new CustomException("Order cannot be cancelled in current status", HttpStatus.BAD_REQUEST);
        }
        
        // Update order status
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setLastModifiedBy(userId);
        order = orderRepository.save(order);
        
        // Create order history entry
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderId(orderId);
        orderHistory.setDate(LocalDateTime.now());
        orderHistory.setStatus(OrderStatus.CANCELLED);
        orderHistory.setTotalAmount(order.getTotalAmount());
        orderHistory.setCreatedBy(userId);
        orderHistoryRepository.save(orderHistory);
        
        // Update payment status
        PaymentOrder paymentOrder = paymentOrderRepository.findByOrderId(orderId)
            .orElseThrow(() -> new CustomException("Payment not found", HttpStatus.NOT_FOUND));
        paymentOrder.setStatus(PaymentStatus.CANCELED);
        paymentOrder.setLastModifiedBy(userId);
        paymentOrderRepository.save(paymentOrder);
        
        // Update shipping status
        ShippingInfo shippingInfo = shippingInfoRepository.findByOrderId(orderId)
            .orElseThrow(() -> new CustomException("Shipping info not found", HttpStatus.NOT_FOUND));
        shippingInfo.setStatus(ShippingStatus.CANCELLED);
        shippingInfo.setLastModifiedBy(userId);
        shippingInfoRepository.save(shippingInfo);
        
        // Restore product quantities
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        for (OrderDetail detail : orderDetails) {
            ProductVariant variant = productVariantRepository.findById(detail.getProductVariantId())
                .orElseThrow(() -> new CustomException("Product variant not found", HttpStatus.NOT_FOUND));
            variant.setQuantity(variant.getQuantity() + detail.getSoldQuantity());
            productVariantRepository.save(variant);
        }
        
        return ResponseEntity.ok(order);
    }
} 