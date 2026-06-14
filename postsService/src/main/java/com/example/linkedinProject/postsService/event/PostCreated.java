package com.example.linkedinProject.postsService.event;

import lombok.Builder;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
@Builder
public class PostCreated {
    private Long ownerId;
    private Long postId;
    private Long  userId;
    private String content;
}
