package com.aisw.community.service;

import com.aisw.community.ifs.CrudInterface;
import com.aisw.community.model.entity.AdminUser;
import com.aisw.community.model.entity.Board;
import com.aisw.community.model.entity.Notice;
import com.aisw.community.model.network.Header;
import com.aisw.community.model.network.request.BoardApiRequest;
import com.aisw.community.model.network.request.NoticeApiRequest;
import com.aisw.community.model.network.response.AdminUserApiResponse;
import com.aisw.community.model.network.response.BoardApiResponse;
import com.aisw.community.model.network.response.NoticeApiResponse;
import com.aisw.community.repository.BoardRepository;
import com.aisw.community.repository.NoticeRepository;
import com.aisw.community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardApiLogicService extends BaseService<BoardApiRequest, BoardApiResponse, Board> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Header<BoardApiResponse> create(Header<BoardApiRequest> request) {
        BoardApiRequest boardApiRequest = request.getData();

        Board board = Board.builder()
                .user(userRepository.getOne(boardApiRequest.getUserId()))
                .build();
        Board newBoard = baseRepository.save(board);

        return Header.OK(response(newBoard));
    }

    @Override
    public Header<BoardApiResponse> read(Long id) {
        return baseRepository.findById(id)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<BoardApiResponse> update(Header<BoardApiRequest> request) {
        BoardApiRequest boardApiRequest = request.getData();

        return baseRepository.findById(boardApiRequest.getId())
                .map(board -> {
                    board.setUser(userRepository.getOne(boardApiRequest.getUserId()));
                    return board;
                })
                .map(board -> baseRepository.save(board))
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(board -> {
                    baseRepository.delete(board);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    private BoardApiResponse response(Board board) {
        BoardApiResponse boardApiResponse = BoardApiResponse.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .build();

        return boardApiResponse;
    }

    @Override
    public Header<List<BoardApiResponse>> search(Pageable pageable) {
        Page<Board> boards = baseRepository.findAll(pageable);

        List<BoardApiResponse> boardApiResponseList = boards.stream()
                .map(this::response)
                .collect(Collectors.toList());

        return Header.OK(boardApiResponseList);
    }
}