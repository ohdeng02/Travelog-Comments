package com.travelog.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberInfoDto {
    private Long memberId;
    private String nickName;
    private String pfp;
}
