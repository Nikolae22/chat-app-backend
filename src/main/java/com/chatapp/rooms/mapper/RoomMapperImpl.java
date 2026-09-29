package com.chatapp.rooms.mapper;

import com.chatapp.rooms.dto.RoomResponse;
import com.chatapp.rooms.entity.Room;
import org.springframework.stereotype.Service;

@Service
public class RoomMapperImpl implements RoomMapper {

    @Override
    public RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getDescription(),
                room.isPrivate(),
                room.getCreatedBy().getId(),
                room.getCreatedAt()
        );
    }
}
