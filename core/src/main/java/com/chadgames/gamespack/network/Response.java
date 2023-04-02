package com.chadgames.gamespack.network;

public class Response {
    public boolean success;
    public ResponseType responseType;
    public Object data;

    public Response() {}
    public Response(boolean success, ResponseType responseType, Object data) {
        this.success = success;
        this.responseType = responseType;
        this.data = data;
    }
}
