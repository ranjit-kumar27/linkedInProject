package com.example.linkedinProject.postsService.service;

import com.example.linkedinProject.postsService.auth.AuthContextHolder;
import com.example.linkedinProject.postsService.client.ConnectionsServiceClient;
import com.example.linkedinProject.postsService.dto.PersonDto;
import com.example.linkedinProject.postsService.dto.PostCreateRequestDto;
import com.example.linkedinProject.postsService.dto.PostDto;
import com.example.linkedinProject.postsService.entity.Post;
import com.example.linkedinProject.postsService.event.PostCreated;
import com.example.linkedinProject.postsService.exception.ResourceNotFoundException;
import com.example.linkedinProject.postsService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsServiceClient connectionsServiceClient;
    private final KafkaTemplate<Long, PostCreated> postCreatedKafkaTemplate ;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto,Long userId) {
        log.info("Creating post for user with id {}", userId);
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        post = postRepository.save(post);

        List<PersonDto> personDtoList =connectionsServiceClient.getFirstDegreeConnections(userId);

        for( PersonDto person : personDtoList ) { //send notification to each connection
            PostCreated postCreated= PostCreated.builder()
                    .postId(post.getId())
                    .content(post.getContent())
                    .userId(person.getUserId())
                    .ownerId(userId)
                    .build();
            postCreatedKafkaTemplate.send("post_created_topic",postCreated);
        }

        return modelMapper.map(post, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting the post with ID {}", postId);

        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found " +
                "with ID " + postId ));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.info("Getting all posts of user with ID: {}", userId);
        List<Post> postList=postRepository.findByUserId(userId);
        return postList
                .stream()
                .map((element)->modelMapper.map(element,PostDto.class))
                .collect(Collectors.toList());
    }
}
