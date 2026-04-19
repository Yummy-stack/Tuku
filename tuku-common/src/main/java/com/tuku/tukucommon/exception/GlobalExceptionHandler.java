package com.tuku.tukucommon.exception;

import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukucommon.BaseResponse;
import com.tuku.tukucommon.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error(e.toString());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error(e.toString());
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
