package com.chatapp.rooms.mapper;

import com.chatapp.rooms.dto.RoomResponse;
import com.chatapp.rooms.entity.Room;

public interface RoomMapper {

    RoomResponse toResponse(Room room);
}
