package lk.ijse.preordersystem.repository;

import lk.ijse.preordersystem.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long> {

    Optional<Discount> findByCodeIgnoreCase(String code);
    List<Discount> findByActiveTrue();
    List<Discount> findByActiveTrueAndDiscountDate(LocalDate discountDate);
}