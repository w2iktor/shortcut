package pl.symentis.shorturl.api;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pl.symentis.shorturl.domain.AccountsService;
import pl.symentis.shorturl.domain.ShortcutsRegistry;

@Path("accounts")
@Component
@Api
public class Accounts {
	
	private final ShortcutsRegistry urlShortcuts;
	private final AccountsService accountsService;
	
	@Autowired
	public Accounts(ShortcutsRegistry urlShortcuts,AccountsService accountsService) {
		super();
		this.urlShortcuts = urlShortcuts;
		this.accountsService = accountsService;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("create account")
	public Response createAccount(
			@Context UriInfo uriInfo,
			CreateAccountRequest createAccount) {
		return accountsService
		.createAccount(createAccount)
		.map(account -> Response.created(uriInfo.getRequestUriBuilder().path(Accounts.class, "getAccount").build(account.getName())))
		.orElseGet(() -> Response.status(Status.CONFLICT))
		.build();
	}
	
	@GET
	@ApiOperation("list all accounts")
	public Response getAccounts(
			@QueryParam("offset") @DefaultValue("0") int offset,
			@QueryParam("limit") @DefaultValue("-1") int limit) {
		return Response.ok().build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("get account")
	public Response getAccount(@PathParam("id") String id) {
		return accountsService
		.getAccount(id)
		.map(AccountResponse::valueOf)
		.map(Response::ok)
		.orElseGet(() -> Response.status(Status.NOT_FOUND))
		.build();
	}

	@PUT
	@Path("{id}")
	@ApiOperation("update account")
	public Response updateAccount(
			@PathParam("id") Integer id,
			UpdateAccountRequest updateAccount) {
		return Response.ok().build();
	}
	
	@DELETE
	@Path("{id}")
	@ApiOperation("remove account")
	public Response removeAccount(@PathParam("id") Integer id) {
		return Response.ok().build();
	}
	
	@PUT
	@Path("{id}/inactive")
	@ApiOperation("change account status, you can activate or deactivate it")
	public Response inactivateAccount(
			@PathParam("id") Integer id,
			InactivateAccountRequest inactivateAccount) {
		return Response.ok().build();
	}
	
	// example of sub-resource locator
	@Path("{accountid}/shortcuts")
	public Shortcuts shortcuts(@PathParam("accountid") String accountid) {
		return new Shortcuts(accountid,urlShortcuts);
	}
	
}
