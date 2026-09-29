package com.chatapp.rooms.repository;

import com.chatapp.auth.users.User;
import com.chatapp.rooms.entity.Room;
import com.chatapp.rooms.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, UUID> {

    boolean existsByRoomAndUser(Room roomId, User user);
}
