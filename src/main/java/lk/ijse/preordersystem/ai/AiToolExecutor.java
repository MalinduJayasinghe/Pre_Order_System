package lk.ijse.preordersystem.ai;

import lk.ijse.preordersystem.dto.*;
import lk.ijse.preordersystem.service.MenuService;
import lk.ijse.preordersystem.service.OrderService;
import lk.ijse.preordersystem.service.StatsService;
import lk.ijse.preordersystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiToolExecutor {

    private final MenuService menuService;
    private final OrderService orderService;
    private final StatsService statsService;
    private final UserService userService;

    public List<Map<String, Object>> searchMenuItems(
            String categoryName,
            List<String> includeIngredients,
            List<String> excludeIngredients,
            Double minPrice,
            Double maxPrice) {

        log.info("Execute tool searchMenuItems");

        List<Map<String, Object>> results = new ArrayList<>();
        List<MenuItemDTO> allItems = menuService.getAllMenuItems();

        for (MenuItemDTO item : allItems) {

            if (!item.isAvailable()) {
                continue;
            }

            if (categoryName != null && !categoryName.trim().isEmpty()) {
                if (item.getCategory() == null || !item.getCategory().equalsIgnoreCase(categoryName.trim())) {
                    continue;
                }
            }

            if (minPrice != null && item.getPrice() < minPrice) {
                continue;
            }

            if (maxPrice != null && item.getPrice() > maxPrice) {
                continue;
            }

            List<String> itemIngredients = item.getIngredients() != null ? item.getIngredients() : new ArrayList<>();

            if (includeIngredients != null && !includeIngredients.isEmpty()) {

                boolean hasAllIncluded = true;
                for (String required : includeIngredients) {
                    if (!containsIngredientIgnoreCase(itemIngredients, required)) {
                        hasAllIncluded = false;
                        break;
                    }
                }

                if (!hasAllIncluded) {
                    continue;
                }
            }

            if (excludeIngredients != null && !excludeIngredients.isEmpty()) {

                boolean hasExcluded = false;
                for (String excluded : excludeIngredients) {
                    if (containsIngredientIgnoreCase(itemIngredients, excluded)) {
                        hasExcluded = true;
                        break;
                    }
                }

                if (hasExcluded) {
                    continue;
                }
            }

            Map<String, Object> resultItem = new LinkedHashMap<>();
            resultItem.put("itemId", item.getItemId());
            resultItem.put("name", item.getName());
            resultItem.put("category", item.getCategory());
            resultItem.put("price", item.getPrice());
            resultItem.put("ingredients", itemIngredients);

            results.add(resultItem);
        }

        return results;
    }

    private boolean containsIngredientIgnoreCase(List<String> ingredients, String target) {

        for (String ingredient : ingredients) {
            if (ingredient.equalsIgnoreCase(target.trim())) {
                return true;
            }
        }

        return false;
    }

    public Map<String, Object> getOrderStatus(long orderId, long requestingUserId, String requestingRole) {

        log.info("Execute tool getOrderStatus");

        List<OrderDTO> candidateOrders;

        if ("CUSTOMER".equals(requestingRole)) {
            candidateOrders = orderService.getOrdersByCustomer(requestingUserId);
        } else {
            candidateOrders = orderService.getAllOrders();
        }

        for (OrderDTO order : candidateOrders) {
            if (order.getOrderId() == orderId) {
                return orderToMap(order);
            }
        }

        Map<String, Object> notFound = new LinkedHashMap<>();
        notFound.put("error", "No order with that ID was found for this account.");
        return notFound;
    }

    public List<Map<String, Object>> getMyOrders(long requestingUserId) {

        log.info("Execute tool getMyOrders");

        List<Map<String, Object>> results = new ArrayList<>();
        List<OrderDTO> orders = orderService.getOrdersByCustomer(requestingUserId);

        for (OrderDTO order : orders) {
            results.add(orderToMap(order));
        }

        return results;
    }

    public List<Map<String, Object>> getActiveOrderQueue() {

        log.info("Execute tool getActiveOrderQueue");

        List<Map<String, Object>> results = new ArrayList<>();
        List<OrderDTO> orders = orderService.getAllOrders();

        for (OrderDTO order : orders) {
            if ("PENDING".equals(order.getStatus()) || "PREPARING".equals(order.getStatus()) || "READY".equals(order.getStatus())) {
                results.add(orderToMap(order));
            }
        }

        return results;
    }

    public String updateOrderStatusTool(long orderId, String newStatus) {

        log.info("Execute tool updateOrderStatusTool");

        orderService.updateOrderStatus(orderId, newStatus.toUpperCase());
        return "Order #" + orderId + " status updated to " + newStatus.toUpperCase() + ".";
    }

    public Map<String, Object> getSalesReport() {

        log.info("Execute tool getSalesReport");

        StatsOverviewDTO stats = statsService.getStatsOverview();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalEarned", stats.getTotalEarned());
        result.put("ordersCount", stats.getOrdersCount());
        result.put("avgOrderValue", stats.getAvgOrderValue());
        result.put("activeOrders", stats.getActiveOrders());

        return result;
    }

    public List<Map<String, Object>> getTopSellers() {

        log.info("Execute tool getTopSellers");

        List<Map<String, Object>> results = new ArrayList<>();
        List<TopSellersDTO> topSellers = statsService.getTopSellers();

        for (TopSellersDTO topSeller : topSellers) {

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("name", topSeller.getName());
            entry.put("quantitySold", topSeller.getQuantity());

            results.add(entry);
        }

        return results;
    }

    public List<Map<String, Object>> getStaffList() {

        log.info("Execute tool getStaffList");

        List<Map<String, Object>> results = new ArrayList<>();
        List<UserDTO> users = userService.getAllUsers();

        for (UserDTO user : users) {

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("username", user.getUsername());
            entry.put("role", user.getUserRoles());
            entry.put("enabled", user.isEnabled());

            results.add(entry);
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> placeOrderForCustomer(String customerName, List<Map<String, Object>> items,
                                                       String pickupTime, String notes) {

        log.info("Execute tool placeOrderForCustomer");

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomerId(0L);
        orderDTO.setCustomerName(customerName);
        orderDTO.setPickupTime(pickupTime);
        orderDTO.setNotes(notes);

        List<OrderItemDTO> orderItems = new ArrayList<>();

        for (Map<String, Object> itemMap : items) {

            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setMenuItemId(((Number) itemMap.get("menuItemId")).longValue());
            orderItemDTO.setName((String) itemMap.get("name"));
            orderItemDTO.setPrice(((Number) itemMap.get("price")).doubleValue());
            orderItemDTO.setQty(((Number) itemMap.get("qty")).intValue());

            orderItems.add(orderItemDTO);
        }

        orderDTO.setItems(orderItems);

        OrderDTO savedOrder = orderService.placeOrder(orderDTO);
        return orderToMap(savedOrder);
    }

    private Map<String, Object> orderToMap(OrderDTO order) {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("orderId", order.getOrderId());
        map.put("customerName", order.getCustomerName());
        map.put("status", order.getStatus());
        map.put("pickupTime", order.getPickupTime());
        map.put("notes", order.getNotes());
        map.put("total", order.getTotal());
        map.put("placedAt", order.getPlacedAt());

        List<Map<String, Object>> items = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItemDTO item : order.getItems()) {

                Map<String, Object> itemMap = new LinkedHashMap<>();
                itemMap.put("name", item.getName());
                itemMap.put("qty", item.getQty());
                itemMap.put("price", item.getPrice());

                items.add(itemMap);
            }
        }
        map.put("items", items);

        return map;
    }
}
