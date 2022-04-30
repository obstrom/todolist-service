package com.obstrom.todolistservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;

@Getter
@Builder
@ToString
@RedisHash("todo")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Todo {

    @Id String id;
    Date createdAt;
    @Indexed String userId;
    @Setter String message;
    @Setter Boolean completed;
    @Setter Date completedAt;

}
