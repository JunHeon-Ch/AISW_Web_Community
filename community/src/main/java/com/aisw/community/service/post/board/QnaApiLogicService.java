package com.aisw.community.service.post.board;

import com.aisw.community.advice.exception.NotEqualAccountException;
import com.aisw.community.advice.exception.PostNotFoundException;
import com.aisw.community.advice.exception.UserNotFoundException;
import com.aisw.community.model.entity.post.board.Qna;
import com.aisw.community.model.entity.post.like.ContentLike;
import com.aisw.community.model.entity.user.Account;
import com.aisw.community.model.enumclass.BulletinStatus;
import com.aisw.community.model.enumclass.FirstCategory;
import com.aisw.community.model.enumclass.SecondCategory;
import com.aisw.community.model.enumclass.UploadCategory;
import com.aisw.community.model.network.Header;
import com.aisw.community.model.network.Pagination;
import com.aisw.community.model.network.request.post.board.FileUploadToQnaDTO;
import com.aisw.community.model.network.request.post.board.QnaApiRequest;
import com.aisw.community.model.network.response.post.board.BoardApiResponse;
import com.aisw.community.model.network.response.post.board.BoardResponseDTO;
import com.aisw.community.model.network.response.post.board.QnaApiResponse;
import com.aisw.community.model.network.response.post.board.QnaDetailApiResponse;
import com.aisw.community.model.network.response.post.comment.CommentApiResponse;
import com.aisw.community.model.network.response.post.file.FileApiResponse;
import com.aisw.community.repository.post.board.QnaRepository;
import com.aisw.community.repository.post.file.FileRepository;
import com.aisw.community.repository.post.like.ContentLikeRepository;
import com.aisw.community.repository.user.AccountRepository;
import com.aisw.community.service.post.comment.CommentApiLogicService;
import com.aisw.community.service.post.file.FileApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QnaApiLogicService extends BoardPostService<QnaApiRequest, FileUploadToQnaDTO, BoardResponseDTO, QnaDetailApiResponse, QnaApiResponse, Qna> {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private QnaRepository qnaRepository;

    @Autowired
    private ContentLikeRepository contentLikeRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CommentApiLogicService commentApiLogicService;

    @Autowired
    private FileApiLogicService fileApiLogicService;

    @Override
    public Header<QnaApiResponse> create(Header<QnaApiRequest> request) {
        QnaApiRequest qnaApiRequest = request.getData();
        Account account = accountRepository.findById(qnaApiRequest.getAccountId()).orElseThrow(
                () -> new UserNotFoundException(qnaApiRequest.getAccountId()));
        Qna qna = Qna.builder()
                .title(qnaApiRequest.getTitle())
                .writer(account.getName())
                .content(qnaApiRequest.getContent())
                .status(qnaApiRequest.getStatus())
                .views(0L)
                .likes(0L)
                .subject(qnaApiRequest.getSubject())
                .isAnonymous(qnaApiRequest.getIsAnonymous())
                .firstCategory(FirstCategory.BOARD)
                .secondCategory(SecondCategory.QNA)
                .account(account)
                .build();

        Qna newQna = baseRepository.save(qna);
        return Header.OK(response(newQna));
    }

    @Override
    @Transactional
    public Header<QnaApiResponse> create(FileUploadToQnaDTO request) {
        QnaApiRequest qnaApiRequest = request.getQnaApiRequest();

        Account account = accountRepository.findById(qnaApiRequest.getAccountId()).orElseThrow(
                () -> new UserNotFoundException(qnaApiRequest.getAccountId()));
        Qna qna = Qna.builder()
                .title(qnaApiRequest.getTitle())
                .writer(account.getName())
                .content(qnaApiRequest.getContent())
                .status(qnaApiRequest.getStatus())
                .views(0L)
                .likes(0L)
                .subject(qnaApiRequest.getSubject())
                .isAnonymous(qnaApiRequest.getIsAnonymous())
                .firstCategory(FirstCategory.BOARD)
                .secondCategory(SecondCategory.QNA)
                .account(account)
                .build();
        Qna newQna = baseRepository.save(qna);

        MultipartFile[] files = request.getFiles();
        List<FileApiResponse> fileApiResponseList = fileApiLogicService.uploadFiles(files, newQna.getId(), UploadCategory.POST);

        return Header.OK(response(newQna, fileApiResponseList));
    }

    @Override
    @Transactional
    public Header<QnaApiResponse> read(Long id) {
        return baseRepository.findById(id)
                .map(qna -> qna.setViews(qna.getViews() + 1))
                .map(qna -> baseRepository.save((Qna) qna))
                .map(this::response)
                .map(Header::OK)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @Override
    @Transactional
    public Header<QnaApiResponse> update(Header<QnaApiRequest> request) {
        QnaApiRequest qnaApiRequest = request.getData();

        Qna qna = baseRepository.findById(qnaApiRequest.getId()).orElseThrow(
                () -> new PostNotFoundException(qnaApiRequest.getId()));

        if(qna.getAccount().getId() != qnaApiRequest.getAccountId()) {
            throw new NotEqualAccountException(qnaApiRequest.getAccountId());
        }

        qna
                .setTitle(qnaApiRequest.getTitle())
                .setContent(qnaApiRequest.getContent())
                .setStatus(qnaApiRequest.getStatus());
        qna.setIsAnonymous(qnaApiRequest.getIsAnonymous());
        qna.setSubject(qnaApiRequest.getSubject());
        baseRepository.save(qna);

        return Header.OK(response(qna));
    }

    @Override
    @Transactional
    public Header<QnaApiResponse> update(FileUploadToQnaDTO request) {
        QnaApiRequest qnaApiRequest = request.getQnaApiRequest();
        MultipartFile[] files = request.getFiles();

        Qna qna = baseRepository.findById(qnaApiRequest.getId()).orElseThrow(
                () -> new PostNotFoundException(qnaApiRequest.getId()));

        if(qna.getAccount().getId() != qnaApiRequest.getAccountId()) {
            throw new NotEqualAccountException(qnaApiRequest.getAccountId());
        }

        qna.getFileList().stream().forEach(file -> fileRepository.delete(file));
        List<FileApiResponse> fileApiResponseList = fileApiLogicService.uploadFiles(files, qna.getId(), UploadCategory.POST);

        qna
                .setTitle(qnaApiRequest.getTitle())
                .setContent(qnaApiRequest.getContent())
                .setStatus(qnaApiRequest.getStatus());
        qna.setIsAnonymous(qnaApiRequest.getIsAnonymous());
        qna.setSubject(qnaApiRequest.getSubject());
        baseRepository.save(qna);

        return Header.OK(response(qna, fileApiResponseList));
    }

    @Override
    public Header delete(Long id, Long userId) {
        Qna qna = baseRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        if (qna.getAccount().getId() != userId) {
            throw new NotEqualAccountException(userId);
        }

        baseRepository.delete(qna);
        return Header.OK();
    }

    private QnaApiResponse response(Qna qna) {
        QnaApiResponse qnaApiResponse = QnaApiResponse.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .writer(qna.getWriter())
                .content(qna.getContent())
                .status(qna.getStatus())
                .createdAt(qna.getCreatedAt())
                .createdBy(qna.getCreatedBy())
                .updatedAt(qna.getUpdatedAt())
                .updatedBy(qna.getUpdatedBy())
                .views(qna.getViews())
                .likes(qna.getLikes())
                .isAnonymous(qna.getIsAnonymous())
                .subject(qna.getSubject())
                .accountId(qna.getAccount().getId())
                .category(qna.getCategory())
                .fileApiResponseList(qna.getFileList().stream()
                        .map(file -> fileApiLogicService.response(file)).collect(Collectors.toList()))
                .build();

        return qnaApiResponse;
    }

    private QnaApiResponse response(Qna qna, List<FileApiResponse> fileApiResponseList) {
        QnaApiResponse qnaApiResponse = QnaApiResponse.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .writer(qna.getWriter())
                .content(qna.getContent())
                .status(qna.getStatus())
                .createdAt(qna.getCreatedAt())
                .createdBy(qna.getCreatedBy())
                .updatedAt(qna.getUpdatedAt())
                .updatedBy(qna.getUpdatedBy())
                .views(qna.getViews())
                .likes(qna.getLikes())
                .isAnonymous(qna.getIsAnonymous())
                .subject(qna.getSubject())
                .accountId(qna.getAccount().getId())
                .category(qna.getCategory())
                .fileApiResponseList(fileApiResponseList)
                .build();

        return qnaApiResponse;
    }

    @Override
    @Transactional
    public Header<QnaDetailApiResponse> readWithComment(Long id) {
        return baseRepository.findById(id)
                .map(qna -> (Qna)qna.setViews(qna.getViews() + 1))
                .map(this::responseWithComment)
                .map(Header::OK)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    private QnaDetailApiResponse responseWithComment(Qna qna) {
        QnaDetailApiResponse qnaWithCommentApiResponse = QnaDetailApiResponse.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .writer(qna.getWriter())
                .content(qna.getContent())
                .status(qna.getStatus())
                .createdAt(qna.getCreatedAt())
                .createdBy(qna.getCreatedBy())
                .updatedAt(qna.getUpdatedAt())
                .updatedBy(qna.getUpdatedBy())
                .views(qna.getViews())
                .likes(qna.getLikes())
                .isAnonymous(qna.getIsAnonymous())
                .subject(qna.getSubject())
                .category(qna.getCategory())
                .accountId(qna.getAccount().getId())
                .commentApiResponseList(commentApiLogicService.searchByPost(qna.getId()))
                .build();

        return qnaWithCommentApiResponse;
    }

    @Override
    @Transactional
    public Header<QnaDetailApiResponse> readWithCommentAndLike(Long postId, Long accountId) {
        return baseRepository.findById(postId)
                .map(qna -> qna.setViews(qna.getViews() + 1))
                .map(qna -> baseRepository.save((Qna) qna))
                .map(qna -> responseWithCommentAndLike(qna, accountId))
                .map(Header::OK)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    private QnaDetailApiResponse responseWithCommentAndLike(Qna qna, Long accountId) {
        List<CommentApiResponse> commentApiResponseList = commentApiLogicService.searchByPost(qna.getId());

        QnaDetailApiResponse qnaDetailApiResponse = QnaDetailApiResponse.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .writer(qna.getWriter())
                .content(qna.getContent())
                .status(qna.getStatus())
                .createdAt(qna.getCreatedAt())
                .createdBy(qna.getCreatedBy())
                .updatedAt(qna.getUpdatedAt())
                .updatedBy(qna.getUpdatedBy())
                .views(qna.getViews())
                .likes(qna.getLikes())
                .subject(qna.getSubject())
                .isAnonymous(qna.getIsAnonymous())
                .category(qna.getCategory())
                .accountId(qna.getAccount().getId())
                .build();

        List<ContentLike> contentLikeList = contentLikeRepository.findAllByAccountId(accountId);
        contentLikeList.stream().forEach(contentLike -> {
            if (contentLike.getBoard() != null) {
                if (contentLike.getBoard().getId() == qna.getId()) {
                    qnaDetailApiResponse.setCheckLike(true);
                }
            } else if (contentLike.getComment() != null) {
                for (CommentApiResponse commentApiResponse : commentApiResponseList) {
                    if (contentLike.getComment().getId() == commentApiResponse.getId()) {
                        commentApiResponse.setCheckLike(true);
                    }
                    else {
                        for(CommentApiResponse subCommentApiResponse : commentApiResponse.getSubComment()) {
                            if (contentLike.getComment().getId() == subCommentApiResponse.getId()) {
                                subCommentApiResponse.setCheckLike(true);
                            }
                        }
                    }
                }
            }
            qnaDetailApiResponse.setCommentApiResponseList(commentApiResponseList);
        });

        return qnaDetailApiResponse;
    }

    @Override
//    @Cacheable(value = "qnaSearch", key = "#pageable.pageNumber")
    public Header<BoardResponseDTO> search(Pageable pageable) {
        Page<Qna> qnas = baseRepository.findAll(pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

    @Override
//    @Cacheable(value = "qnaSearchByWriter", key = "#writer.concat(':').concat(#pageable.pageNumber)")
    public Header<BoardResponseDTO> searchByWriter(String writer, Pageable pageable) {
        Page<Qna> qnas = qnaRepository.findAllByWriterContaining(writer, pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

    @Override
//    @Cacheable(value = "qnaSearchByTitle", key = "#title.concat(':').concat(#pageable.pageNumber)")
    public Header<BoardResponseDTO> searchByTitle(String title, Pageable pageable) {
        Page<Qna> qnas = qnaRepository.findAllByTitleContaining(title, pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

    @Override
//    @Cacheable(value = "qnaSearchByTitleOrContent", key = "#title.concat(':').concat(#content)")
    public Header<BoardResponseDTO> searchByTitleOrContent(String title, String content, Pageable pageable) {
        Page<Qna> qnas = qnaRepository
                .findAllByTitleContainingOrContentContaining(title, content, pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

//    @Cacheable(value = "qnaSearchBySubject", key = "#pageable.pageNumber#subject")
    public Header<BoardResponseDTO> searchBySubject(List<String> subject, Pageable pageable) {
        Page<Qna> qnas = qnaRepository.findAllBySubjectIn(subject, pageable);
        Page<Qna> qnasByStatus = searchByStatus(pageable);

        return getListHeader(qnas, qnasByStatus);
    }

    public Page<Qna> searchByStatus(Pageable pageable) {
        Page<Qna> qnas = qnaRepository.findAllByStatusOrStatus(
                BulletinStatus.URGENT, BulletinStatus.NOTICE, pageable);

        return qnas;
    }

    private Header<BoardResponseDTO> getListHeader(Page<Qna> qnas, Page<Qna> qnasByStatus) {
        BoardResponseDTO boardResponseDTO = BoardResponseDTO.builder()
                .boardApiResponseList(qnas.stream()
                        .map(board -> BoardApiResponse.builder()
                                .id(board.getId())
                                .title(board.getTitle())
                                .category(board.getCategory())
                                .createdAt(board.getCreatedAt())
                                .status(board.getStatus())
                                .views(board.getViews())
                                .writer(board.getWriter())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        List<BoardApiResponse> boardApiNoticeResponseList = new ArrayList<>();
        List<BoardApiResponse> boardApiUrgentResponseList = new ArrayList<>();
        qnasByStatus.stream().forEach(board -> {
            BoardApiResponse boardApiResponse = BoardApiResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .category(board.getCategory())
                    .createdAt(board.getCreatedAt())
                    .status(board.getStatus())
                    .views(board.getViews())
                    .writer(board.getWriter())
                    .build();
            if(boardApiResponse.getStatus() == BulletinStatus.NOTICE) {
                boardApiNoticeResponseList.add(boardApiResponse);
            }
            else if(boardApiResponse.getStatus() == BulletinStatus.URGENT) {
                boardApiUrgentResponseList.add(boardApiResponse);
            }
        });
        boardResponseDTO.setBoardApiNoticeResponseList(boardApiNoticeResponseList);
        boardResponseDTO.setBoardApiUrgentResponseList(boardApiUrgentResponseList);

        Pagination pagination = Pagination.builder()
                .totalElements(qnas.getTotalElements())
                .totalPages(qnas.getTotalPages())
                .currentElements(qnas.getNumberOfElements())
                .currentPage(qnas.getNumber())
                .build();

        return Header.OK(boardResponseDTO, pagination);
    }
}
