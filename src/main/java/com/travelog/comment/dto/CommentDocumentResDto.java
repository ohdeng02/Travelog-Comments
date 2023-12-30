package com.travelog.comment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommentDocumentResDto {
    private Long commentId;
    private Long boardId;
    private Long memberId;
    private String nickname;
    private String content;
    private String createdAt;
    private String updatedAt;
    private int report;
    private boolean status;

}
