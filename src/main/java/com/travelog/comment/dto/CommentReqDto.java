package com.travelog.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReqDto {
    @NotNull
    private Long memberId;
    @NotBlank
    private String nickname;
    @NotBlank
    private String content;

    private boolean status;
}
