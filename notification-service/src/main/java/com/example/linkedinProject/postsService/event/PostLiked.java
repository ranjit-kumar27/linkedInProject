package com.example.linkedinProject.postsService.event;

import lombok.Builder;
import lombok.Data;

@Data

public class PostLiked {
    private Long postId;
    private Long ownerUserId;
    private Long likedUserId;
}
