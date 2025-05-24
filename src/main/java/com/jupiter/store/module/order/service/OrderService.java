package com.jupiter.store.module.order.service;

import com.jupiter.store.common.exception.CustomException;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.dto.OrderItemsDTO;
import com.jupiter.store.module.order.dto.UpdateOrderDTO;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.model.OrderDetail;
import com.jupiter.store.module.order.repository.OrderDetailRepository;
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
    private ProductVariantRepository productVariantRepository;

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
        order.setTotalAmount(0L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedBy(currentUserId());
        return orderRepository.save(order);
    }

    public OrderItemsDTO addProductToOrder(Integer orderId, Integer productVariantId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!"));
        OrderDetail existedOrderDetail = orderDetailRepository.findByOrderIdAndProductVariantId(orderId, productVariantId);
        if (existedOrderDetail != null) {
            int quantity = existedOrderDetail.getSoldQuantity() + 1;
            existedOrderDetail.setPrice(productVariant.getCostPrice());
            existedOrderDetail.setSoldQuantity(quantity);
            existedOrderDetail.setSoldPrice(productVariant.getPrice());
            orderDetailRepository.save(existedOrderDetail);
        } else {
            OrderDetail orderItem = new OrderDetail();
            orderItem.setOrder(order);
            orderItem.setProductVariantId(productVariantId);
            orderItem.setPrice(productVariant.getCostPrice());
            orderItem.setSoldQuantity(1);
            orderItem.setSoldPrice(productVariant.getPrice());
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
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!"));
        if (productVariant != null) {
            orderDetail.setSoldQuantity(quantity);
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!");
        }
        return ResponseEntity.ok(orderDetailRepository.save(orderDetail));
    }

    public void updateOrderStatus(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);
    }

    public void updateOrder(UpdateOrderDTO updateOrderDTO) {
        Order order = orderRepository.findById(updateOrderDTO.getOrderId())
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));
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