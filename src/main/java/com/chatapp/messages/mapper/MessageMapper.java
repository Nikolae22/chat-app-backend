package com.chatapp.messages.mapper;

import com.chatapp.messages.dto.MessageResponse;
import com.chatapp.messages.entity.Message;

public interface MessageMapper {

    MessageResponse toResponse(Message message);
}
