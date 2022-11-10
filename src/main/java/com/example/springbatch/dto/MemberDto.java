package com.example.springbatch.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {

    private String username;

    private String name;

    public MemberDto(String username, String name) {
        this.username = username;
        this.name = name;
    }

}
