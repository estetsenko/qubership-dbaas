package org.qubership.cloud.dbaas.controller.error;

import org.qubership.cloud.dbaas.dto.Source;
import org.qubership.cloud.dbaas.exceptions.ValidationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.CustomLog;

import java.util.Optional;

import static org.qubership.cloud.dbaas.controller.error.Utils.WARNING_MESSAGE;
import static org.qubership.cloud.dbaas.controller.error.Utils.buildResponse;
import static org.qubership.cloud.dbaas.controller.error.Utils.tmfResponseBuilder;

@CustomLog
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ValidationException e) {
        log.warn(WARNING_MESSAGE, e.getClass().getSimpleName(), uriInfo.getPath(), e.getMessage());
        Response.Status status = e.getStatus() != null ? Response.Status.fromStatusCode(e.getStatus()) : Response.Status.BAD_REQUEST;
        return buildResponse(status,
                () -> tmfResponseBuilder(e, status)
                        .source(Optional.ofNullable(e.getSource())
                                .map(s -> Source.builder()
                                        .pointer(s.getPointer())
                                        .parameter(s.getParameter())
                                        .pathVariable(s.getPathVariable())
                                        .build()).orElse(null))
                        .build());
    }
}
