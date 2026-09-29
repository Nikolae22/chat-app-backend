package com.chatapp.rooms.entity;

import com.chatapp.auth.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "rooms")
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Builder.Default
    private LocalDateTime createdAt=LocalDateTime.now();


    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private Set<RoomMember> memberSet = new HashSet<>();
}
