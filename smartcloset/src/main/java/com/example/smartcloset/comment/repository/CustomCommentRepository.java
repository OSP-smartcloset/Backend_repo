package com.example.smartcloset.comment.repository;

import com.example.smartcloset.comment.entity.CommentEntity;

import java.util.List;
import java.util.Map;

public interface CustomCommentRepository {
    /**
     * 댓글의 신고 개수 업데이트를 배치로 처리
     */
    void batchUpdate(List<CommentEntity> commentEntities,
                     Map<Long, Integer> commentIdAndReportCount);
}