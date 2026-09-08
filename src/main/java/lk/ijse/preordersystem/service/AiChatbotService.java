package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.ChatReplyDTO;

public interface AiChatbotService {

    ChatReplyDTO chat(long userId, String username, String role, String message);
}
