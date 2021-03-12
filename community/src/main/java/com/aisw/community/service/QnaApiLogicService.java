package com.aisw.community.service;

import com.aisw.community.model.entity.*;
import com.aisw.community.model.enumclass.BulletinStatus;
import com.aisw.community.model.enumclass.FirstCategory;
import com.aisw.community.model.enumclass.SecondCategory;
import com.aisw.community.model.network.Header;
import com.aisw.community.model.network.Pagination;
import com.aisw.community.model.network.request.QnaApiRequest;
import com.aisw.community.model.network.response.*;
import com.aisw.community.repository.QnaRepository;
import com.aisw.community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QnaApiLogicService extends PostService<QnaApiRequest, BoardResponse, QnaApiResponse, Qna> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QnaRepository qnaRepository;

    @Override
    public Header<QnaApiResponse> create(Header<QnaApiRequest> request) {
        QnaApiRequest qnaApiRequest = request.getData();

        Qna qna = Qna.builder()
                .title(qnaApiRequest.getTitle())
                .writer(userRepository.getOne(qnaApiRequest.getUserId()).getName())
                .content(qnaApiRequest.getContent())
                .attachmentFile(qnaApiRequest.getAttachmentFile())
                .status(qnaApiRequest.getStatus())
                .views(0L)
                .likes(0L)
                .subject(qnaApiRequest.getSubject())
                .isAnonymous(qnaApiRequest.getIsAnonymous())
                .level(qnaApiRequest.getLevel())
                .firstCategory(FirstCategory.BOARD)
                .secondCategory(SecondCategory.QNA)
                .user(userRepository.getOne(qnaApiRequest.getUserId()))
                .build();

        Qna newQna = baseRepository.save(qna);
        return Header.OK(response(newQna));
    }

    @Override
    @Transactional
    public Header<QnaApiResponse> read(Long id) {
        return baseRepository.findById(id)
                .map(qna -> qna.setViews(qna.getViews() + 1))
                .map(qna -> baseRepository.save((Qna) qna))
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<QnaApiResponse> update(Header<QnaApiRequest> request) {
        QnaApiRequest qnaApiRequest = request.getData();

        return baseRepository.findById(qnaApiRequest.getId())
                .map(qna -> {
                    qna
                            .setTitle(qnaApiRequest.getTitle())
                            .setContent(qnaApiRequest.getContent())
                            .setAttachmentFile(qnaApiRequest.getAttachmentFile())
                            .setStatus(qnaApiRequest.getStatus())
                            .setLevel(qnaApiRequest.getLevel());
                    qna.setIsAnonymous(qnaApiRequest.getIsAnonymous());
                    qna.setSubject(qnaApiRequest.getSubject());

                    return qna;
                })
                .map(qna -> baseRepository.save(qna))
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(qna -> {
                    baseRepository.delete(qna);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    private QnaApiResponse response(Qna qna) {
        QnaApiResponse qnaApiResponse = QnaApiResponse.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .writer(qna.getWriter())
                .content(qna.getContent())
                .attachmentFile(qna.getAttachmentFile())
                .status(qna.getStatus())
                .createdAt(qna.getCreatedAt())
                .createdBy(qna.getCreatedBy())
                .updatedAt(qna.getUpdatedAt())
                .updatedBy(qna.getUpdatedBy())
                .views(qna.getViews())
                .level(qna.getLevel())
                .likes(qna.getLikes())
                .isAnonymous(qna.getIsAnonymous())
                .subject(qna.getSubject())
                .category(qna.getCategory())
                .userId(qna.getUser().getId())
                .build();

        return qnaApiResponse;
    }

    @Override
    public Header<BoardResponse> search(Pageable pageable) {
        Page<Qna> qnas = baseRepository.findAll(pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

    @Override
    public Header<BoardResponse> searchByWriter(String writer, Pageable pageable) {
        Page<Qna> qnas = qnaRepository.findAllByWriterContaining(writer, pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

    @Override
    public Header<BoardResponse> searchByTitle(String title, Pageable pageable) {
        Page<Qna> qnas = qnaRepository.findAllByTitleContaining(title, pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

    @Override
    public Header<BoardResponse> searchByTitleOrContent(String title, String content, Pageable pageable) {
        Page<Qna> qnas = qnaRepository
                .findAllByTitleContainingOrContentContaining(title, content, pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

    public Page<Qna> searchByStatus(Pageable pageable) {
        Page<Qna> qnas = qnaRepository.findAllByStatusOrStatus(
                BulletinStatus.URGENT, BulletinStatus.NOTICE, pageable);

        return qnas;
    }

//    @Cacheable(value = "searchBySubject", key = "#subject")
//    public Header<List<BoardApiResponse>> searchBySubject(String subject, Pageable pageable) {
//        Page<Qna> qnas = qnaRepository.findAllBySubject(subject, pageable);
//
//        return getListHeader(qnas);
//    }

    private Header<BoardResponse> getListHeader
        (Page<Qna> qnas, Page<Qna> qnasByStatus) {
        BoardResponse boardResponse = BoardResponse.builder()
                .boardApiResponseList(qnas.stream()
                        .map(qna -> BoardApiResponse.builder()
                                .id(qna.getId())
                                .title(qna.getTitle())
                                .category(qna.getCategory())
                                .createdAt(qna.getCreatedAt())
                                .status(qna.getStatus())
                                .views(qna.getViews())
                                .writer(qna.getWriter())
                                .build())
                        .collect(Collectors.toList()))
                .boardApiTopResponseList(qnasByStatus.stream()
                        .map(qna -> BoardApiResponse.builder()
                                .id(qna.getId())
                                .title(qna.getTitle())
                                .category(qna.getCategory())
                                .createdAt(qna.getCreatedAt())
                                .status(qna.getStatus())
                                .views(qna.getViews())
                                .writer(qna.getWriter())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        Pagination pagination = Pagination.builder()
                .totalElements(qnas.getTotalElements())
                .totalPages(qnas.getTotalPages())
                .currentElements(qnas.getNumberOfElements())
                .currentPage(qnas.getNumber())
                .build();

        return Header.OK(boardResponse, pagination);
    }

    @Transactional
    public Header<QnaApiResponse> pressLikes(Long id) {
        return baseRepository.findById(id)
                .map(qna -> qna.setLikes(qna.getLikes() + 1))
                .map(qna -> baseRepository.save((Qna)qna))
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }
}
