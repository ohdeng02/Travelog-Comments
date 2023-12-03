package com.travelog.comment;

import com.travelog.comment.dto.BoardReqDto;
import com.travelog.comment.dto.CMRespDto;
import com.travelog.comment.dto.CommentReqDto;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://172.16.210.131:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final BoardServiceFeignClient boardServiceFeignClient;

    //댓글 조회 일단 OK
//    @GetMapping(value = "/{nickname}/{boardId}")
//    public ResponseEntity<?> getComments(@PathVariable String nickname, @PathVariable Long boardId){
//        List<Comment> comments = commentService.getComments(boardId);
//        return new ResponseEntity<>(CMRespDto.builder()
//                .isSuccess(true).msg("댓글 조회").body(comments).build(), HttpStatus.OK);
//    }
    @GetMapping(value = "/{boardId}")
    public List<Comment> getComments(@PathVariable Long boardId){
        List<Comment> comments = commentService.getComments(boardId);
        return comments;
    }

    //댓글 작성
    @PostMapping(value = "/{nickname}/{boardId}")
    public ResponseEntity<?> createComment(@RequestBody CommentReqDto commentReqDto,
                                           @PathVariable String nickname, @PathVariable Long boardId){ //boardId, Member 받아와야함
        Comment comment = commentService.createComment(commentReqDto, boardId);
        int commentSize = commentService.getComments(boardId).size();
        BoardReqDto boardReqDto = new BoardReqDto(boardId, commentSize);
        try{
            commentSize = boardServiceFeignClient.updateCommentSize(boardReqDto);
            log.info("info {}", ": " + commentSize);
        } catch (FeignException e){
            log.error("error {}", ": " + e.getMessage());
        }
        return new ResponseEntity<>(CMRespDto.builder().isSuccess(true).msg("댓글 저장 완료")
                .body(comment.getId()).build(), HttpStatus.OK);
    }

    //댓글 삭제 일단 OK
    @DeleteMapping(value = "/{nickname}/{boardId}/{commentId}")
    public String deleteComment(HttpServletRequest request, @PathVariable String nickname,
                                @PathVariable Long boardId, @PathVariable Long commentId){
        commentService.deleteComment(boardId, commentId);
        String referer = request.getHeader("Referer");
        if(referer == null){
            referer = "/" + nickname;
        }
        return "redirect:" + referer;
    }

    //댓글 수정
}
