package lk.ijse.preordersystem.repository;

import lk.ijse.preordersystem.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuItem, Long> {

    @Query("SELECT m FROM MenuItem m WHERE m.itemId NOT IN (" +
            "SELECT mi.itemId FROM MenuItem mi JOIN mi.ingredients i WHERE i.ingredientName IN :excludedIngredients)")
    List<MenuItem> findAllExcludingIngredients(@Param("excludedIngredients") List<String> excludedIngredients);

    List<MenuItem> findByCategory(String category);
}
