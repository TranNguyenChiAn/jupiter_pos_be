package com.jupiter.store.module.order.resource;

import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.dto.ConfirmOrderDTO;
import com.jupiter.store.module.order.dto.CreateOrderDTO;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.service.OrderService;
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

    }

    @PostMapping("/confirm-order/{orderId}")
    public void confirmOrder(@PathVariable Long orderId, @RequestBody ConfirmOrderDTO confirmOrderDTO) {

    }

    @PutMapping("/update-status")
    public void updateOrder(@RequestParam Long orderId, @RequestBody OrderStatus orderStatus) {

    }

    @PutMapping("/cancel")
    public void cancelOrder(@RequestParam Long orderId) {

    }
}
