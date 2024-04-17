package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;


@ExtendWith(SpringExtension.class)
public class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @InjectMocks
    MenuServiceImpl menuService;

    @Test
    void testGetMenus_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.getMenues(1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);

    }
    @Test
    void testGetMenus_CategoryNotFound() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        given(categoryRepository.findById(1L))
                .willReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.getMenues(1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);

    }
    @Test
    void testGetMenus_CategoryNotInRestaurant() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        RestaurantEntity restaurant2 = new RestaurantEntity(2, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("o", "o"), null);
        CategoryEntity category = new CategoryEntity(1, "category", restaurant2, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));

        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.getMenues(1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);

    }
    @Test
    void testGetMenus_Empty() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));

        assertThat(menuService.getMenues(1L, 1L))
                .hasSize(0);
    }
    @Test
    void testGetMenus_NotEmpty() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.findByCategoryId(category.getId()))
                .willReturn(List.of(menu));
        assertThat(menuService.getMenues(restaurant1.getId(),category.getId()))
                .hasSize(1)
                .element(0)
                .returns(1L, MenuResource::getId)
                .returns("menu", MenuResource::getName)
                .returns(1L, MenuResource::getCategory);
    }

    @Test
    void testGetMenuDetail_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.getMenueDetails(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    void testGetMenuDetail_CategoryNotFound() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        given(categoryRepository.findById(1L))
                .willReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.getMenueDetails(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);
    }
    @Test
    void testGetMenuDetail_MenuNotFound() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        given(menuRepository.findById(1L))
                .willReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.getMenueDetails(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(MenuNotFoundException.class);
    }
    @Test
    void testGetMenuDetail_Found() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());

        given(menuRepository.findById(1L))
                .willReturn(Optional.of(menu));
        assertThat(menuService.getMenueDetails(1L,1L,1L))
                .returns(1L, MenuResource::getId)
                .returns("menu", MenuResource::getName)
                .returns(1L, MenuResource::getCategory);
    }


    @Test
    void testAddMenu_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        MenuEntity menu = new MenuEntity(1, "menu", null, new ArrayList<>());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.createMenu(1L,1L, menu);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }

    @Test
    void testAddMenu_CategoryNotFound() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        given(categoryRepository.findById(1L))
                .willReturn(Optional.empty());
        MenuEntity menu = new MenuEntity(1, "menu", null, new ArrayList<>());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.createMenu(1L,1L, menu);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddMenu_Success() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), new ArrayList<>());
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.save(menu))
                .willReturn(menu);
        assertThat(menuService.createMenu(1L,1L, menu))
                .returns(1L, MenuResource::getId)
                .returns("menu", MenuResource::getName)
                .returns(1L, MenuResource::getCategory);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddMenu_Duplicate() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), new ArrayList<>());
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.save(menu))
                .willThrow(new RuntimeException("duplicate key value violates unique constraint"));
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.createMenu(1,1, menu);
        });
        assertThat(exception.getMessage())
                .isEqualTo("menu with this name already exists in category");

    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddMenu_MissingValues() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), new ArrayList<>());
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.save(menu))
                .willThrow(new RuntimeException("not-null property references a null"));
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.createMenu(1,1, menu);
        });
        assertThat(exception.getMessage())
                .isEqualTo("You must provide all the menu fields");
    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testAddMenu_NotOwner() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), new ArrayList<>());
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.save(menu))
                .willThrow(new UserNotOwnerOfRestaurantException());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.createMenu(1,1, menu);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }



    @Test
    void testUpdateMenu_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        MenuEntity menu = new MenuEntity(1, "menu", null, new ArrayList<>());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.updateMenu(1L,1L, 1L, menu);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    void testUpdateMenu_CategoryNotFound() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        given(categoryRepository.findById(1L))
                .willReturn(Optional.empty());
        MenuEntity menu = new MenuEntity(1, "menu", null, new ArrayList<>());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.updateMenu(1L,1L, 1L, menu);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateMenu_Success() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), new ArrayList<>());
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.findById(1L))
                .willReturn(Optional.of(menu));
        given(menuRepository.save(menu))
                .willReturn(menu);
        assertThat(menuService.updateMenu(1L,1L, 1L, menu))
                .returns(1L, MenuResource::getId)
                .returns("menu", MenuResource::getName)
                .returns(1L, MenuResource::getCategory);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateMenu_Duplicate() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.findById(1L))
                .willReturn(Optional.of(menu));
        given(menuRepository.save(menu))
                .willThrow(new RuntimeException("duplicate key value violates unique constraint"));
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.updateMenu(1,1,1, menu);
        });
        assertThat(exception.getMessage())
                .isEqualTo("duplicate key value violates unique constraint");

    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateMenu_NullName() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.findById(1L))
                .willReturn(Optional.of(menu));
        given(menuRepository.save(menu))
                .willThrow(new RuntimeException("name is required"));
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.updateMenu(1, 1, 1, menu);
        });
        assertThat(exception.getMessage())
                .isEqualTo("name is required");

    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testUpdateMenu_NotOwner() throws Exception {

        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.findById(1L))
                .willReturn(Optional.of(menu));
        given(menuRepository.save(menu))
                .willThrow(new UserNotOwnerOfRestaurantException());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.updateMenu(1,1, 1, menu);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }



    @Test
    void testDeleteMenu_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.deleteMenu(1L,1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testDeleteMenu_Success() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), new ArrayList<>());
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.findById(1L))
                .willReturn(Optional.of(menu));
        doNothing()
                .when(menuRepository).deleteById(1L);
        menuService.deleteMenu(1L,1L, 1L);
    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testDeleteMenu_NotOwner() throws Exception {

        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        CategoryEntity category = new CategoryEntity(1, "category", restaurant1, new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        MenuEntity menu = new MenuEntity(1, "menu", category, new ArrayList<>());
        given(menuRepository.findById(1L))
                .willReturn(Optional.of(menu));
        doThrow(new UserNotOwnerOfRestaurantException())
                .when(menuRepository).deleteById(1L);
        Throwable exception = assertThrows(Exception.class, () -> {
            menuService.deleteMenu(1,1, 1);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }


}
