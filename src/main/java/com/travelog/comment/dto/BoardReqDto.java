package com.travelog.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardReqDto {
    private Long boardId;
    private int commentSize;
}
