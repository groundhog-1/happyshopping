package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Data;


/**
 * @author: oyyb
 * @data: 2020-03-17
 * @version: 1.0.0
 * @descript:
 */

@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timeStamp;

    public ExceptionResult(ExceptionEnum em){
        this.status=em.getCode();
        this.message=em.getMsg();
        this.timeStamp=System.currentTimeMillis();
    }
}
