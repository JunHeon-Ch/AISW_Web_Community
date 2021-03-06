package com.aisw.community.repository.post.like;

import com.aisw.community.model.entity.post.like.ContentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentLikeRepository extends JpaRepository<ContentLike, Long> {
    List<ContentLike> findAllByAccountId(Long accountId);
    Optional<ContentLike> findContentLikeByAccountIdAndBoardId(Long accountId, Long boardId);
    Optional<ContentLike> findContentLikeByAccountIdAndCommentId(Long accountId, Long commentId);
}