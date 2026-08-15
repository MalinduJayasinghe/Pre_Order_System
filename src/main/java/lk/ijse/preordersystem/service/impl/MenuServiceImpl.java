package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.MenuItemDTO;
import lk.ijse.preordersystem.entity.Ingredient;
import lk.ijse.preordersystem.entity.MenuItem;
import lk.ijse.preordersystem.repository.IngredientRepository;
import lk.ijse.preordersystem.repository.MenuItemRepository;
import lk.ijse.preordersystem.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;

    private static final String UPLOAD_DIR = "resources/menu-images";

    @Override
    public List<MenuItemDTO> getAllMenuItems(MenuItemDTO menuItemDTO) {

        try {
            log.info("Execute method getAllMenuItems");
            List<MenuItemDTO> responseList = new ArrayList<>();
            List<MenuItem> menuItemsList = menuItemRepository.findAll();

            for (MenuItem menuItem : menuItemsList) {

                menuItemDTO = new MenuItemDTO();
                menuItemDTO.setItemId(menuItem.getItemId());
                menuItemDTO.setName(menuItem.getName());
                menuItemDTO.setCategory(menuItem.getCategory());
                menuItemDTO.setPrice(menuItem.getPrice());
                menuItemDTO.setAvailable(menuItem.isAvailable());
                menuItemDTO.setImageFileName(menuItem.getImageFileName());
                menuItemDTO.setIngredients(menuItem.getIngredients());

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
    public List<MenuItemDTO> getMenuItemsExcludingIngredients(List<String> excludingIngredients) {

        log.info("Execute method getMenuItemsExcludingIngredients");

        try {

            if(excludingIngredients==null || excludingIngredients.isEmpty()){
                return getAllMenuItems(new MenuItemDTO());
            }

            List<String> excludeIngredients = new ArrayList<>();
            for (String ingredient : excludingIngredients) {

                excludeIngredients.add(ingredient.toLowerCase().trim());
            }

            List<MenuItem> menuItemList = menuItemRepository.findAllExcludingIngredients(excludeIngredients);
            List<MenuItemDTO> responseList = new ArrayList<>();

            for (MenuItem menuItem : menuItemList) {

                MenuItemDTO menuItemDTO = new MenuItemDTO();
                menuItemDTO.setItemId(menuItem.getItemId());
                menuItemDTO.setName(menuItem.getName());
                menuItemDTO.setCategory(menuItem.getCategory());
                menuItemDTO.setPrice(menuItem.getPrice());
                menuItemDTO.setAvailable(menuItem.isAvailable());
                menuItemDTO.setImageFileName(menuItem.getImageFileName());
                menuItemDTO.setIngredients(menuItem.getIngredients());

                responseList.add(menuItemDTO);
            }

            log.info("MenuItems retrieved successfully");
            return responseList;

        }catch (Exception e){
            log.info("Error in method getMenuItemsExcludingIngredients" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void saveMenuItem(MenuItemDTO menuItemDTO) {

        log.info("Execute method saveMenuItem");

        try {

            MenuItem menuItem = new MenuItem();
            menuItem.setName(menuItemDTO.getName());
            menuItem.setCategory(menuItemDTO.getCategory());
            menuItem.setPrice(menuItemDTO.getPrice());
            menuItem.setAvailable(menuItemDTO.isAvailable());
            menuItem.setImageFileName(menuItemDTO.getImageFileName());
            menuItem.setIngredients(menuItemDTO.getIngredients());
            menuItemRepository.save(menuItem);

            menuItemRepository.save(menuItem);
            log.info("MenuItem saved successfully");

        }catch (Exception e){
            log.info("Error in method saveMenuItem" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateMenuItem(MenuItemDTO menuItemDTO) {

        log.info("Execute method updateMenuItem");

        try {

            Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(menuItemDTO.getItemId());
            if (optionalMenuItem.isEmpty()){
                throw new Exception("Item not found");
            }

            MenuItem menuItem = optionalMenuItem.get();
            menuItem.setName(menuItemDTO.getName());
            menuItem.setCategory(menuItemDTO.getCategory());
            menuItem.setPrice(menuItemDTO.getPrice());
            menuItem.setAvailable(menuItemDTO.isAvailable());
            menuItem.setImageFileName(menuItemDTO.getImageFileName());
            menuItem.setIngredients(menuItemDTO.getIngredients());
            menuItemRepository.save(menuItem);
            log.info("MenuItem updated successfully");

        }catch (Exception e){
            log.info("Error in method updateMenuItem" + e.getMessage());
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
}
