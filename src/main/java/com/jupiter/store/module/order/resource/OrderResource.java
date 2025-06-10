package com.jupiter.store.module.order.resource;

import com.jupiter.store.module.order.dto.CreateOrderDTO;
import com.jupiter.store.module.order.dto.OrderItemsDTO;
import com.jupiter.store.module.order.dto.OrderSearchDTO;
import com.jupiter.store.module.order.dto.UpdateOrderStatusDTO;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.service.OrderService;
import com.jupiter.store.module.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderResource {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/search")
    public ResponseEntity<Page<Order>> search(@RequestBody OrderSearchDTO orderSearchDTO) {
        return ResponseEntity.ok(
                orderService.search(
                        orderSearchDTO.getPageSize(),
                        orderSearchDTO.getPageNumber(),
                        orderSearchDTO.getSearch(),
                        orderSearchDTO.getOrderStatuses(),
                        orderSearchDTO.getStartDate(),
                        orderSearchDTO.getEndDate()
                ));
    }

    @GetMapping("/detail-search/{orderId}")
    public OrderItemsDTO getOrderDetail(@PathVariable Integer orderId) {
        return orderService.getOrderDetailById(orderId);
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
                createOrderDTO.getOrderStatus(),
                createOrderDTO.getOrderItems()
        );
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
    public void updateOrder(@RequestBody UpdateOrderStatusDTO updateOrderStatusDTO) {
        orderService.updateOrderStatus(updateOrderStatusDTO);
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
