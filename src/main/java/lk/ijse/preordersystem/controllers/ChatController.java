package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.ChatReplyDTO;
import lk.ijse.preordersystem.dto.ChatRequestDTO;
import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.entity.User;
import lk.ijse.preordersystem.repository.UserRepository;
import lk.ijse.preordersystem.service.AiChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final AiChatbotService aiChatbotService;
    private final UserRepository userRepository;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse chat(@RequestBody ChatRequestDTO chatRequestDTO, Authentication authentication) {

        log.info("chat API was called");

        String username = authentication.getName();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("CUSTOMER");

        ChatReplyDTO reply = aiChatbotService.chat(user.getUserId(), username, role, chatRequestDTO.getMessage());

        log.info("chat API successful");
        return new CommonResponse(0, reply, "Chat reply");
    }
}
