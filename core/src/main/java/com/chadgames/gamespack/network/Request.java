package com.chadgames.gamespack.network;

public class Request {
    public RequestType requestType;
    public Object data;
    public Request() {}
    public Request(RequestType requestType) {
        this.requestType = requestType;
    }
    public Request(RequestType requestType, Object data) {
        this.requestType = requestType;
        this.data = data;
    }
}
