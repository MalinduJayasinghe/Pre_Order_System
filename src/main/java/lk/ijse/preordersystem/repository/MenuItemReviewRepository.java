package lk.ijse.preordersystem.repository;

import lk.ijse.preordersystem.entity.MenuItemReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemReviewRepository extends JpaRepository<MenuItemReview,Long> {

    List<MenuItemReview> findByMenuItem_ItemId(long itemId);
}
