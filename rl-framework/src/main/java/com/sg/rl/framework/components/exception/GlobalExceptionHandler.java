package com.sg.rl.framework.components.exception;

import com.sg.rl.common.constants.ExceptionDefineEnum;
import com.sg.rl.common.entity.base.HttpResult;
import com.sg.rl.common.entity.base.R;
import com.sg.rl.common.exception.BussinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(value=HttpStatus.OK)
    @ExceptionHandler(value = BussinessException.class)
    @ResponseBody
    public Object serviceExceptionHandler(BussinessException e, HttpServletRequest request) {
        log.warn(" [ServiceException] errorMsg:{},uri:{},errCode:{}",e.getErrorMessage(),request.getRequestURI(),e.getCode(),e);
        return R.fail(e).setPath(request.getRequestURI());
    }


    @ResponseStatus(value=HttpStatus.OK)
    @ExceptionHandler(value = DataAccessException.class)
    @ResponseBody
    public Object dbExceptionHandler(DataAccessException e,HttpServletRequest request) {
        log.warn(" [dbExceptionHandler] errorMsg:{},uri:{}",e.getMessage(),request.getRequestURI(),e);
        HttpResult hr = new HttpResult();
        R r = null;
        if(e instanceof DuplicateKeyException){
            r = R.fail(ExceptionDefineEnum.EXCEPTION_CODE_DB_DUP_FAILED.getCode()
                    ,ExceptionDefineEnum.EXCEPTION_CODE_DB_DUP_FAILED.getMessage());
        }
        else if(e instanceof DataIntegrityViolationException){
            r = R.fail(ExceptionDefineEnum.EXCEPTION_CODE_DB_UPDATE_FAILED.getCode()
                    ,ExceptionDefineEnum.EXCEPTION_CODE_DB_UPDATE_FAILED.getMessage());

        }
        else if(e instanceof DataAccessResourceFailureException){
            r = R.fail(ExceptionDefineEnum.EXCEPTION_CODE_DB_ACCESS_FAILED.getCode()
                    ,ExceptionDefineEnum.EXCEPTION_CODE_DB_ACCESS_FAILED.getMessage());
        }
        else {
            r = R.fail(ExceptionDefineEnum.EXCEPTION_CODE_DB_UNKNOW_FAILED.getCode()
                    ,e.getMessage());
        }
        return r.setPath(request.getRequestURI());
    }



    @ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Object httpRequestMethodNotSupportedHandler(Exception e, HttpServletRequest request) {
        log.warn(" [HttpRequestMethodNotSupportedException] errorCode:{},uri:{}", ExceptionDefineEnum.METHOD_NOT_ALLOWED.getCode()
                ,request.getRequestURI());

        return R.fail(ExceptionDefineEnum.METHOD_NOT_ALLOWED.getCode()
                ,ExceptionDefineEnum.METHOD_NOT_ALLOWED.getMessage())
                .setPath(request.getRequestURI());

    }


    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object defaultExceptionHandler(Exception e,HttpServletRequest request) {
        log.warn(" [Exception] errorMsg:{},uri:{}",e.getMessage(),request.getRequestURI(),e);
        return R.fail(ExceptionDefineEnum.SYS_ERR.getCode()
                ,e.getMessage()).setPath(request.getRequestURI());
    }



    @ResponseStatus(value=HttpStatus.OK)
    @ExceptionHandler(value=MethodArgumentNotValidException.class)
    @ResponseBody
    public Object MethodArgumentNotValidHandler(HttpServletRequest request,
                                                MethodArgumentNotValidException exception) throws Exception
    {
        log.warn(" [MethodArgumentNotValidException] errorCode:{}",400);
        StringBuffer errDetail = new StringBuffer();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errDetail.append("default message:");
            errDetail.append(error.getDefaultMessage());
            errDetail.append(",");
            errDetail.append("field:");
            errDetail.append(error.getField());
            errDetail.append(",");
            errDetail.append("rejected value:");
            errDetail.append(error.getRejectedValue());
            errDetail.append(";");
        }

        return R.fail(ExceptionDefineEnum.EXCEPTION_CODE_PARM_VALIDATION_ERR.getCode()
                ,errDetail.toString()).setPath(request.getRequestURI());
    }



    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public Object missingServletRequestParameterException(Exception e,HttpServletRequest request) {
        log.warn(" [HttpMessageNotReadableException] miss parameters errorCode:400");
        return R.fail(HttpStatus.BAD_REQUEST.value()
                ,"miss parameters").setPath(request.getRequestURI());
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    @ResponseBody
    public Object missingServletRequestPartException(Exception e,HttpServletRequest request) {
        log.warn(" [HttpMessageNotReadableException] parameters is not present errorCode:400");
        return R.fail(HttpStatus.BAD_REQUEST.value()
                ,"parameters is not present").setPath(request.getRequestURI());
    }



    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Object MethodArgumentTypeMismatchExceptionHandler(Exception e,HttpServletRequest request) {
        log.warn(" [MethodArgumentTypeMismatchException] errorCode:400 parameter type is mismatch");
        return R.fail(HttpStatus.BAD_REQUEST.value()
                ,"parameter type is mismatch").setPath(request.getRequestURI());
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public Object jsonParseExceptionHandler(Exception e,HttpServletRequest request) {
        log.warn(" [HttpMessageNotReadableException] errorCode:400 error json format ");

        return R.fail(HttpStatus.BAD_REQUEST.value()
                ,"error json format ").setPath(request.getRequestURI());
    }


    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Error.class)
    @ResponseBody
    public Object errorHandler(Error ex) {
        log.error("unknown error", ex);
        return R.fail("system abnormal,please contact sys admin");
    }

}