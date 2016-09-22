package com.utils.Exception;

/**
 * Created with IntelliJ IDEA.
 * User: xiaoyx
 * Date: 2016-9-16
 * Time: 23:07
 * 拷贝异常，通常为参数个数不相等或源对象属性在目标对象中不存在
 */
public class CopyException extends RuntimeException {
    public CopyException(String exceptionStr) {
        super(exceptionStr);
    }
}
