package com.aisw.community.controller.api;

import com.aisw.community.controller.NoticePostController;
import com.aisw.community.model.entity.Council;
import com.aisw.community.model.network.request.CouncilApiRequest;
import com.aisw.community.model.network.response.CouncilApiResponse;
import com.aisw.community.model.network.response.NoticeResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notice/council")
public class CouncilApiController extends NoticePostController<CouncilApiRequest, NoticeResponseDTO, CouncilApiResponse, Council> {
}
