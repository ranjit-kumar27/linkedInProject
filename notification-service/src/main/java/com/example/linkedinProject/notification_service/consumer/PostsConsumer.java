package com.example.linkedinProject.notification_service.consumer;

import com.example.linkedinProject.notification_service.entity.Notification;
import com.example.linkedinProject.notification_service.service.NotificationService;
import com.example.linkedinProject.postsService.event.PostCreated;
import com.example.linkedinProject.postsService.event.PostLiked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsConsumer {
    private final NotificationService notificationService;

   @KafkaListener(topics="post_created_topic", groupId = "notification-group")
    public void handlePostCreated(PostCreated postCreated){
       log.info("Received notification: {}", postCreated);
       String message=String .format("your connection with id: %d has created this post: %s",
               postCreated.getOwnerId(),postCreated.getContent());
       Notification notification= Notification.builder()
               .message(message)
               .userId(postCreated.getUserId())
               .build();
       notificationService.addNotification(notification);

   }

    @KafkaListener(topics="post_liked_topic", groupId = "notification-group")
    public void handlePostLiked(PostLiked postLiked){
       log.info("handlePostLiked: {}", postLiked);
       String message =String.format("User with id: %d has liked your post with id: %d",
               postLiked.getLikedUserId(),postLiked.getPostId());
       Notification notification= Notification.builder()
               .message(message)
               .userId(postLiked.getOwnerUserId())
               .build();
       notificationService.addNotification(notification);
    }

}
