package com.jupiter.store.module.order.service;

import com.jupiter.store.common.exception.CustomException;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.customer.repository.CustomerRepository;
import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.dto.OrderItemsDTO;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.model.OrderDetail;
import com.jupiter.store.module.order.repository.OrderDetailRepository;
import com.jupiter.store.module.order.repository.OrderHistoryRepository;
import com.jupiter.store.module.order.repository.OrderRepository;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    /**
     * Get order by ID
     */
    public OrderItemsDTO getOrderDetailById(Integer orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
        orderItemsDTO.setOrderId(orderId);
        orderItemsDTO.setOrderItems(orderDetails);
        return orderItemsDTO;
    }

    public List<Order> getAllUserOrders() {
       return orderRepository.findByUserId(currentUserId());
    }

    public Order createOrder(Integer customerId) {
        Order order = new Order();
        order.setUserId(currentUserId());
        order.setCustomerId(customerId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedBy(currentUserId());
        return orderRepository.save(order);
    }

    public OrderItemsDTO addProductToOrder(Integer orderId, Integer productVariantId) {
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product variant not found"));
        OrderDetail orderDetailDTO = orderDetailRepository.findByOrderIdAndProductVariantId(orderId, productVariantId);
        if (orderDetailDTO != null) {
            int quantity = orderDetailDTO.getSoldQuantity() + 1;
            orderDetailDTO.setSoldQuantity(quantity);
            orderDetailDTO.setSoldPrice(productVariant.getPrice());
            orderDetailDTO.setTotalAmount(quantity + productVariant.getPrice());
            orderDetailDTO.setLastModifiedBy(currentUserId());
            orderDetailRepository.save(orderDetailDTO);
        } else {
            OrderDetail orderItem = new OrderDetail();
            orderItem.setOrderId(orderId);
            orderItem.setProductVariantId(productVariantId);
            orderItem.setSoldQuantity(1);
            orderItem.setSoldPrice(productVariant.getPrice());
            orderItem.setTotalAmount(productVariant.getPrice());
            orderItem.setCreatedBy(currentUserId());
            orderDetailRepository.save(orderItem);
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
        orderItemsDTO.setOrderId(orderId);
        orderItemsDTO.setOrderItems(orderDetails);
        return orderItemsDTO;
    }

    public ResponseEntity<OrderDetail> updateQuantityItem(Integer orderDetailId, int quantity) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(() -> new OpenApiResourceNotFoundException("Cart item not found"));
        ProductVariant productVariant = productVariantRepository.findById(orderDetail.getProductVariantId())
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product variant not found"));

        orderDetail.setSoldQuantity(quantity);
        orderDetail.setTotalAmount(productVariant.getPrice() * quantity);
        orderDetail.setLastModifiedBy(currentUserId());
        return ResponseEntity.ok(orderDetailRepository.save(orderDetail));
    }

    public void updateOrderStatus(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);
    }

    public void deleteOrderItem(Integer orderDetailId) {
        orderDetailRepository.deleteById(orderDetailId);
    }

    public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);
    }
}