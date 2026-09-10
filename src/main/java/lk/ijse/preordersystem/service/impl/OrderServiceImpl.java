package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.OrderDTO;
import lk.ijse.preordersystem.dto.OrderItemDTO;
import lk.ijse.preordersystem.entity.*;
import lk.ijse.preordersystem.enumeration.OrderStatus;
import lk.ijse.preordersystem.repository.DiscountRepository;
import lk.ijse.preordersystem.repository.OrderRepository;
import lk.ijse.preordersystem.repository.OrderStatusHistoryRepository;
import lk.ijse.preordersystem.repository.PaymentRepository;
import lk.ijse.preordersystem.service.NotificationService;
import lk.ijse.preordersystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DiscountRepository discountRepository;
    private final PaymentRepository paymentRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final NotificationService notificationService;

    @Override
    public OrderDTO placeOrder(OrderDTO orderDTO) {

        log.info("Execute method placeOrder");

        try {

            Order order = new Order();
            order.setCustomerId(orderDTO.getCustomerId());
            order.setCustomerName(orderDTO.getCustomerName());
            order.setPickupTime(orderDTO.getPickupTime());
            order.setNotes(orderDTO.getNotes());
            order.setStatus(OrderStatus.PENDING.name());
            order.setPlacedAt(LocalDateTime.now());

            List<OrderItem> orderItems = new ArrayList<>();
            double subtotal = 0;

            if (orderDTO.getItems() != null) {
                for (OrderItemDTO itemDTO : orderDTO.getItems()) {

                    OrderItem orderItem = new OrderItem();
                    orderItem.setMenuItemId(itemDTO.getMenuItemId());
                    orderItem.setName(itemDTO.getName());
                    orderItem.setPrice(itemDTO.getPrice());
                    orderItem.setQty(itemDTO.getQty());
                    orderItem.setOrder(order);

                    orderItems.add(orderItem);
                    subtotal += itemDTO.getPrice() * itemDTO.getQty();
                }
            }

            order.setOrderItems(orderItems);

            double total = subtotal;

            if (orderDTO.getDiscountCode() != null && !orderDTO.getDiscountCode().trim().isEmpty()) {

                Optional<Discount> optionalDiscount = discountRepository.findByCodeIgnoreCase(orderDTO.getDiscountCode().trim());

                if (optionalDiscount.isPresent() && optionalDiscount.get().isActive()) {
                    Discount discount = optionalDiscount.get();
                    order.setDiscount(discount);
                    total = subtotal - (subtotal * discount.getPercentage() / 100.0);
                }
            }

            order.setTotal(total);

            Order savedOrder = orderRepository.save(order);

            recordStatusHistory(savedOrder, OrderStatus.PENDING.name(), "SYSTEM");

            Payment payment = new Payment();
            payment.setOrder(savedOrder);
            payment.setAmount(total);
            payment.setMethod("COUNTER");
            payment.setStatus("PENDING");
            paymentRepository.save(payment);

            if (savedOrder.getCustomerId() > 0) {
                notificationService.createNotification(savedOrder.getCustomerId(),
                        "Your order " + orderCode(savedOrder.getOrderId()) + " has been placed and is pending confirmation.");
            }

            log.info("Order placed successfully");
            return mapToDto(savedOrder);

        }catch (Exception e){
            log.info("Error in method placeOrder" + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OrderDTO> getAllOrders() {

        log.info("Execute method getAllOrders");

        try {

            List<OrderDTO> responseList = new ArrayList<>();
            List<Order> orderList = orderRepository.findAll();

            for (Order order : orderList) {
                responseList.add(mapToDto(order));
            }

            log.info("Orders retrieved successfully");
            return responseList;

        }catch (Exception e){
            log.info("Error in method getAllOrders" + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OrderDTO> getOrdersByCustomer(long customerId) {

        log.info("Execute method getOrdersByCustomer");

        try {

            List<OrderDTO> responseList = new ArrayList<>();
            List<Order> orderList = orderRepository.findByCustomerId(customerId);

            for (Order order : orderList) {
                responseList.add(mapToDto(order));
            }

            log.info("Customer orders retrieved successfully");
            return responseList;

        }catch (Exception e){
            log.info("Error in method getOrdersByCustomer" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateOrderStatus(long orderId, String status) {

        log.info("Execute method updateOrderStatus");

        try {

            OrderStatus.valueOf(status);

            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                throw new RuntimeException("Order not found");
            }

            Order order = optionalOrder.get();
            order.setStatus(status);
            orderRepository.save(order);

            recordStatusHistory(order, status, "STAFF");

            if (OrderStatus.SERVED.name().equals(status)) {

                Optional<Payment> optionalPayment = paymentRepository.findByOrder_OrderId(orderId);
                if (optionalPayment.isPresent()) {
                    Payment payment = optionalPayment.get();
                    payment.setStatus("PAID");
                    payment.setPaidAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                }
            }

            if (order.getCustomerId() > 0) {
                notificationService.createNotification(order.getCustomerId(),
                        "Your order " + orderCode(order.getOrderId()) + " is now " + status + ".");
            }

            log.info("Order status updated successfully");

        }catch (Exception e){
            log.info("Error in method updateOrderStatus" + e.getMessage());
            throw e;
        }
    }

    private void recordStatusHistory(Order order, String status, String changedBy) {

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());
        history.setChangedBy(changedBy);

        orderStatusHistoryRepository.save(history);
    }

    private String orderCode(long orderId) {
        return "#" + String.format("%04d", orderId);
    }

    private OrderDTO mapToDto(Order order) {

        List<OrderItemDTO> itemDTOs = new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItems()) {

            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setMenuItemId(orderItem.getMenuItemId());
            itemDTO.setName(orderItem.getName());
            itemDTO.setPrice(orderItem.getPrice());
            itemDTO.setQty(orderItem.getQty());

            itemDTOs.add(itemDTO);
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setCustomerId(order.getCustomerId());
        orderDTO.setCustomerName(order.getCustomerName());
        orderDTO.setItems(itemDTOs);
        orderDTO.setPickupTime(order.getPickupTime());
        orderDTO.setNotes(order.getNotes());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPlacedAt(order.getPlacedAt());
        orderDTO.setDiscountCode(order.getDiscount() != null ? order.getDiscount().getCode() : null);

        return orderDTO;
    }
}
