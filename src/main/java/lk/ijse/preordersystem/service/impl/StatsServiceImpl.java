package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.StatsOverviewDTO;
import lk.ijse.preordersystem.dto.TopSellersDTO;
import lk.ijse.preordersystem.entity.Order;
import lk.ijse.preordersystem.entity.OrderItem;
import lk.ijse.preordersystem.enumeration.OrderStatus;
import lk.ijse.preordersystem.repository.OrderRepository;
import lk.ijse.preordersystem.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final OrderRepository orderRepository;

    @Override
    public StatsOverviewDTO getStatsOverview() {

        log.info("Execute method getStatsOverview");

        try {

            List<Order> orderList = orderRepository.findAll();

            double totalEarned = 0;
            int ordersCount = 0;
            int activeOrders = 0;

            for (Order order : orderList) {

                if (order.getStatus().equals(OrderStatus.CANCELLED.name())) {
                    continue;
                }

                totalEarned += order.getTotal();
                ordersCount++;

                if (order.getStatus().equals(OrderStatus.PENDING.name()) || order.getStatus().equals(OrderStatus.PREPARING.name())) {
                    activeOrders++;
                }
            }

            double avgOrderValue = ordersCount > 0 ? (totalEarned / ordersCount) : 0;

            StatsOverviewDTO statsOverviewDTO = new StatsOverviewDTO();
            statsOverviewDTO.setTotalEarned(totalEarned);
            statsOverviewDTO.setOrdersCount(ordersCount);
            statsOverviewDTO.setAvgOrderValue(avgOrderValue);
            statsOverviewDTO.setActiveOrders(activeOrders);

            log.info("StatsOverview computed successfully");
            return statsOverviewDTO;

        }catch (Exception e){
            log.info("Error in method getStatsOverview" + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<TopSellersDTO> getTopSellers() {

        log.info("Execute method getTopSellers");

        try {

            List<Order> orderList = orderRepository.findAll();
            Map<String, Integer> quantityByName = new LinkedHashMap<>();

            for (Order order : orderList) {

                if (order.getStatus().equals(OrderStatus.CANCELLED.name())) {
                    continue;
                }

                for (OrderItem orderItem : order.getOrderItems()) {

                    String name = orderItem.getName();
                    int existingQty = quantityByName.containsKey(name) ? quantityByName.get(name) : 0;
                    quantityByName.put(name, existingQty + orderItem.getQty());
                }
            }

            List<TopSellersDTO> topSellers = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : quantityByName.entrySet()) {

                TopSellersDTO topSellersDTO = new TopSellersDTO();
                topSellersDTO.setName(entry.getKey());
                topSellersDTO.setQuantity(entry.getValue());

                topSellers.add(topSellersDTO);
            }

            topSellers.sort((a, b) -> b.getQuantity() - a.getQuantity());

            List<TopSellersDTO> topFive = new ArrayList<>();
            for (int i = 0; i < topSellers.size() && i < 5; i++) {
                topFive.add(topSellers.get(i));
            }

            log.info("TopSellers computed successfully");
            return topFive;

        }catch (Exception e){
            log.info("Error in method getTopSellers" + e.getMessage());
            throw e;
        }
    }
}