package lk.ijse.preordersystem.repository;

import lk.ijse.preordersystem.entity.ChatbotQueryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotQueryLogRepository extends JpaRepository<ChatbotQueryLog,Long> {
}
