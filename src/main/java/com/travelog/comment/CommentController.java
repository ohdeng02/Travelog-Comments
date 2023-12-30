package com.travelog.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travelog.comment.dto.*;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommentController {
    private final CommentService commentService;
    private final BoardServiceFeignClient boardServiceFeignClient;
    private final MemberServiceFeignClient memberServiceFeignClient;

    @Operation(summary = "특정 게시글의 댓글 전체 조회")
    @GetMapping(value = "/{boardId}")
    public List<CommentListDto> getComments(@PathVariable Long boardId) throws IOException {
        List<CommentDocumentResDto> comments = commentService.getComments(boardId);
        List<Long> membersId = comments.stream().map(CommentDocumentResDto::getMemberId).toList();
        List<MemberInfoDto> memberInfos = new ArrayList<>();
        List<CommentListDto> commentRes = new ArrayList<>();

        try {
            memberInfos = memberServiceFeignClient.getMemberInfo(membersId);
            log.info("info={}" , memberInfos);
        } catch (FeignException e){
            log.error("error={}", e.getMessage());
        }
        for(CommentDocumentResDto comment: comments){
            MemberInfoDto member = memberInfos.stream()
                    .filter(o->o.getMemberId().equals(comment.getMemberId())).findFirst()
                    .orElseGet(()->new MemberInfoDto(null, "존재하지 않은 회원입니다.", null));
            CommentListDto c = new CommentListDto(comment, member);
            log.info("info={}", c);
            commentRes.add(c);
        }
        return commentRes;
    }

    @Operation(summary = "댓글 작성")
    //댓글 작성
    @PostMapping(value = "/{boardId}")
    public ResponseEntity<?> createComment(@RequestBody CommentReqDto commentReqDto,
                                           @PathVariable Long boardId){ //boardId, Member 받아와야함
        CommentResDto comment = commentService.createComment(commentReqDto, boardId);
        int commentSize = commentService.countCommentSize(boardId);
        BoardReqDto boardReqDto = new BoardReqDto(boardId, commentSize);
        try{
            boardServiceFeignClient.updateCommentSize(boardReqDto);
        } catch (FeignException e){
            log.error("error {}", ": " + e.getMessage());
        }
        return new ResponseEntity<>(CMRespDto.builder().isSuccess(true).msg("댓글 저장 완료")
                .body(comment).build(), HttpStatus.OK);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping(value = "/{boardId}/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody CommentReqDto commentReqDto,
                                           @PathVariable Long boardId, @PathVariable Long commentId){
        CommentResDto comment = commentService.updateComment(commentReqDto, boardId, commentId);
        return new ResponseEntity<>(CMRespDto.builder().isSuccess(true).msg("수정 완료")
                .body(comment).build(), HttpStatus.OK);
    }

    @Operation(summary = "댓글 삭제")
    //댓글 삭제 일단 OK
    @DeleteMapping(value = "/{boardId}/{commentId}")
    public void deleteComment(HttpServletRequest request,
                                @PathVariable Long boardId, @PathVariable Long commentId){
        commentService.deleteComment(boardId, commentId);
        int commentSize = commentService.countCommentSize(boardId);
        BoardReqDto boardReqDto = new BoardReqDto(boardId, commentSize);
        try{
            commentSize = boardServiceFeignClient.updateCommentSize(boardReqDto);
        } catch (FeignException e){
            log.error("error {}", ": " + e.getMessage());
        }
    }

}
