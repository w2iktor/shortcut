package pl.symentis.shorturl.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import pl.symentis.shorturl.domain.AccountDoesntExistException;

public class AccountDoesntExistExceptionMapper implements ExceptionMapper<AccountDoesntExistException> {

	@Override
	public Response toResponse(AccountDoesntExistException exception) {
		return Response.status(Status.NOT_FOUND).build();
	}

}
