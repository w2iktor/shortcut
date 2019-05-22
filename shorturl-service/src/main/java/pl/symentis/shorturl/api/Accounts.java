package pl.symentis.shorturl.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import pl.symentis.shorturl.domain.AccountBuilder;
import pl.symentis.shorturl.service.AccountsService;

import static pl.symentis.shorturl.domain.AccountBuilder.accountBuilder;

@RestController
@RequestMapping(path = "/api/accounts")
@Api
public class Accounts {
	
	private final AccountsService accountsService;

	@Autowired
	public Accounts(AccountsService accountsService) {
		super();
		this.accountsService = accountsService;
	}
	
	@PostMapping(
	    consumes = MediaType.APPLICATION_JSON_VALUE,
	    produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ApiOperation(value = "create account")
	public ResponseEntity<Void> createAccount(
			@ApiParam @RequestBody CreateAccountRequest createAccount) {
		Account account = accountBuilder()
			.withName(createAccount.getName())
			.withEmail(createAccount.getEmail())
			.withTaxnumber(createAccount.getTaxnumber())
			.withMaxShortcuts(createAccount.getMaxShortcuts())
			.build();

		return accountsService
			.createAccount(account)
			.map(createdAccount -> ResponseEntity.created(
				ControllerLinkBuilder
					.linkTo(Accounts.class)
					.slash(createdAccount.getName())
					.toUri()))
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

	@PutMapping(
	    path = "{accountid}",
	    consumes = MediaType.APPLICATION_JSON_VALUE
	)
	@ApiOperation(value="update account",code=200)
	public ResponseEntity<Void> updateAccount(
			@PathVariable("accountid") Integer id,
			@RequestBody UpdateAccountRequest updateAccount) {

		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping(
	    path = "{accountid}"
	)
	@ApiOperation(value="remove account",code=200)
	public ResponseEntity<Void> removeAccount(@PathVariable("accountid") String id) {
		if (accountsService.removeAccount(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping(
	    path = "{accountid}/inactive",
	    consumes = MediaType.APPLICATION_JSON_VALUE
	)
	@ApiOperation("change account status, you can activate or deactivate it")
	public ResponseEntity<Void> inactivateAccount(
			@PathVariable("accountid") Integer id,
			@RequestBody InactivateAccountRequest inactivateAccount) {
		return ResponseEntity.ok().build();
	}
	
}
