package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.MenuItemDTO;
import lk.ijse.preordersystem.entity.Category;
import lk.ijse.preordersystem.entity.MenuItem;
import lk.ijse.preordersystem.repository.CategoryRepository;
import lk.ijse.preordersystem.repository.MenuItemRepository;
import lk.ijse.preordersystem.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    private static final String UPLOAD_DIR = "uploads/menu-images";

    @Override
    public List<MenuItemDTO> getAllMenuItems() {

        try {
            log.info("Execute method getAllMenuItems");
            List<MenuItemDTO> responseList = new ArrayList<>();
            List<MenuItem> menuItemsList = menuItemRepository.findAll();

            for (MenuItem menuItem : menuItemsList) {

                MenuItemDTO menuItemDTO = new MenuItemDTO();
                menuItemDTO.setItemId(menuItem.getItemId());
                menuItemDTO.setName(menuItem.getName());
                menuItemDTO.setCategory(menuItem.getCategory() != null ? menuItem.getCategory().getCategoryName() : null);
                menuItemDTO.setPrice(menuItem.getPrice());
                menuItemDTO.setAvailable(menuItem.isAvailable());
                menuItemDTO.setImageFileName(menuItem.getImageFileName());

                responseList.add(menuItemDTO);
            }

            log.info("MenuItems retrieved successfully");
            return responseList;
        }catch (Exception e){
            log.info("Error in method getAllMenuItems" + e.getMessage());
            throw e;
        }
    }

    @Override
    public MenuItemDTO saveMenuItem(MenuItemDTO menuItemDTO) {

        log.info("Execute method saveMenuItem");

        try {

            MenuItem menuItem = new MenuItem();
            menuItem.setName(menuItemDTO.getName());
            menuItem.setCategory(resolveCategory(menuItemDTO.getCategory()));
            menuItem.setPrice(menuItemDTO.getPrice());
            menuItem.setAvailable(menuItemDTO.isAvailable());
            menuItem.setImageFileName(menuItemDTO.getImageFileName());
            MenuItem savedMenuItem = menuItemRepository.save(menuItem);

            log.info("MenuItem saved successfully");

            MenuItemDTO savedMenuItemDTO = new MenuItemDTO();
            savedMenuItemDTO.setItemId(savedMenuItem.getItemId());
            savedMenuItemDTO.setName(savedMenuItem.getName());
            savedMenuItemDTO.setCategory(savedMenuItem.getCategory() != null ? savedMenuItem.getCategory().getCategoryName() : null);
            savedMenuItemDTO.setPrice(savedMenuItem.getPrice());
            savedMenuItemDTO.setAvailable(savedMenuItem.isAvailable());
            savedMenuItemDTO.setImageFileName(savedMenuItem.getImageFileName());

            return savedMenuItemDTO;

        }catch (Exception e){
            log.info("Error in method saveMenuItem" + e.getMessage());
            throw e;
        }
    }

    @Override
    public MenuItemDTO updateMenuItem(MenuItemDTO menuItemDTO) {

        log.info("Execute method updateMenuItem");

        try {

            Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(menuItemDTO.getItemId());
            if (optionalMenuItem.isEmpty()){
                throw new RuntimeException("Item not found");
            }

            MenuItem menuItem = optionalMenuItem.get();
            menuItem.setName(menuItemDTO.getName());
            menuItem.setCategory(resolveCategory(menuItemDTO.getCategory()));
            menuItem.setPrice(menuItemDTO.getPrice());
            menuItem.setAvailable(menuItemDTO.isAvailable());
            if (menuItemDTO.getImageFileName() != null) {
                menuItem.setImageFileName(menuItemDTO.getImageFileName());
            }
            MenuItem updatedMenuItem = menuItemRepository.save(menuItem);

            log.info("MenuItem updated successfully");

            MenuItemDTO updatedMenuItemDTO = new MenuItemDTO();
            updatedMenuItemDTO.setItemId(updatedMenuItem.getItemId());
            updatedMenuItemDTO.setName(updatedMenuItem.getName());
            updatedMenuItemDTO.setCategory(updatedMenuItem.getCategory() != null ? updatedMenuItem.getCategory().getCategoryName() : null);
            updatedMenuItemDTO.setPrice(updatedMenuItem.getPrice());
            updatedMenuItemDTO.setAvailable(updatedMenuItem.isAvailable());
            updatedMenuItemDTO.setImageFileName(updatedMenuItem.getImageFileName());

            return updatedMenuItemDTO;

        }catch (Exception e){
            log.info("Error in method updateMenuItem" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteMenuItem(Long itemId) {

        log.info("Execute method deleteMenuItem");
        try {
            menuItemRepository.deleteById(itemId);
            log.info("MenuItem deleted successfully");

        }catch (Exception e){
            log.info("Error in method deleteMenuItem" + e.getMessage());
            throw e;
        }
    }

    @Override
    public String saveMenuItemImage(long itemId, MultipartFile file) throws IOException {

        log.info("Execute method saveMenuItemImage");

        try {

            MenuItem menuItem = menuItemRepository.findById(itemId).
                    orElseThrow(() -> new RuntimeException("Item not found"));

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + (extension != null ? "." + extension : "");
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName));

            menuItem.setImageFileName(fileName);
            menuItemRepository.save(menuItem);

            return fileName;

        }catch (Exception e){
            log.info("Error in method saveMenuItemImage" + e.getMessage());
            throw e;
        }
    }

    private Category resolveCategory(String categoryName) {

        log.info("Execute method resolveCategory");

        if (categoryName == null || categoryName.trim().isEmpty()) {
            return null;
        }

        String cleanName = categoryName.trim();
        Optional<Category> existingCategory = categoryRepository.findByCategoryNameIgnoreCase(cleanName);

        if (existingCategory.isPresent()) {
            return existingCategory.get();
        }

        Category category = new Category();
        category.setCategoryName(cleanName);
        return categoryRepository.save(category);
    }
}
