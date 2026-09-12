package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.MenuItemDTO;
import lk.ijse.preordersystem.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "v1/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addMenuItem(@RequestBody MenuItemDTO menuItemDTO) {

        log.info("addMenuItem API was called");
        MenuItemDTO savedMenuItem = menuService.saveMenuItem(menuItemDTO);

        log.info("addMenuItem API successful");
        return new CommonResponse(0, savedMenuItem, "Item added successfully");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> addMenuItemWithImage(
            @RequestPart("menuItem") MenuItemDTO menuItemDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        log.info("addMenuItemWithImage API was called");
        try {
            MenuItemDTO savedMenuItem = menuService.saveMenuItem(menuItemDTO);

            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = menuService.saveMenuItemImage(savedMenuItem.getItemId(), imageFile);
                savedMenuItem.setImageFileName(fileName);
            }

            return ResponseEntity.ok(new CommonResponse(0, savedMenuItem, "Item added successfully"));
        } catch (Exception e) {
            log.error("Error in addMenuItemWithImage: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(1, "Failed to process menu item: " + e.getMessage(), null));
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        log.info("updateMenuItem API was called");
        try {
            menuService.updateMenuItem(menuItemDTO);
            return ResponseEntity.ok(new CommonResponse(0, "Menu Item Updated", "Item updated successfully"));
        } catch (Exception e) {
            log.error("Error in updateMenuItem: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(1, "Failed to update menu item: " + e.getMessage(), null));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllMenuItems() {
        log.info("getAllMenuItems API was called");
        List<MenuItemDTO> allMenuItems = menuService.getAllMenuItems();
        log.info("getAllMenuItems API successful");
        return new CommonResponse(0, allMenuItems, "MenuItems called");
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterMenuItems(@RequestParam(required = false) List<String> excludedIngredients) {
        log.info("filterMenuItems API was called");
        List<MenuItemDTO> allMenuItems = menuService.getMenuItemsExcludingIngredients(excludedIngredients);
        log.info("filterMenuItems API successful");
        return new CommonResponse(0, allMenuItems, "Excluded MenuItems filtered");
    }

    @DeleteMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteMenuItem(@PathVariable Long itemId) {
        log.info("deleteMenuItem API was called");
        menuService.deleteMenuItem(itemId);
        log.info("deleteMenuItem API successful");
        return new CommonResponse(0, "Menu Item Deleted", "Item deleted successfully");
    }

    @PostMapping(value = "/{itemId}/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> addMenuItemImage(@PathVariable Long itemId, @RequestParam("file") MultipartFile file) {
        log.info("addMenuItemImage API was called");
        try {
            String fileName = menuService.saveMenuItemImage(itemId, file);
            log.info("addMenuItemImage API successful");
            return ResponseEntity.ok(new CommonResponse(0, fileName, "Image added successfully"));
        } catch (Exception e) {
            log.error("addMenuItemImage API failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(1, "Image upload failed: " + e.getMessage(), null));
        }
    }
}
