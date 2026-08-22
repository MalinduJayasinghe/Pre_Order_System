package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.OrderDTO;
import lk.ijse.preordersystem.dto.OrderStatusUpdateDTO;
import lk.ijse.preordersystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse placeOrder(@RequestBody OrderDTO orderDTO) {

        log.info("placeOrder API was called");
        OrderDTO savedOrder = orderService.placeOrder(orderDTO);

        log.info("placeOrder API successful");
        return new CommonResponse(0, savedOrder, "Order placed successfully");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllOrders() {

        log.info("getAllOrders API was called");
        List<OrderDTO> allOrders = orderService.getAllOrders();

        log.info("getAllOrders API successful");
        return new CommonResponse(0, allOrders, "Orders called");
    }

    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getOrdersByCustomer(@PathVariable long customerId) {

        log.info("getOrdersByCustomer API was called");
        List<OrderDTO> customerOrders = orderService.getOrdersByCustomer(customerId);

        log.info("getOrdersByCustomer API successful");
        return new CommonResponse(0, customerOrders, "Customer orders called");
    }

    @PatchMapping(value = "/{orderId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateOrderStatus(@PathVariable long orderId, @RequestBody OrderStatusUpdateDTO orderStatusUpdateDTO) {

        log.info("updateOrderStatus API was called");
        orderService.updateOrderStatus(orderId, orderStatusUpdateDTO.getStatus());

        log.info("updateOrderStatus API successful");
        return new CommonResponse(0, "Order Status Updated", "Status updated successfully");
    }
}