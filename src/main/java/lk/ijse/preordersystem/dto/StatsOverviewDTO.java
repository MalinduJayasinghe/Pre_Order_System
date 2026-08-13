package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsOverviewDTO {

    private double totalEarned;
    private int ordersCount;
    private double avgOrderValue;
    private int activeOrders;
}
