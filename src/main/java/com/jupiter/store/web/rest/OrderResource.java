package com.jupiter.store.web.rest;

import com.jupiter.store.constant.OrderStatus;
import com.jupiter.store.dto.order.ConfirmOrderDTO;
import com.jupiter.store.dto.order.CreateOrderDTO;
import com.jupiter.store.model.Order;
import com.jupiter.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderResource {
    @Autowired
    private OrderService orderService;

    @GetMapping("/search")
    public void getUserOrder() {
        orderService.getAllUserOrders();
    }

    @GetMapping("/detail-search/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/create-order")
    public void createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        orderService.createOrderFromCart(createOrderDTO);
    }

    @PostMapping("/confirm-order/{orderId}")
    public void confirmOrder(@PathVariable Long orderId, @RequestBody ConfirmOrderDTO confirmOrderDTO) {
        orderService.confirmOrder(orderId, confirmOrderDTO);
    }

    @PutMapping("/update-status")
    public void updateOrder(@RequestParam Long orderId, @RequestBody OrderStatus orderStatus) {
        orderService.updateOrderStatus(orderId, orderStatus);
    }

    @PutMapping("/cancel")
    public void cancelOrder(@RequestParam Long orderId) {
        orderService.cancelOrder(orderId);
    }
}
