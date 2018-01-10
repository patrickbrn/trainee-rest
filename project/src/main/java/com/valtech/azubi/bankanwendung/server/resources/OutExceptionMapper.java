package com.valtech.azubi.bankanwendung.server.resources;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class OutExceptionMapper implements ExceptionMapper<OutException> {
    @Override
    public Response toResponse(OutException e) {
        return Response.status(e.getStatus())
                .entity(new ErrorMessage(e))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
