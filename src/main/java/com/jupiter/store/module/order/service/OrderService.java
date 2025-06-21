package com.jupiter.store.module.order.service;

import com.jupiter.store.common.exception.CustomException;
import com.jupiter.store.common.utils.HelperUtils;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.customer.dto.CustomerDTO;
import com.jupiter.store.module.customer.model.Customer;
import com.jupiter.store.module.customer.service.CustomerService;
import com.jupiter.store.module.notifications.constant.NotificationEntityType;
import com.jupiter.store.module.notifications.dto.NotificationDTO;
import com.jupiter.store.module.notifications.service.NotificationService;
import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.constant.OrderType;
import com.jupiter.store.module.order.dto.*;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.model.OrderDetail;
import com.jupiter.store.module.order.repository.OrderDetailRepository;
import com.jupiter.store.module.order.repository.OrderRepository;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import com.jupiter.store.module.payment.dto.PaymentReadDTO;
import com.jupiter.store.module.payment.service.PaymentService;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import com.jupiter.store.module.user.dto.UserReadDTO;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import com.jupiter.store.module.user.service.UserService;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private UserService userService;
    @Autowired
    private HelperUtils helperUtils;

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public Page<Order> search(
            Integer pageSize,
            Integer pageNumber,
            String search,
            List<OrderStatus> orderStatuses,
            LocalDate startDate,
            LocalDate endDate,
            String sortBy,
            String sortDirection
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

        if (sortBy != null && !sortBy.isBlank()) {
            sortBy = helperUtils.convertCamelCaseToSnakeCase(sortBy);
        } else {
            sortBy = "order_date";
        }
        if (sortDirection == null || sortDirection.isBlank()) {
            sortDirection = "DESC";
        } else {
            sortDirection = sortDirection.toUpperCase();
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return orderRepository.search(search, statuses, startDate, endDate, pageable);
    }

    public Order findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));
    }

    public Order createOrder(
            Integer customerId,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            String note,
            Long paid,
            PaymentMethod paymentMethod,
            List<OrderDetailCreateDTO> orderItems,
            OrderType orderType
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
        order.setOrderType(orderType);

        if (paid != null && (totalAmount.equals(paid) || paid > totalAmount )) {
            if (orderType.equals(OrderType.MUA_TRUC_TIEP)) {
                order.setOrderStatus(OrderStatus.HOAN_THANH);
            } else {
                order.setOrderStatus(OrderStatus.DA_XAC_NHAN);
            }
        } else {
            order.setOrderStatus(OrderStatus.CHO_XAC_NHAN);
        }
        order.setCreatedBy(currentUserId());
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);

        if (paid != null && paid > 0) {
            paymentService.createPayment(order.getId(), paid, paymentMethod, note);
        }

        //them order history create order

        // Save order details
        if (!orderItems.isEmpty()) {
            List<OrderDetail> orderDetailList = new ArrayList<>();
            for (OrderDetailCreateDTO orderDetailDTO : orderItems) {
                ProductVariant productVariant = productVariantRepository.findById(orderDetailDTO.getProductVariantId())
                        .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy biến thể sản phẩm có ID: " + orderDetailDTO.getProductVariantId() + "!"));

                OrderDetail orderDetail = new OrderDetail();
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

                // Cập nhật số lượng tồn kho
                if (productVariant.getQuantity() - orderDetailDTO.getSoldQuantity() > 0) {
                    Integer remainingQuantity = productVariant.getQuantity() - orderDetailDTO.getSoldQuantity();
                    productVariant.setQuantity(remainingQuantity);
                    productVariantRepository.save(productVariant);
                } else {
                    throw new CustomException("Số lượng tồn kho của biến thể " + productVariant.getId() + " không đủ", HttpStatus.BAD_REQUEST);
                }
            }
            orderDetailRepository.saveAll(orderDetailList);
        }

        CompletableFuture.runAsync(() -> {
            NotificationDTO notificationDTO = new NotificationDTO("Đơn hàng mới", user.getFullName() + " đã tạo một đơn hàng mới",
                    NotificationEntityType.ORDER, order.getId());
            notificationService.sendNotification(notificationDTO);
        });
        return order;
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
        User user = userService.findById(currentUserId());
        if (user == null) {
            throw new CustomException("Không tìm thấy người dùng hiện tại", HttpStatus.NOT_FOUND);
        }
        if (!user.canUpdateOrder()) {
            throw new CustomException("Bạn không có quyền cập nhật đơn hàng", HttpStatus.FORBIDDEN);
        }
        order.setOrderStatus(newOrderStatus);
        order.setLastModifiedBy(user.getId());
        orderRepository.save(order);

        orderHistoryService.createOrderHistory(order.getId(), oldStatus, newOrderStatus);
    }

    public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(currentUserId()).orElse(null);
        if (user == null) {
            throw new CustomException("Không tìm thấy người dùng hiện tại", HttpStatus.NOT_FOUND);
        }
        List<PaymentReadDTO> payments = paymentService.getPaymentsByOrderId(orderId);
        if (!user.canUpdateOrder()) {
            throw new CustomException("Bạn không có quyền hủy đơn hàng", HttpStatus.FORBIDDEN);
        }
        // Đã có thanh toán
        if (payments != null && !payments.isEmpty()) {
            if (!user.isAdmin()) {
                throw new CustomException("Bạn không có quyền hủy đơn hàng đã có thanh toán", HttpStatus.FORBIDDEN);
            }
        }

        if (List.of(OrderStatus.HOAN_THANH, OrderStatus.DA_GIAO).contains(order.getOrderStatus())) {
            throw new CustomException("Không thể hủy đơn hàng đã giao hoặc đã hoàn thành", HttpStatus.BAD_REQUEST);
        }
        if (order.getOrderStatus() == OrderStatus.DA_HUY) {
            throw new CustomException("Đơn hàng đã được hủy trước đó", HttpStatus.BAD_REQUEST);
        }
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

    public void updateStatus(Integer orderId, UpdateOrderDTO updateOrderDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND));

        if (updateOrderDTO.getReceiverName() != null) {
            order.setReceiverName(updateOrderDTO.getReceiverName());
        }
        if (updateOrderDTO.getReceiverPhone() != null) {
            order.setReceiverPhone(updateOrderDTO.getReceiverPhone());
        }
        if (updateOrderDTO.getReceiverAddress() != null) {
            order.setReceiverAddress(updateOrderDTO.getReceiverAddress());
        }
        if (updateOrderDTO.getNote() != null) {
            order.setNote(updateOrderDTO.getNote());
        }
        if (updateOrderDTO.getOrderType() != null) {
            order.setOrderType(updateOrderDTO.getOrderType());
        }
        order.setLastModifiedBy(currentUserId());
        orderRepository.save(order);
    }
}