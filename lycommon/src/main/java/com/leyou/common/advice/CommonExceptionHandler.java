package com.leyou.common.advice;


import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author: oyyb
 * @data: 2020-03-17
 * @version: 1.0.0
 * @descript:
 */

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException e){
        ExceptionEnum em=e.getExceptionEnums();
        return ResponseEntity.status(em.getCode()).body(new ExceptionResult(em));
    }
}
