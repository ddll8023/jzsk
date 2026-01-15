package com.szy.common.exception;

import com.szy.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.szy.common.lang.ResponseCode.FAIL;
import static com.szy.common.lang.ResponseCode.INSERT_NOT_UNIQUE;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result<Object> handler(AccessDeniedException e) {
        log.info("security权限不足：----------------{}", e.getMessage());
        return Result.fail(FAIL,"权限不足");
    }

    //处理实体校验的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Object> handler(MethodArgumentNotValidException e) {
        log.info("实体校验异常：----------------{}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.fail(FAIL,objectError.getDefaultMessage());
    }

    //处理Assert的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<Object> handler(IllegalArgumentException e) {
        log.error("Assert异常：----------------{}", e.getMessage());
        return Result.fail(FAIL,e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = DataAccessException.class)
    public Result<Object> handler(SQLIntegrityConstraintViolationException e){
        log.error("新增项存在------{}",e.getMessage());
        return Result.fail(INSERT_NOT_UNIQUE);
    }

    //捕捉其他异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result<Object> handler(RuntimeException e) {
        log.error("运行时异常：----------------{}", e.getMessage());
        return Result.fail(FAIL,e.getMessage());
    }

}
