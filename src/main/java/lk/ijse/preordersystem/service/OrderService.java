package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO placeOrder(OrderDTO orderDTO);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByCustomer(long customerId);
    void updateOrderStatus(long orderId, String status);
}