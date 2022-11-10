package com.example.springbatch.controller;

import com.example.springbatch.dto.MemberDto;
import com.example.springbatch.entity.MemberEntity;
import com.example.springbatch.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * status
     *
     * @return
     */
    @GetMapping("status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("OK");
    }

    /**
     * 멤버 조회
     *
     * @return
     */
    @GetMapping("member")
    public List<MemberEntity> findAllMember() {
        return memberService.findAllMember();
    }

    /**
     * 회원가입
     *
     * @return
     */
    @PostMapping("member")
    public MemberEntity signUp(@RequestBody MemberDto memberDto) {
        return memberService.signUp(memberDto);
    }

}
