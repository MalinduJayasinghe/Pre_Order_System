package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.MenuItemDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MenuService {

    List<MenuItemDTO> getAllMenuItems();
    List<MenuItemDTO> getMenuItemsExcludingIngredients(List<String> excludingIngredients);
    void saveMenuItem(MenuItemDTO menuItemDTO);
    void updateMenuItem(MenuItemDTO menuItemDTO);
    void deleteMenuItem(Long itemId);
    String saveMenuItemImage(long itemId, MultipartFile file) throws IOException;
}