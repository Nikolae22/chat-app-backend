package com.chatapp.rooms.controller;

import com.chatapp.auth.users.User;
import com.chatapp.rooms.dto.CreateRoomRequest;
import com.chatapp.rooms.dto.RoomResponse;
import com.chatapp.rooms.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;



    @GetMapping
    public ResponseEntity<List<RoomResponse>> listRooms(){
        return ResponseEntity.ok(roomService.listRooms());
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @Valid @RequestBody CreateRoomRequest request,
            @AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.createRoom(request,user));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<RoomResponse> joinRoom(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(roomService.joinRoom(id,user));
    }
}
