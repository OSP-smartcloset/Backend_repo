package com.example.smartcloset.chat.service;

import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Override
    public void savePost(String content) {
        System.out.println("게시물 저장: " + content);
    }
}
