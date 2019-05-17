package pl.symentis.shorturl.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.service.AccountsService;
import pl.symentis.shorturl.service.ShortcutStatsService;
import pl.symentis.shorturl.service.ShortcutsRegistry;

@RestController
@RequestMapping(path = "/api/accounts")
@Api
public class Accounts {
	
	private final ShortcutsRegistry urlShortcuts;
	private final AccountsService accountsService;
	private final ShortcutStatsService shortcutStatsService;

	@Autowired
	public Accounts(ShortcutsRegistry urlShortcuts, AccountsService accountsService, ShortcutStatsService shortcutStatsService) {
		super();
		this.urlShortcuts = urlShortcuts;
		this.accountsService = accountsService;
		this.shortcutStatsService = shortcutStatsService;
	}
	
	@PostMapping(
	    consumes = MediaType.APPLICATION_JSON_VALUE,
	    produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ApiOperation(value = "create account")
	public ResponseEntity<Void> createAccount(
			@ApiParam @RequestBody CreateAccountRequest createAccount) {
	  
		return accountsService
		.createAccount(createAccount)
		.map(account -> ResponseEntity.created(ControllerLinkBuilder.linkTo(Accounts.class).slash(account.getName()).toUri()))
		.orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT))
		.build();
	}
	
	@GetMapping(
	    produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="list all accounts",code=200,response=GetAccountResponse.class,responseContainer="List")
	public ResponseEntity<List<Account>> getAccounts(
			@RequestParam(name="offset", defaultValue = "0") int offset,
			@RequestParam(name="limit",defaultValue="-1") int limit) {
		return ResponseEntity.ok(accountsService.getAccounts());
	}

	@GetMapping(
	    path="{id}",
	    produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
			value="get account",
			produces= MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses({
		@ApiResponse(code=200,message="returns existing account",response=GetAccountResponse.class),
		@ApiResponse(code=404,message="account doesn't exists")
		
	})
	public ResponseEntity<GetAccountResponse> getAccount(@PathVariable String id) {
		return accountsService
		.getAccount(id)
		.map(GetAccountResponse::valueOf)
		.map(ResponseEntity::ok)
		.orElseGet(() -> ResponseEntity.notFound().build());
	}
//
//	@PUT
//	@Path("{id}")
//	@ApiOperation(value="update account",code=200)
//	public Response updateAccount(
//			@PathParam("id") Integer id,
//			UpdateAccountRequest updateAccount) {
//		return Response.ok().build();
//	}
//	
//	@DELETE
//	@Path("{id}")
//	@ApiOperation(value="remove account",code=200)
//	public Response removeAccount(@PathParam("id") Integer id) {
//		return Response.ok().build();
//	}
//	
//	@PUT
//	@Path("{id}/inactive")
//	@ApiOperation("change account status, you can activate or deactivate it")
//	public Response inactivateAccount(
//			@PathParam("id") Integer id,
//			InactivateAccountRequest inactivateAccount) {
//		return Response.ok().build();
//	}
//	
//	// example of sub-resource locator
////	@Path("{accountid}/shortcuts")
////	public Shortcuts shortcuts(@PathParam("accountid") String accountid) {
////		Account account = accountsService.getAccount(accountid).orElseGet(() -> {throw new AccountDoesntExistException();});
////		return new Shortcuts(account,urlShortcuts, shortcutStatsService);
////	}
	
}
