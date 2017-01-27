package com.sorcerer.sorcery.iconpack.net.avos;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */

public class AvosRequest {
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    final String method;
    final String path;
    final Object body;

    public AvosRequest(String method, String path, Object body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

}
