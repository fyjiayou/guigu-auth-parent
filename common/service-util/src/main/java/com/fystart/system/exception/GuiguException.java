package com.fystart.system.exception;

import com.fystart.common.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fy
 * @date 2022/12/10 14:45
 */
@Data
public class GuiguException extends RuntimeException{

    private Integer code;
    private String msg;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param code
     * @param msg
     */
    public GuiguException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public GuiguException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }
}
