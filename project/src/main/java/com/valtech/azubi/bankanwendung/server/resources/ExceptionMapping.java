package com.valtech.azubi.bankanwendung.server.resources;

import javax.ws.rs.core.Response;
import java.util.HashMap;

public class ExceptionMapping {

    static HashMap<Exception, Response> respExceptMap = new HashMap<>();


    public static void put(Response r, Exception e) {
        respExceptMap.put(e, r);

    }

    public static Response getResponse(Exception e) {

        return respExceptMap.get(e);
    }
}