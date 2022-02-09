package logic.controller;

public class NoPartnerException extends Exception{
	private static final long serialVersionUID = 1L;

	public NoPartnerException() {
		super("Non possiedi i diritti necessari per fare effettuare questa operazione");
	}
	
	public NoPartnerException(String message) {
		super(message);
	}

}
