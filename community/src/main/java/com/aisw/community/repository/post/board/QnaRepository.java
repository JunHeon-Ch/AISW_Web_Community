package com.aisw.community.repository.post.board;

import com.aisw.community.model.entity.post.board.Qna;
import com.aisw.community.model.enumclass.BulletinStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
    Page<Qna> findAllByWriterContaining(String writer, Pageable pageable);
    Page<Qna> findAllByTitleContaining(String title, Pageable pageable);
    Page<Qna> findAllByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    Page<Qna> findAllByStatusOrStatus(BulletinStatus status1, BulletinStatus status2, Pageable pageable);
    Page<Qna> findAllBySubjectIn(List<String> subject, Pageable pageable);
}