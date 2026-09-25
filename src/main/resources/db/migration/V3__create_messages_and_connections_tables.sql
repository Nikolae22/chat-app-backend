
create table  messages (
    id UUID primary key ,
    room_id UUID not null references rooms(id) on delete cascade ,
    user_id UUID not null references users(id) on delete cascade ,
    content text not null ,
    sent_at timestamp not null default NOW()
);

create index idx_messages_room on messages(room_id);
create index idx_messages_sent_at on messages(sent_at);


create  table connections(
    id UUID primary key ,
    session_id varchar(255) not null unique ,
    room_id UUID not null references rooms(id) on delete cascade ,
    user_id UUID not null references users(id) on delete set null ,
    connected_at timestamp not null  default NOWT()
);

create index idx_connections_session on connections(session_id);
create index idx_connections_user on connections(user_id);