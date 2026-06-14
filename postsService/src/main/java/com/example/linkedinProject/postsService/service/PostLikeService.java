package com.example.linkedinProject.postsService.service;

import com.example.linkedinProject.postsService.auth.AuthContextHolder;
import com.example.linkedinProject.postsService.entity.Post;
import com.example.linkedinProject.postsService.entity.PostLike;
import com.example.linkedinProject.postsService.event.PostLiked;
import com.example.linkedinProject.postsService.exception.BadRequestException;
import com.example.linkedinProject.postsService.exception.ResourceNotFoundException;
import com.example.linkedinProject.postsService.repository.PostLikeRepository;
import com.example.linkedinProject.postsService.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, PostLiked> postLikedKafkaTemplate;

    @Transactional
    public void likePost(Long postId) {
        Long userId= AuthContextHolder.getCurrentUserId();
        log.info("User with ID: {} Liking post with ID {}",userId,postId);

       Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post not found with ID:"+postId));

       boolean hasAlreadyLiked=postLikeRepository.existsByUserIdAndPostId(userId,postId);
       if(hasAlreadyLiked)throw new BadRequestException("Posts is already liked");

       PostLike postLike=new PostLike();
       postLike.setPostId(postId);
       postLike.setUserId(userId);
       postLikeRepository.save(postLike);

       PostLiked postLiked=PostLiked.builder()
               .postId(postId)
               .likedUserId(userId)
               .ownerUserId(userId)
               .build();
       postLikedKafkaTemplate.send("post_liked_topic", postLiked);
    }

    @Transactional
    public void unlikePost(Long postId) {
        Long userId=AuthContextHolder.getCurrentUserId();
        log.info("User with ID: {} unLiking post with ID {}",userId,postId);

        postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post not found with ID:"+postId));

        boolean hasAlreadyLiked=postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if(!hasAlreadyLiked)throw new BadRequestException("You can't unlike this post that  you have not liked");

        postLikeRepository.deleteByUserIdAndPostId(userId,postId);

    }
}
