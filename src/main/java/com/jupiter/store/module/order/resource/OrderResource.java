package com.jupiter.store.module.order.resource;

import com.jupiter.store.module.order.dto.CreateOrderDTO;
import com.jupiter.store.module.order.dto.OrderItemsDTO;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderResource {
    @Autowired
    private OrderService orderService;

    @GetMapping("/search")
    public List<Order> getUserOrder() {
        return orderService.getAllUserOrders();
    }

    @GetMapping("/detail-search/{orderId}")
    public OrderItemsDTO getOrderDetail(@PathVariable Integer orderId) {
        return orderService.getOrderDetailById(orderId);
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        Order order = orderService.createOrder(
                createOrderDTO.getCustomerId(),
                createOrderDTO.getReceiverName(),
                createOrderDTO.getReceiverPhone(),
                createOrderDTO.getReceiverAddress(),
                createOrderDTO.getNote(),
                createOrderDTO.getPaymentMethod(),
                createOrderDTO.getOrderStatus(),
                createOrderDTO.getOrderItems()
        );
        return ResponseEntity.ok(order);
    }

    @PostMapping("/add-product/{orderId}/{productVariantId}")
    public OrderItemsDTO addProductToOrder(@PathVariable Integer orderId, @PathVariable Integer productVariantId) {
        return orderService.addProductToOrder(orderId, productVariantId);
    }

    @PutMapping("/update-quantity-item")
    public void updateQuantityItem(@RequestParam Integer orderDetailId, @RequestParam int quantity) {
        orderService.updateQuantityItem(orderDetailId, quantity);
    }

    @PutMapping("/update-status")
    public void updateOrder(@RequestParam Integer orderId) {
        orderService.updateOrderStatus(orderId);
    }

    @PostMapping("/confirm-order/{orderId}")
    public void confirmOrder(@PathVariable Integer orderId) {
    }

    @DeleteMapping("/delete-order-item/{orderDetailId}")
    public void deleteOrderItem(@PathVariable Integer orderDetailId) {
        orderService.deleteOrderItem(orderDetailId);
    }

    @PutMapping("/cancel")
    public void cancelOrder(@RequestParam Integer orderId) {
        orderService.cancelOrder(orderId);
    }
}
