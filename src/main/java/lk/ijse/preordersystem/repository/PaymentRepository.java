package lk.ijse.preordersystem.repository;

import lk.ijse.preordersystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findByOrder_OrderId(long orderId);
}
