package com.travelog.comment;

import com.travelog.comment.dto.CMRespDto;
import com.travelog.comment.dto.CommentReqDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private final CommentService commentService;

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
        Comment res = commentService.createComment(commentReqDto, boardId);
        return new ResponseEntity<>(CMRespDto.builder().isSuccess(true).msg("댓글 저장완료")
                .body(res.getId()).build(), HttpStatus.OK);
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
