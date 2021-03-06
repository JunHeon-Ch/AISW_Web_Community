package com.aisw.community.repository;

import com.aisw.community.CommunityApplicationTests;
import com.aisw.community.model.entity.user.Account;
import com.aisw.community.model.entity.post.board.Board;
import com.aisw.community.model.entity.post.comment.Comment;
import com.aisw.community.repository.post.board.BoardRepository;
import com.aisw.community.repository.post.comment.CommentRepository;
import com.aisw.community.repository.user.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class CommentRepositoryTest extends CommunityApplicationTests  {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository<Board> boardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void create() {
        String writer = "writer";
        String content = "test Content";
        Boolean isAnonymous = true;
        Account userId = accountRepository.getOne(1L);
        Board boardId = boardRepository.getOne(1L);

        Comment comment = Comment.builder()
                .writer(writer)
                .content(content)
                .isAnonymous(isAnonymous)
                .isDeleted(false)
                .likes(0L)
                .board(boardId)
                .account(userId)
                .build();

        Comment newComment = commentRepository.save(comment);
        System.out.println("newComment: " + newComment);
    }

    @Test
    public void delete() {
        Optional<Comment> comment = commentRepository.findById(1L);

        Assertions.assertTrue(comment.isPresent());

        comment.ifPresent(readComment -> {
            commentRepository.delete(readComment);
        });

        Optional<Comment> deleteComment = commentRepository.findById(1L);

        Assertions.assertFalse(deleteComment.isPresent());
    }
}