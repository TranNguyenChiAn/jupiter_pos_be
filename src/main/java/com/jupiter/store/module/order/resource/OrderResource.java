package com.jupiter.store.module.order.resource;

import com.jupiter.store.module.order.dto.*;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.service.OrderDetailService;
import com.jupiter.store.module.order.service.OrderService;
import com.jupiter.store.module.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
public class OrderResource {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/search")
    public ResponseEntity<Page<Order>> search(@RequestBody OrderSearchDTO orderSearchDTO) {
        return ResponseEntity.ok(
                orderService.search(
                        orderSearchDTO.getPageSize(),
                        orderSearchDTO.getPageNumber(),
                        orderSearchDTO.getSearch(),
                        orderSearchDTO.getOrderStatuses(),
                        orderSearchDTO.getStartDate(),
                        orderSearchDTO.getEndDate(),
                        orderSearchDTO.getSortBy(),
                        orderSearchDTO.getSortDirection()
                ));
    }

    @GetMapping("/{orderId}")
    public OrderReadDTO getOrderById(@PathVariable Integer orderId) {
        OrderReadDTO order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Đơn hàng không tồn tại");
        }
        return order;
    }

    @GetMapping("/detail-search/{orderId}")
    public OrderItemsDTO getOrderDetail(@PathVariable Integer orderId) {
        return orderDetailService.getOrderDetailById(orderId);
    }

    @PostMapping("/create")
    public Order createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        return orderService.createOrder(
                createOrderDTO.getCustomerId(),
                createOrderDTO.getReceiverName(),
                createOrderDTO.getReceiverPhone(),
                createOrderDTO.getReceiverAddress(),
                createOrderDTO.getNote(),
                createOrderDTO.getPaid(),
                createOrderDTO.getPaymentMethod(),
                createOrderDTO.getOrderItems(),
                createOrderDTO.getOrderType()
        );
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Void> updateOrder(@PathVariable Integer orderId, @RequestBody UpdateOrderDTO updateOrderDTO) {
        orderService.updateStatus(orderId, updateOrderDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Integer orderId, @RequestBody UpdateOrderStatusDTO updateOrderStatusDTO) {
        orderService.updateOrderStatus(orderId, updateOrderStatusDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-order/{orderId}")
    public void confirmOrder(@PathVariable Integer orderId) {
    }

    @PutMapping("/cancel")
    public void cancelOrder(@RequestParam Integer orderId) {
        orderService.cancelOrder(orderId);
    }
}
