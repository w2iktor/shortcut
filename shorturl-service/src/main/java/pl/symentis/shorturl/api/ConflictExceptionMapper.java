package pl.symentis.shorturl.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import pl.symentis.shorturl.service.ConflictException;

public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {

	@Override
	public Response toResponse(ConflictException exception) {
		return Response.status(Status.CONFLICT).build();
	}

}
