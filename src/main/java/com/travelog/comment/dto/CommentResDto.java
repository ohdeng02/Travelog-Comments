package com.travelog.comment.dto;


import com.travelog.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResDto {
    private Long id;
    private Long boardId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int report;
    private boolean status;

    public CommentResDto(Comment comment){
        this.id = comment.getId();
        this.boardId = comment.getBoardId();
        this.nickname = comment.getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.report = comment.getReport();
        this.status = comment.isStatus();
    }
}
