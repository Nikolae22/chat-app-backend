package com.chatapp.messages.controller;

import com.chatapp.messages.dto.MessageResponse;
import com.chatapp.messages.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms/{roomId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @GetMapping
    public ResponseEntity<List<MessageResponse>> getHistory(
            @PathVariable UUID roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return ResponseEntity.ok(
                messageService.getHistory(
                        roomId,
                        (Pageable) PageRequest.of(page,size, Sort.by("sentAt").descending())
                )
        );
    }


}
