package com.chatapp;

import com.chatapp.auth.users.User;
import com.chatapp.auth.users.UserRepository;
import com.chatapp.messages.ChatWebSocketHandler;
import com.chatapp.messages.ConnectionManager;
import com.chatapp.messages.dto.ChatMessage;
import com.chatapp.messages.entity.Message;
import com.chatapp.messages.service.MessageService;
import com.chatapp.rooms.entity.Room;
import com.chatapp.rooms.repository.RoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Chat WebSocket handler Test")
public class ChatWebSocketHandlerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WebSocketSession session;

    @InjectMocks
    private ChatWebSocketHandler chatWebSocketHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;

    private User testUser;
    private Room testRoom;
    private UUID roomId;
    private UUID userId;
    private UUID chatId;

    @BeforeEach
    public void setUp(){
        chatId=UUID.randomUUID();
        testUser=userRepository.save(User.builder()
                .username("username")
                .email("email@email.com")
                .password(passwordEncoder.encode("password123"))
                .build());

        userId=testUser.getId();

        testRoom=roomRepository.save(Room.builder()
                .name("name")
                .description("description")
                .memberSet(Set.of())
                .build());
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("test the handleTextMessage method -> save the mesasge and brodcast it to room")
    public void test_handle_test_message() throws Exception {
        String payload= """
                {
                    "type":"SEND",
                    "roomId": %s,
                    "content":"Hello Dude!"
                }
                """.formatted(roomId);

        ChatMessage chatMessage=new ChatMessage(
                ChatMessage.Type.SEND, roomId,"Hello dude!");

        when(objectMapper.readValue(payload,ChatMessage.class)).thenReturn(chatMessage);
        when(connectionManager.getUserFromSession(session)).thenReturn(testUser);
        when(messageService.save(roomId,userId,"Hello Dude!")).thenReturn(
                Message.builder()
                        .id(chatId)
                        .room(testRoom)
                        .user(testUser)
                        .build()
        );
        when(objectMapper.writeValueAsString(any())).thenReturn(
                """
                        {
                            "content":"Hello Dude!"
                        }
                        """
        );
        chatWebSocketHandler.handleTextMessage(session,new TextMessage(payload));
        verify(messageService,times(1)).save(roomId,userId,"Hello Dude!");
        verify(connectionManager,times(1)).broadcastToRoom(eq(roomId),any());
    }

    @Test
    @DisplayName("test the handleTextMessage method  without user-> dose nothing")
    public void test_handle_test_message_without_user() throws Exception {
        when(connectionManager.getUserFromSession(session)).thenReturn(null);
        chatWebSocketHandler.handleTextMessage(session,new TextMessage("{}"));
        verify(messageService,never()).save(any(),any(),any());
        verify(session, times(1)).close(any());

    }

    @Test
    @DisplayName("Test the afterConnectionClose method -> remove the session")
    public void test_after_connection_close() throws Exception {
        chatWebSocketHandler.afterConnectionClosed(session, CloseStatus.NORMAL);
        verify(connectionManager,times(1)).removeSession(session);
    }

    @Test
    @DisplayName("Test the handleTransportError method -> remove the session fro mthe connectionManager")
    void test_handle_transport_error() throws Exception {
        chatWebSocketHandler.handleTransportError(session,new RuntimeException("network error"));
        verify(connectionManager,times(1)).removeSession(session);

    }
}
