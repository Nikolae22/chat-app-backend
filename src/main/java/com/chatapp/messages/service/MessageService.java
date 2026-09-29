package com.chatapp.messages.service;

import com.chatapp.auth.users.User;
import com.chatapp.auth.users.UserRepository;
import com.chatapp.messages.dto.MessageResponse;
import com.chatapp.messages.entity.Message;
import com.chatapp.messages.mapper.MessageMapper;
import com.chatapp.messages.reposiotry.MessageRepository;
import com.chatapp.rooms.entity.Room;
import com.chatapp.rooms.exceptions.RoomNotFoundException;
import com.chatapp.rooms.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Transactional
    public Message save(UUID roomId,UUID userId, String content){
        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->new RoomNotFoundException("Room not found "+roomId));

        User user=userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found "+userId));

        Message message=Message.builder()
                .room(room)
                .user(user)
                .content(content)
                .build();

        return messageRepository.save(message);
    }


    @Transactional(readOnly = true)
    public List<MessageResponse> getHistory(UUID roomId, Pageable pageable){
       return messageRepository.findByRoomIdOrderBySendAtDesc(roomId,pageable)
                .stream()
                .map(messageMapper::toResponse)
                .toList();
    }
}
