package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private long orderId;
    private long customerId;
    private String customerName;
    private List<OrderItemDTO> items;
    private String pickupTime;
    private String notes;
    private double total;
    private String status;
    private LocalDateTime placedAt;
}