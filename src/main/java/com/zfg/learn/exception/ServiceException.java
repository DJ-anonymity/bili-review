package com.zfg.learn.exception;

/**
 * 业务层抛出
 */
public class ServiceException extends RuntimeException {
    private Integer status;

    public ServiceException() {
    }

    public ServiceException(Integer status) {
    }


    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
