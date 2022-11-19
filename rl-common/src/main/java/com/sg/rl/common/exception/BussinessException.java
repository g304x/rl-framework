package com.sg.rl.common.exception;


import com.sg.rl.common.constants.ExceptionDefineEnum;

public class BussinessException extends RuntimeException {
    private Integer code;
    private String errorMessage;

    public BussinessException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public BussinessException(ExceptionDefineEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}