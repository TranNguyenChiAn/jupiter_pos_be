package com.jupiter.store.module.order.resource;

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
    public OrderItemsDTO getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderDetailById(orderId);
    }

    @PostMapping("/create-order/")
    public Order createOrder(@RequestParam(required = false) Long customerId) {
        if (customerId == null) {
            return orderService.createOrder(null);
        }
       return orderService.createOrder(customerId);
    }

    @PostMapping("/add-product/{orderId}/{productVariantId}")
    public OrderItemsDTO addProductToOrder(@PathVariable Long orderId, @PathVariable Long productVariantId) {
        return orderService.addProductToOrder(orderId, productVariantId);
    }

    @PutMapping("/update-quantity-item")
    public void updateQuantityItem(@RequestParam Long orderDetailId, @RequestParam int quantity) {
        orderService.updateQuantityItem(orderDetailId, quantity);
    }

    @PutMapping("/update-status")
    public void updateOrder(@RequestParam Long orderId) {
        orderService.updateOrderStatus(orderId);
    }
    @PostMapping("/confirm-order/{orderId}")
        public void confirmOrder(@PathVariable Long orderId) {
    }

    @DeleteMapping("/delete-order-item/{orderDetailId}")
    public void deleteOrderItem(@PathVariable Long orderDetailId) {
        orderService.deleteOrderItem(orderDetailId);
    }

    @PutMapping("/cancel")
    public void cancelOrder(@RequestParam Long orderId) {
        orderService.cancelOrder(orderId);
    }
}
