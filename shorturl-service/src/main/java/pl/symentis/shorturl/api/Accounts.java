package pl.symentis.shorturl.api;

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
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.service.AccountDoesntExistException;
import pl.symentis.shorturl.service.AccountsService;
import pl.symentis.shorturl.service.ShortcutsRegistry;

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
	@ApiOperation(value="list all accounts",code=200,response=AccountResponse.class,responseContainer="List")
	public Response getAccounts(
			@QueryParam("offset") @DefaultValue("0") int offset,
			@QueryParam("limit") @DefaultValue("-1") int limit) {
		return Response.ok(accountsService.getAccounts()).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value="get account",
			produces=MediaType.APPLICATION_JSON)
	@ApiResponses({
		@ApiResponse(code=200,message="returns existing account",response=AccountResponse.class),
		@ApiResponse(code=404,message="account doesn't exists")
		
	})
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
	@ApiOperation(value="update account",code=200)
	public Response updateAccount(
			@PathParam("id") Integer id,
			UpdateAccountRequest updateAccount) {
		return Response.ok().build();
	}
	
	@DELETE
	@Path("{id}")
	@ApiOperation(value="remove account",code=200)
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
		Account account = accountsService.getAccount(accountid).orElseGet(() -> {throw new AccountDoesntExistException();});
		return new Shortcuts(account,urlShortcuts);
	}
	
}
