package com.chatapp.messages.mapper;

import com.chatapp.messages.dto.MessageResponse;
import com.chatapp.messages.entity.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageMapperImpl implements MessageMapper {
    @Override
    public MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getRoom().getId(),
                message.getUser().getId(),
                message.getUser().getUsername(),
                message.getContent(),
                message.getSentAt());
    }
}
