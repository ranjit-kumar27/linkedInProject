package com.example.linkedinProject.postsService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PostCreated {
    private Long ownerId;
    private Long postId;
    private Long  userId;
    private String content;
}
