package com.jupiter.store.module.order.service;

import com.jupiter.store.common.exception.CustomException;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.customer.dto.CustomerDTO;
import com.jupiter.store.module.customer.model.Customer;
import com.jupiter.store.module.customer.service.CustomerService;
import com.jupiter.store.module.notifications.constant.NotificationEntityType;
import com.jupiter.store.module.notifications.dto.NotificationDTO;
import com.jupiter.store.module.notifications.service.NotificationService;
import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.dto.*;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.model.OrderDetail;
import com.jupiter.store.module.order.repository.OrderDetailRepository;
import com.jupiter.store.module.order.repository.OrderRepository;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import com.jupiter.store.module.payment.dto.PaymentReadDTO;
import com.jupiter.store.module.payment.model.Payment;
import com.jupiter.store.module.payment.service.PaymentService;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import com.jupiter.store.module.user.dto.UserReadDTO;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderHistoryService orderHistoryService;

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    /**
     * Get order by ID
     */
    public OrderItemsDTO getOrderDetailById(Integer orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
        orderItemsDTO.setOrderId(orderId);
        orderItemsDTO.setOrderItems(orderDetails);
        return orderItemsDTO;
    }

    public Page<Order> search(
            Integer pageSize,
            Integer pageNumber,
            String search,
            List<OrderStatus> orderStatuses,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Integer currentUserId = currentUserId();
        if (currentUserId == null) {
            throw new CustomException("Không tìm thấy người dùng hiện tại", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new CustomException("Không tìm thấy người dùng hiện tại", HttpStatus.NOT_FOUND));

        if (user.getRole() == null || !user.canViewOrder()) {
            throw new CustomException("Bạn không có quyền xem đơn hàng", HttpStatus.FORBIDDEN);
        }

        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        if (search != null) {
            search = search.trim();
            if (search.isBlank()) {
                search = null;
            } else {
                search = search.toLowerCase();
            }
        }
        if (startDate == null) {
            startDate = LocalDate.of(1900, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.of(9999, 1, 1);
        }

        // +1 ngày vì dùng `<` trong sql
        endDate = endDate.plusDays(1);

        List<String> statuses = new ArrayList<>();
        if (orderStatuses != null && !orderStatuses.isEmpty()) {
            statuses = orderStatuses.stream().map(OrderStatus::name).toList();
            if (statuses.isEmpty()) {
                statuses = OrderStatus.getAllStatuses();
            }
        } else {
            statuses = OrderStatus.getAllStatuses();
        }

        return orderRepository.search(search, statuses, startDate, endDate, pageable);
    }

    public Order createOrder(
            Integer customerId,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            String note,
            Long paid,
            PaymentMethod paymentMethod,
            OrderStatus orderStatus,
            List<OrderDetailCreateDTO> orderItems
    ) {
        User user = userRepository.findById(currentUserId())
                .orElseThrow(() -> new CustomException("Không tìm thấy người dùng hiện tại", HttpStatus.NOT_FOUND));

        Order order = new Order();
        order.setUserId(currentUserId());
        Long totalAmount = orderItems.stream().filter(Objects::nonNull)
                .mapToLong(item -> item.getSoldPrice() * item.getSoldQuantity())
                .sum();
        if (totalAmount < 0) {
            throw new CustomException("Tổng số tiền phải lớn hơn 0", HttpStatus.BAD_REQUEST);
        }
        order.setTotalAmount(totalAmount);
        Customer customer = null;
        if (customerId != null) {
            customer = customerService.findById(customerId);
        }
        if (customer == null && receiverPhone != null) {
            customer = customerService.findByPhone(receiverPhone);
        }
        if (customer != null) {
            customerId = customer.getId();
            order.setCustomerId(customerId);
            customerService.updateAfterOrder(customerId, totalAmount, 1);
        }
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setNote(note);
        order.setOrderStatus(orderStatus != null ? orderStatus : OrderStatus.CHO_XAC_NHAN);
        order.setCreatedBy(currentUserId());
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);

        // Save order details if provided
        if (!orderItems.isEmpty()) {
            List<OrderDetail> orderDetailList = new ArrayList<>();
            for (OrderDetailCreateDTO orderDetailDTO : orderItems) {
                OrderDetail orderDetail = new OrderDetail();
                ProductVariant productVariant = productVariantRepository.findById(orderDetailDTO.getProductVariantId())
                        .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!"));
                orderDetail.setOrder(order);
                orderDetail.setProductVariant(productVariant);
                orderDetail.setPrice(productVariant.getCostPrice());
                orderDetail.setSoldQuantity(orderDetailDTO.getSoldQuantity());
                orderDetail.setSoldPrice(productVariant.getPrice());
                if (orderDetail.getSoldQuantity() < 0) {
                    throw new CustomException("Số lượng sản phẩm phải lớn hơn 0", HttpStatus.BAD_REQUEST);
                }
                if (orderDetail.getSoldPrice() < 0) {
                    throw new CustomException("Giá sản phẩm phải lớn hơn 0", HttpStatus.BAD_REQUEST);
                }
                orderDetailList.add(orderDetail);
            }
            orderDetailRepository.saveAll(orderDetailList);
        }

        paymentService.createPayment(order.getId(), paid, paymentMethod);
        CompletableFuture.runAsync(() -> {
            NotificationDTO notificationDTO = new NotificationDTO("Đơn hàng mới", user.getFullName() + " đã tạo một đơn hàng mới",
                    NotificationEntityType.ORDER, order.getId());
            notificationService.sendNotification(notificationDTO);
        });
        return order;
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
            orderItem.setProductVariant(productVariant);
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
        ProductVariant productVariant = productVariantRepository.findById(orderDetail.getProductVariant().getId())
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!"));
        if (productVariant != null) {
            orderDetail.setSoldQuantity(quantity);
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!");
        }
        return ResponseEntity.ok(orderDetailRepository.save(orderDetail));
    }

    public void updateOrderStatus(Integer orderId, UpdateOrderStatusDTO updateOrderStatusDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));
        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus oldStatus = updateOrderStatusDTO.getOldOrderStatus();
        if (currentStatus == OrderStatus.DA_HUY) {
            throw new CustomException("Không thể cập nhật trạng thái đơn hàng đã hủy", HttpStatus.BAD_REQUEST);
        }
        if (oldStatus != null && !currentStatus.equals(oldStatus)) {
            throw new CustomException("Trạng thái đơn hàng đã bị thay đổi", HttpStatus.BAD_REQUEST);
        }
        OrderStatus newOrderStatus = updateOrderStatusDTO.getNewOrderStatus();
        if (newOrderStatus == null) {
            throw new CustomException("Trạng thái đơn hàng mới không được để trống", HttpStatus.BAD_REQUEST);
        }
        Set<OrderStatus> allowedTransitions = OrderStatus.validTransitions.get(currentStatus);
        if (allowedTransitions == null || !allowedTransitions.contains(newOrderStatus)) {
            throw new CustomException("Không thể chuyển từ " + currentStatus.getValue() + " sang " + newOrderStatus.getValue(), HttpStatus.BAD_REQUEST);
        }
        order.setOrderStatus(newOrderStatus);
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);

        orderHistoryService.createOrderHistory(order.getId(), oldStatus, newOrderStatus);
    }

    public void updateOrder(UpdateOrderDTO updateOrderDTO) {
        Order order = orderRepository.findById(updateOrderDTO.getOrderId())
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));
        order.setOrderStatus(OrderStatus.DA_XAC_NHAN);
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);
    }

    public void deleteOrderItem(Integer orderDetailId) {
        orderDetailRepository.deleteById(orderDetailId);
    }

    public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        order.setOrderStatus(OrderStatus.DA_HUY);
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);
    }

    public OrderReadDTO getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));

        OrderReadDTO orderReadDTO = new OrderReadDTO(order);

        if (order.getUserId() != null) {
            User user = userRepository.findById(order.getUserId()).orElse(null);
            orderReadDTO.setUser(new UserReadDTO(user));
        }
        if (order.getCustomerId() != null) {
            Customer customer = customerService.findById(order.getCustomerId());
            orderReadDTO.setCustomer(customer != null ? new CustomerDTO(customer) : null);
        }
        setOrderDetails(orderReadDTO);
        setPayments(orderReadDTO);
        setOrderHistory(orderReadDTO);
        return orderReadDTO;
    }

    public void setOrderDetails(OrderReadDTO orderReadDTO) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderReadDTO.getId());
        List<OrderDetailReadDTO> orderDetailReadDTOs = orderDetailService.setDetails(orderDetails);
        orderReadDTO.setOrderDetails(orderDetailReadDTOs);
    }

    public void setPayments(OrderReadDTO orderReadDTO) {
        List<PaymentReadDTO> payments = paymentService.getPaymentsByOrderId(orderReadDTO.getId());
        orderReadDTO.setPayments(payments);
    }

    public void setOrderHistory(OrderReadDTO orderReadDTO) {
        List<OrderHistoryDTO> orderHistories = orderHistoryService.getOrderHistoriesByOrderId(orderReadDTO.getId());
        orderReadDTO.setOrderHistories(orderHistories);
    }
}