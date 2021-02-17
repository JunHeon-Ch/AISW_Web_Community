package com.aisw.community.service;

import com.aisw.community.model.entity.Free;
import com.aisw.community.model.entity.FreeComment;
import com.aisw.community.model.entity.QnaComment;
import com.aisw.community.model.network.Header;
import com.aisw.community.model.network.Pagination;
import com.aisw.community.model.network.request.FreeCommentApiRequest;
import com.aisw.community.model.network.response.FreeCommentApiResponse;
import com.aisw.community.model.network.response.QnaCommentApiResponse;
import com.aisw.community.repository.FreeCommentRepository;
import com.aisw.community.repository.FreeRepository;
import com.aisw.community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreeCommentApiLogicService extends CommentService<FreeCommentApiRequest, FreeCommentApiResponse, FreeComment>{

    @Autowired
    private FreeRepository freeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FreeCommentRepository freeCommentRepository;

    @Override
    public Header<FreeCommentApiResponse> create(Header<FreeCommentApiRequest> request) {
        FreeCommentApiRequest freeCommentApiRequest = request.getData();

        System.out.println(request);

        FreeComment freeComment = FreeComment.builder()
                .writer(userRepository.getOne(freeCommentApiRequest.getUserId()).getName())
                .comment(freeCommentApiRequest.getComment())
                .likes(freeCommentApiRequest.getLikes())
                .isAnonymous(freeCommentApiRequest.getIsAnonymous())
                .free(freeRepository.getOne(freeCommentApiRequest.getFreeId()))
                .user(userRepository.getOne(freeCommentApiRequest.getUserId()))
                .build();

        FreeComment newFreeComment = baseRepository.save(freeComment);
        return Header.OK(response(newFreeComment));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(freeComment -> {
                    baseRepository.delete(freeComment);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    private FreeCommentApiResponse response(FreeComment freeComment) {
        FreeCommentApiResponse freeCommentApiResponse = FreeCommentApiResponse.builder()
                .id(freeComment.getId())
                .writer(freeComment.getWriter())
                .comment(freeComment.getComment())
                .createdAt(freeComment.getCreatedAt())
                .likes(freeComment.getLikes())
                .isAnonymous(freeComment.getIsAnonymous())
                .freeId(freeComment.getFree().getId())
                .build();

        return freeCommentApiResponse;
    }

    @Override
    public Header<List<FreeCommentApiResponse>> searchByPost(Long id, Pageable pageable) {
        Page<FreeComment> freeComments = freeCommentRepository.findAllByFreeId(id, pageable);

        List<FreeCommentApiResponse> freeCommentApiResponseList = freeComments.stream()
                .map(this::response)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalElements(freeComments.getTotalElements())
                .totalPages(freeComments.getTotalPages())
                .currentElements(freeComments.getNumberOfElements())
                .currentPage(freeComments.getNumber())
                .build();

        return Header.OK(freeCommentApiResponseList, pagination);
    }

    @Override
    @Transactional
    public Header<FreeCommentApiResponse> pressLikes(Long id) {
        return baseRepository.findById(id)
                .map(freeComment -> freeComment.setLikes(freeComment.getLikes() + 1))
                .map(freeComment -> baseRepository.save(freeComment))
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }
}
