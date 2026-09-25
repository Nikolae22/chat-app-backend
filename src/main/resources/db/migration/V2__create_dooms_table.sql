
create table rooms(
    id UUID primary key,
    name varchar(255) not null,
    description varchar(500),
    created_by UUID not null references users(id),
    created_at TIMESTAMP not null default NOW()
    );



create table room_members
(
    id        UUID primary key,
    room_id   UUID      not null references rooms (id) ON DELETE CASCADE,
    user_id   UUID      not null references users (id) on delete cascade,
    joined_at TIMESTAMP not null default NOW(),
    unique (room_id, user_id)
);



-- Indice per velocizzare le query sui membri di una specifica stanza
CREATE INDEX idx_room_members_room ON room_members(room_id);

-- Indice per velocizzare le query sulle stanze a cui appartiene uno specifico utente
CREATE INDEX idx_room_members_user ON room_members(user_id);