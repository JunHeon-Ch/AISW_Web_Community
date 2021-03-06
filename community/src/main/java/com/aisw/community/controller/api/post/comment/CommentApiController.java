package com.aisw.community.controller.api.post.comment;

import com.aisw.community.model.network.Header;
import com.aisw.community.model.network.request.post.comment.CommentApiRequest;
import com.aisw.community.model.network.response.post.comment.CommentApiResponse;
import com.aisw.community.service.post.comment.CommentApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/board/comment")
public class CommentApiController {

    @Autowired
    private CommentApiLogicService commentApiLogicService;

    @PostMapping("")
    public Header<CommentApiResponse> create(@RequestBody Header<CommentApiRequest> request) {
        return commentApiLogicService.create(request);
    }

    @DeleteMapping("{id}/{userId}")
    public Header delete(@PathVariable Long id, @PathVariable Long userId) {
        return commentApiLogicService.delete(id, userId);
    }
}