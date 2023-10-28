package com.travelog.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CMRespDto<T> {
    private boolean isSuccess;
    private String msg;
    private T body;
}