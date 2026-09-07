package lk.ijse.preordersystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatbotQueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 2000)
    private String userMessage;

    @Column(length = 4000)
    private String botReply;

    private String toolUsed;
    private LocalDateTime askedAt;
}
