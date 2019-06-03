package com.support.mvc.exception;


import com.support.mvc.enums.Code;

import java.util.function.Consumer;

/**
 * 自定义异常:指定返回编码异常,禁止指定Code.SUCCESS
 *
 * @author 谢长春 2017年7月21日 下午1:02:04
 */
public class CodeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Code code;

    public CodeException(CodeException e) {
        super(String.format("%s:%s", e.getCode(), e.getMessage()), e);
        this.code = e.getCode();
        if (Code.SUCCESS == this.code) {
            this.code = Code.FAILURE;
        }
    }

    public CodeException(Code code, String message) {
        super(String.format("%s:%s", code.name(), message));
        this.code = code;
        if (Code.SUCCESS == this.code) {
            this.code = Code.FAILURE;
        }
    }

    public CodeException(Code code, String message, Throwable cause) {
        super(String.format("%s:%s", code.name(), message), cause);
        this.code = code;
        if (Code.SUCCESS == this.code) {
            this.code = Code.FAILURE;
        }
    }

    public Code getCode() {
        return code;
    }

    public CodeException go(final Consumer<CodeException> consumer) {
        consumer.accept(this);
        return this;
    }
}
