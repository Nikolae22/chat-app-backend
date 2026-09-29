package com.chatapp.rooms.service;

import com.chatapp.auth.users.User;
import com.chatapp.rooms.dto.CreateRoomRequest;
import com.chatapp.rooms.dto.RoomResponse;
import com.chatapp.rooms.entity.Room;
import com.chatapp.rooms.entity.RoomMember;
import com.chatapp.rooms.exceptions.RoomAlreadyExistsException;
import com.chatapp.rooms.exceptions.RoomNotFoundException;
import com.chatapp.rooms.mapper.RoomMapper;
import com.chatapp.rooms.repository.RoomMemberRepository;
import com.chatapp.rooms.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomMapper roomMapper;


    @Transactional
    public List<RoomResponse> listRooms(){
        return roomRepository.findByIsPrivateFalse()
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request, User creator){
        if (roomRepository.existsRoomByName(request.name())){
            throw new RoomAlreadyExistsException("Room "+request.name() + " already exists ");
        }

        Room room=Room.builder()
                .name(request.name())
                .description(request.description())
                .isPrivate(request.isPrivate())
                .createdBy(creator)
                .build();

        Room saved = roomRepository.save(room);
        roomMemberRepository.save(RoomMember.builder()
                .room(saved).user(creator).build());

        return roomMapper.toResponse(saved);
    }

    @Transactional
    public RoomResponse joinRoom(UUID roomId,User user){
        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->new RoomNotFoundException("Room not found"));

        if (!roomMemberRepository.existsByRoomAndUser(room,user)){
            roomMemberRepository.save(RoomMember.builder()
                    .room(room).user(user).build());
        }
        return roomMapper.toResponse(room);
    }
}
