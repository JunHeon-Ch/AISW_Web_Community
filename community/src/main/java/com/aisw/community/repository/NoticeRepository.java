package com.aisw.community.repository;

import com.aisw.community.model.entity.Board;
import com.aisw.community.model.entity.Free;
import com.aisw.community.model.entity.Notice;
import com.aisw.community.model.enumclass.BulletinStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository<T extends Notice> extends JpaRepository<T, Long> {
    Page<Notice> findAllByWriterContaining(String writer, Pageable pageable);

    Page<Notice> findAllByTitleContaining(String title, Pageable pageable);

    Page<Notice> findAllByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    Page<Notice> findAllByStatusOrStatus(BulletinStatus status1, BulletinStatus status2, Pageable pageable);
}
