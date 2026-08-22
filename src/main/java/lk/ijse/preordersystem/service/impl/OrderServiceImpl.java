package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.OrderDTO;
import lk.ijse.preordersystem.dto.OrderItemDTO;
import lk.ijse.preordersystem.entity.Order;
import lk.ijse.preordersystem.entity.OrderItem;
import lk.ijse.preordersystem.enumeration.OrderStatus;
import lk.ijse.preordersystem.repository.OrderRepository;
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
            double total = 0;

            if (orderDTO.getItems() != null) {
                for (OrderItemDTO itemDTO : orderDTO.getItems()) {

                    OrderItem orderItem = new OrderItem();
                    orderItem.setMenuItemId(itemDTO.getMenuItemId());
                    orderItem.setName(itemDTO.getName());
                    orderItem.setPrice(itemDTO.getPrice());
                    orderItem.setQty(itemDTO.getQty());
                    orderItem.setOrder(order);

                    orderItems.add(orderItem);
                    total += itemDTO.getPrice() * itemDTO.getQty();
                }
            }

            order.setOrderItems(orderItems);
            order.setTotal(total);

            Order savedOrder = orderRepository.save(order);

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

            log.info("Order status updated successfully");

        }catch (Exception e){
            log.info("Error in method updateOrderStatus" + e.getMessage());
            throw e;
        }
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

        return orderDTO;
    }
}