package com.example.smartcloset.board.controller;

import com.example.smartcloset.board.model.Post;
import com.example.smartcloset.board.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 생성자 주입
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/search")
    public List<Post> searchPosts(@RequestParam String title) {
        return postService.searchPostsByTitle(title);
    }

    @GetMapping("/search/page")
    public Page<Post> searchPostsWithPaging(
            @RequestParam String title,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return postService.searchPostsByTitleWithPaging(title, page, size);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.savePost(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        return postService.updatePost(id, updatedPost);
    }

    @PutMapping("/{id}/like")
    public void likePost(@PathVariable Long id) {
        postService.increasePostLikes(id);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
