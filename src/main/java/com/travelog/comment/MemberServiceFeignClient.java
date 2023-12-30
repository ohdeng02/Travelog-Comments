package com.travelog.comment;

import com.travelog.comment.dto.MemberInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "members")
public interface MemberServiceFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/members/briefInfo")
    List<MemberInfoDto> getMemberInfo(@RequestBody List<Long> memberIds);
}
