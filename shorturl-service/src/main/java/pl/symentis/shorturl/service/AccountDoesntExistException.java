package pl.symentis.shorturl.service;

public class AccountDoesntExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6130815867549319898L;

	public AccountDoesntExistException(){
		super();
	}

	public AccountDoesntExistException(String message){
		super(message);
	}

}
