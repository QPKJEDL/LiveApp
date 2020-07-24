package com.qingpeng.mz.okhttp;

import java.io.IOException;

public class ResultException extends IOException {
    private int status;
    private String info;

    public ResultException(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}