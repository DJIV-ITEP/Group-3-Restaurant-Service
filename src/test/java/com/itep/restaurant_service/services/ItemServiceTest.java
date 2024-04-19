package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuItemRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.*;
import com.itep.restaurant_service.services.impl.MenuItemServiceImpl;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;


@ExtendWith(SpringExtension.class)
public class ItemServiceTest {

    @Mock
    MenuItemRepository itemRepository;
    @Mock
    MenuRepository menuRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @InjectMocks
    MenuItemServiceImpl itemService;
    void givenNotFoundRestaurant() {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
    }
    RestaurantEntity givenFoundRestaurant() {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        return restaurant1;
    }
    void givenNotFoundCategory() {
        given(categoryRepository.findById(1L))
                .willReturn(Optional.empty());
    }
    CategoryEntity givenFoundCategory(RestaurantEntity restaurant) {
        CategoryEntity category = new CategoryEntity(1, "category", restaurant, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        return category;
    }
    void givenNotFoundMenu() {
        given(menuRepository.findById(1L))
                .willReturn(Optional.empty());
    }
    MenuEntity givenFoundMenu(CategoryEntity category) {
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.findById(1L))
                .willReturn(Optional.of(menu));
        return menu;
    }

    @Test
    void testGetItems_RestaurantNotFound() throws Exception {
        givenNotFoundRestaurant();
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItems(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);

    }
    @Test
    void testGetItems_CategoryNotFound() throws Exception {
        givenFoundRestaurant();
        given(categoryRepository.findById(1L))
                .willReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItems(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);

    }
    @Test
    void testGetItems_MenuNotFound() {
        givenFoundCategory(givenFoundRestaurant());
        givenNotFoundMenu();
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItems(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(MenuNotFoundException.class);

    }
    @Test
    void testGetItems_CategoryNotInRestaurant() throws Exception {
        givenFoundRestaurant();
        RestaurantEntity restaurant2 = new RestaurantEntity(2, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("o", "o"), null);
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(restaurant2)));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));

        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItems(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);

    }
    @Test
    void testGetItems_MenuNotInCategory() throws Exception {
        var restaurant1 = givenFoundRestaurant();
        givenFoundCategory(restaurant1);
        CategoryEntity category2 = new CategoryEntity(2, "category", restaurant1, new ArrayList<>());
        givenFoundMenu(category2);
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItems(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(MenuNotInCategoryException.class);

    }
    @Test
    void testGetItems_Empty() throws Exception {
        givenFoundMenu(givenFoundCategory(givenFoundRestaurant()));
        assertThat(itemService.getItems(1L,1L, 1L))
                .hasSize(0);
    }
    @Test
    void testGetItems_NotEmpty() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findByMenuId(1))
                .willReturn(List.of(item));
        assertThat(itemService.getItems(1,1, 1))
                .hasSize(1)
                .element(0)
                .returns(1L, ItemResource::getId)
                .returns("item", ItemResource::getName)
                .returns("description", ItemResource::getDescription)
                .returns(23.0, ItemResource::getPrice)
                .returns(1.2, ItemResource::getQuantity)
                .returns(1L, ItemResource::getMenu);
    }
    @Test
    void testGetItemsByID_NotEmpty() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findById(item.getId()))
                .willReturn(Optional.of(item));
        assertThat(itemService.getItemsbyIds(List.of(1).toArray(Integer[]::new)))
                .hasSize(1)
                .element(0)
                .returns(1L, ItemResource::getId)
                .returns("item", ItemResource::getName)
                .returns("description", ItemResource::getDescription)
                .returns(23.0, ItemResource::getPrice)
                .returns(1.2, ItemResource::getQuantity)
                .returns(1L, ItemResource::getMenu);
    }

    @Test
    void testGetItemDetail_RestaurantNotFound() {
        givenNotFoundRestaurant();
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItemsDetails(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    void testGetItemDetail_CategoryNotFound() {
        givenFoundRestaurant();
        given(categoryRepository.findById(1L))
                .willReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItemsDetails(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);
    }
    @Test
    void testGetItemDetail_MenuNotFound() {
        givenFoundCategory(givenFoundRestaurant());
        givenNotFoundMenu();
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItemsDetails(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(MenuNotFoundException.class);

    }
    @Test
    void testGetItemDetail_CategoryNotInRestaurant() throws Exception {
        givenFoundRestaurant();
        RestaurantEntity restaurant2 = new RestaurantEntity(2, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("o", "o"), null);
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(restaurant2)));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));

        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItemsDetails(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);

    }
    @Test
    void testGetItemDetail_MenuNotInCategory() {
        RestaurantEntity restaurant1 = givenFoundRestaurant();
        givenFoundCategory(restaurant1);
        CategoryEntity category2 = new CategoryEntity(2, "category", restaurant1, new ArrayList<>());
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(category2));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));

        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItemsDetails(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(MenuNotInCategoryException.class);

    }
    @Test
    void testGetItemDetail_ItemNotFound() {
        givenFoundMenu(givenFoundCategory(givenFoundRestaurant()));
        given(itemRepository.findById(1L))
                .willReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.getItemsDetails(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(ItemNotFoundException.class);
    }
    @Test
    void testGetItemDetail_Found() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));
        assertThat(itemService.getItemsDetails(1,1, 1, 1))
                .returns(1L, ItemResource::getId)
                .returns("item", ItemResource::getName)
                .returns("description", ItemResource::getDescription)
                .returns(23.0, ItemResource::getPrice)
                .returns(1.2, ItemResource::getQuantity)
                .returns(1L, ItemResource::getMenu);
    }


    @Test
    void testAddItem_RestaurantNotFound() {
        givenNotFoundRestaurant();
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,null);
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.createItem(1L,1L,1L, item);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }

    @Test
    void testAddItem_CategoryNotFound() {
        givenFoundRestaurant();
        givenNotFoundCategory();
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,null);
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.createItem(1L,1L,1L, item);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);
    }
    @Test
    void testAddItem_MenuNotFound() {
        givenFoundCategory(givenFoundRestaurant());
        givenNotFoundMenu();
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,null);
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.createItem(1L,1L,1L, item);
        });

        assertThat(exception)
                .isInstanceOf(MenuNotFoundException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddItem_Success() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.save(item))
                .willReturn(item);
        assertThat(itemService.createItem(1L,1L,1L, item))
                .returns(1L, ItemResource::getId)
                .returns("item", ItemResource::getName)
                .returns("description", ItemResource::getDescription)
                .returns(23.0, ItemResource::getPrice)
                .returns(1.2, ItemResource::getQuantity)
                .returns(1L, ItemResource::getMenu);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddItem_Duplicate() {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.save(item))
                .willThrow(new RuntimeException("duplicate key value violates unique constraint"));
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.createItem(1, 1,1, item);
        });
        assertThat(exception.getMessage())
                .isEqualTo("item already exists");

    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddItem_MissingValues() {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.save(item))
                .willThrow(new RuntimeException("not-null property references a null"));
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.createItem(1,1,1, item);
        });
        assertThat(exception.getMessage())
                .isEqualTo("You must provide all the item fields");
    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testAddItem_NotOwner() {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.save(item))
                .willThrow(new UserNotOwnerOfRestaurantException());
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.createItem(1,1,1, item);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }



    @Test
    void testUpdateItem_RestaurantNotFound() {
        givenNotFoundRestaurant();
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,null);
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.updateItem(1L,1L,1L, 1L, item);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    void testUpdateItem_CategoryNotFound() {
        givenFoundRestaurant();
        givenNotFoundCategory();
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,null);
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.updateItem(1L,1L,1L, 1L, item);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);
    }
    @Test
    void testUpdateItem_MenuNotFound() {
        givenFoundCategory(givenFoundRestaurant());
        givenNotFoundMenu();
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,null);
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.updateItem(1L,1L,1L, 1L, item);
        });

        assertThat(exception)
                .isInstanceOf(MenuNotFoundException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateItem_Success() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));
        given(itemRepository.save(item))
                .willReturn(item);
        assertThat(itemService.updateItem(1L,1L,1L,1L, item))
                .returns(1L, ItemResource::getId)
                .returns("item", ItemResource::getName)
                .returns("description", ItemResource::getDescription)
                .returns(23.0, ItemResource::getPrice)
                .returns(1.2, ItemResource::getQuantity)
                .returns(1L, ItemResource::getMenu);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateItem_Duplicate() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));
        given(itemRepository.save(item))
                .willThrow(new RuntimeException("duplicate key value violates unique constraint"));
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.updateItem(1,1,1, 1, item);
        });
        assertThat(exception.getMessage())
                .isEqualTo("Failed to update item");

    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateItem_NullName() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));
        given(itemRepository.save(item))
                .willThrow(new RuntimeException("name is required"));
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.updateItem(1,1, 1, 1, item);
        });
        assertThat(exception.getMessage())
                .isEqualTo("Failed to update item");

    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testUpdateItem_NotOwner() {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));
        given(itemRepository.save(item))
                .willThrow(new UserNotOwnerOfRestaurantException());
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.updateItem(1,1,1, 1, item);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }



    @Test
    void testDeleteItem_RestaurantNotFound() {
        givenNotFoundRestaurant();
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.deleteItem(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    void testDeleteItem_CategoryNotFound() {
        givenFoundRestaurant();
        givenNotFoundCategory();
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.deleteItem(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);
    }
    @Test
    void testDeleteItem_MenuNotFound() {
        givenFoundCategory(givenFoundRestaurant());
        givenNotFoundMenu();
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.deleteItem(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(MenuNotFoundException.class);
    }
    @Test
    void testDeleteItem_ItemNotFound() {
        givenFoundMenu(givenFoundCategory(givenFoundRestaurant()));
        given(itemRepository.findById(1L))
                .willReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.deleteItem(1L,1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(ItemNotFoundException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testDeleteItem_Success() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));
        doNothing()
                .when(itemRepository).deleteById(1L);
        itemService.deleteItem(1L,1L,1L, 1L);
    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testDeleteItem_NotOwner() throws Exception {
        ItemEntity item = new ItemEntity(1, "item", "description", 23.0,1.2,givenFoundMenu(givenFoundCategory(givenFoundRestaurant())));
        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));
        doThrow(new UserNotOwnerOfRestaurantException())
                .when(itemRepository).deleteById(1L);
        Throwable exception = assertThrows(Exception.class, () -> {
            itemService.deleteItem(1,1,1, 1);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }


}
