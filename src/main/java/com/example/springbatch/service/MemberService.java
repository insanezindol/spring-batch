package com.example.springbatch.service;

import com.example.springbatch.dto.MemberDto;
import com.example.springbatch.entity.MemberEntity;
import com.example.springbatch.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberEntity> findAllMember() {
        return memberRepository.findAll();
    }

    public MemberEntity signUp(MemberDto memberDto) {
        final MemberEntity member = MemberEntity.builder()
                .username(memberDto.getUsername())
                .name(memberDto.getName())
                .build();
        return memberRepository.save(member);
    }

}
