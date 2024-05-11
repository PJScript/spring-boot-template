package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErroCode {

        SERVICE_UNAVAILABLE(503, "SYSTEM001", "Service Unavailable", "Service Unavailable"),

        // JWT 관련 에러
        TOKEN_EXPIRED(401, "AUTH0001", "Authorization Failed", "토큰이 만료되었습니다."),
        UNSUPPORTED_TOKEN(401, "AUTH0002", "Authorization Failed", "지원하지 않는 토큰입니다."),
        MALFORMED_TOKEN(401, "AUTH0003", "Authorization Failed", "형식이 잘못된 토큰입니다."),
        SIGNATURE_INVALID(401, "AUTH0004", "Authorization Failed", "토큰의 서명이 유효하지 않습니다."),
        TOKEN_EMPTY(401, "AUTH0005", "Authorization Failed", "토큰이 제공되지 않았습니다."),
        TOKEN_USER_MISMATCH(401, "AUTH0101", "Authorization Failed", "Token user does not match user information"),

        // auth
        EMAIL_DUPLICATED(409,"AUTH001","Duplicated Email","이미 가입된 이메일"),
        AUTHENTICATION_FAILED(401,"AUTH006","Authorization Failed","존재하지 않는 유저"),
        NOT_FOUND_ROLE(401,"AUTH007","Auth Service Unavailable","Not Found Role");

        private int status;
        private String code;
        private String message;
        private String logMessge;
}

