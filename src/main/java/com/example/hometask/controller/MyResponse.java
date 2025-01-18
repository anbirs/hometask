package com.example.hometask.controller;

public class MyResponse {
    String error;
    Object data;

    public MyResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public MyResponse setError(String error) {
        this.error = error;
        return this;
    }
}
