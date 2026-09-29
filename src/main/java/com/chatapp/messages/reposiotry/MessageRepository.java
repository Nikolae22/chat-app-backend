package com.chatapp.messages.reposiotry;

import com.chatapp.messages.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByRoomIdOrderBySendAtDesc(UUID roomId, Pageable pageable);
}
