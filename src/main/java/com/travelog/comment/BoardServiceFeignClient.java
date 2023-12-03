package com.travelog.comment;

import com.travelog.comment.dto.BoardReqDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "board")
public interface BoardServiceFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/board/comments")
    int updateCommentSize(@Valid @RequestBody BoardReqDto boardReqDto);
}
