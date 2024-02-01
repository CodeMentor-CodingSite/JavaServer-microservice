package com.codementor.question.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ResponseDto<T> {
    private final HttpStatus httpStatus = HttpStatus.OK;
    private String message = "success";
    private T data;

    public ResponseDto(T data, String msg) {
        this.message = msg;
        this.data = data;
    }



    //Response 성공시

    public static <S> ResponseDto<S> ok(S data) {
        return new ResponseDto<S>(data, "success");
    }

    public static <S> ResponseDto<S> ok(S data, String msg) {
        return new ResponseDto<S>(data, msg);
    }

    //Response 성공시 (메세지만 출력)
    public static <S> ResponseDto<S> ok(String msg) {
        return new ResponseDto<S>(null, msg);
    }

}