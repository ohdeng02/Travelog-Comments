package com.travelog.comment.dto;

import com.travelog.comment.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentListDto {
    private Long id;
    private Long boardId;
    private Long memberId;
    private String nickname;
    private String pfp;
    private String content;
    private String createdAt;
    private String updatedAt;
    private int report;
    private boolean status;

    public CommentListDto(CommentDocumentResDto comment, MemberInfoDto member){
        this.id = comment.getCommentId();
        this.boardId = comment.getBoardId();
        this.memberId = comment.getMemberId();
        this.nickname = member.getNickName();
        this.pfp = member.getPfp();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.report = comment.getReport();
        this.status = comment.isStatus();
    }
}
