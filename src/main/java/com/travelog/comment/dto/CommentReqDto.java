package com.travelog.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReqDto {
    @NotBlank
    private String nickname;
    @NotBlank
    private String content;

    private boolean status;
}
