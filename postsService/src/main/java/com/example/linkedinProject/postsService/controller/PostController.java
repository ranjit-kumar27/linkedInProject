package com.example.linkedinProject.postsService.controller;

import com.example.linkedinProject.postsService.auth.AuthContextHolder;
import com.example.linkedinProject.postsService.dto.PostCreateRequestDto;
import com.example.linkedinProject.postsService.dto.PostDto;
import com.example.linkedinProject.postsService.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto,
                                              HttpServletRequest httpServletRequest) {
        PostDto postDto = postService.createPost(postCreateRequestDto,1L);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        Long userId = AuthContextHolder.getCurrentUserId();
        PostDto postDto=postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

        @GetMapping("/user/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPosts(@PathVariable Long userId) {
        List<PostDto> posts=postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }
}
