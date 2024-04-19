package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import com.itep.restaurant_service.services.impl.CategoryServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.CategoryNotInRestaurantException;
import com.itep.restaurant_service.services.impl.errorsHandels.RestaurantNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.UserNotOwnerOfRestaurantException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;


@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @InjectMocks
    CategoryServiceImpl categoryService;
    void assertEmptyRestaurant(Throwable exception) {
        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }

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
    @Test
    void testGetCategories_RestaurantNotFound() {
        givenNotFoundRestaurant();
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.getCategory(1L);
        });
        assertEmptyRestaurant(exception);
    }
    @Test
    void testGetCategories_Empty() throws Exception {
        givenFoundRestaurant();
        assertThat(categoryService.getCategory(1L))
                .hasSize(0);
    }
    @Test
    void testGetMenus_NotEmpty() throws Exception {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.findByRestaurantId(1L))
                .willReturn(List.of(category));
        assertThat(categoryService.getCategory(1L))
                .hasSize(1)
                .element(0)
                .returns(1L, CategoryResource::getId)
                .returns("category", CategoryResource::getName)
                .returns(1L, CategoryResource::getRestaurant);
    }

    @Test
    void testGetCategoryDetail_RestaurantNotFound() {
        givenNotFoundRestaurant();
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.getCategoryDetails(1L, 1L);
        });
        assertEmptyRestaurant(exception);
    }
    @Test
    void testGetCategoryDetail_CategoryNotFound() {
        givenFoundRestaurant();
        given(categoryRepository.findById(1L))
                .willReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.getCategoryDetails(1L, 1L);
        });

        assertThat(exception)
                .isInstanceOf(CategoryNotInRestaurantException.class);
    }
    @Test
    void testGetCategoryDetail_Found() throws Exception {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        assertThat(categoryService.getCategoryDetails(1L,1L))
                .returns(1L, CategoryResource::getId)
                .returns("category", CategoryResource::getName)
                .returns(1L, CategoryResource::getRestaurant);
    }


    @Test
    void testAddCategory_RestaurantNotFound() {
        givenNotFoundRestaurant();
        CategoryEntity category = new CategoryEntity(1, "category", null, new ArrayList<>());
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.createCategory(1L, category);
        });
        assertEmptyRestaurant(exception);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddCategory_Success() throws Exception {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.save(category))
                .willReturn(category);
        assertThat(categoryService.createCategory(1L, category))
                .returns(1L, CategoryResource::getId)
                .returns("category", CategoryResource::getName)
                .returns(1L, CategoryResource::getRestaurant);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddCategory_Duplicate() {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.save(category))
                .willThrow(new RuntimeException("duplicate key value violates unique constraint"));
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.createCategory(1, category);
        });
        assertThat(exception.getMessage())
                .isEqualTo("category with this name already exists");

    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAddCategory_MissingValues() {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.save(category))
                .willThrow(new RuntimeException("not-null property references a null"));
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.createCategory(1, category);
        });
        assertThat(exception.getMessage())
                .isEqualTo("You must provide all the category fields");
    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testAddCategory_NotOwner() {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.save(category))
                .willThrow(new UserNotOwnerOfRestaurantException());
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.createCategory(1, category);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }



    @Test
    void testUpdateCategory_RestaurantNotFound() {
        givenNotFoundRestaurant();
        CategoryEntity category = new CategoryEntity(1, "category", null, new ArrayList<>());
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.updateCategory(1L, 1L, category);
        });
        assertEmptyRestaurant(exception);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateCategory_Success() throws Exception {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        given(categoryRepository.save(category))
                .willReturn(category);
        assertThat(categoryService.updateCategory(1L, 1L, category))
                .returns(1L, CategoryResource::getId)
                .returns("category", CategoryResource::getName)
                .returns(1L, CategoryResource::getRestaurant);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateCategory_Duplicate() {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        given(categoryRepository.save(category))
                .willThrow(new RuntimeException("duplicate key value violates unique constraint"));
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.updateCategory(1, 1, category);
        });
        assertThat(exception.getMessage())
                .isEqualTo("duplicate key value violates unique constraint");

    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testUpdateCategory_NullName() {
        CategoryEntity category = new CategoryEntity(1, "", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        given(categoryRepository.save(category))
                .willThrow(new RuntimeException("name is required"));
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.updateCategory(1, 1, category);
        });
        assertThat(exception.getMessage())
                .isEqualTo("name is required");

    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testUpdateCategory_NotOwner() {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        given(categoryRepository.save(category))
                .willThrow(new UserNotOwnerOfRestaurantException());
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.updateCategory(1, 1, category);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }



    @Test
    void testDeleteCategory_RestaurantNotFound() {
        givenNotFoundRestaurant();
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.deleteCategory(1L, 1L);
        });
        assertEmptyRestaurant(exception);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testDeleteCategory_Success() throws Exception {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        doNothing()
                .when(categoryRepository).deleteById(1L);
        categoryService.deleteCategory(1L, 1L);
    }
    @Test
    @WithMockUser(username = "o1", password = "o1")
    void testDeleteCategory_NotOwner() {
        CategoryEntity category = new CategoryEntity(1, "category", givenFoundRestaurant(), new ArrayList<>());
        given(categoryRepository.findById(1L))
                .willReturn(Optional.of(category));
        doThrow(new UserNotOwnerOfRestaurantException())
                .when(categoryRepository).deleteById(1L);
        Throwable exception = assertThrows(Exception.class, () -> {
            categoryService.deleteCategory(1, 1);
        });
        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }


}
