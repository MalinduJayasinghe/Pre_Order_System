package lk.ijse.preordersystem.repository;

import lk.ijse.preordersystem.entity.FavoriteMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteMenuItemRepository extends JpaRepository<FavoriteMenuItem,Long> {

    List<FavoriteMenuItem> findByUser_UserId(long userId);
    Optional<FavoriteMenuItem> findByUser_UserIdAndMenuItem_ItemId(long userId, long itemId);
}
