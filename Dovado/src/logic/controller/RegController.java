package logic.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.model.Log;
import logic.model.RegBean;

public abstract class RegController {
	private Pattern patternPsw;
	private Pattern patternEmail;
	private Pattern patternUname;
	
	protected RegController() {
		patternEmail = Pattern.compile(".+@.+\\.[a-z]+");
		patternPsw = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[,.!?&]).{8,20})");
		patternUname = Pattern.compile("^[a-zA-Z0-9_-]{4,15}");
		
	}
	
	public RegBean validateForm(RegBean bean) {
		String error = null;
		// controllo la mail se e scritta in maniera corretta
		Matcher matchEmail = patternEmail.matcher(bean.getEmail());
		if (!matchEmail.matches()) {
				error = "Mail sintatticamente sbagliata";
				Log.getInstance().getLogger().info("Mail sintatticamente sbagliata");
				bean.setError(error);
				return bean;
		}
		
		//check sulla password		
		if (!bean.getPassword().equals(bean.getPassword2())) {
				error = "Le password non coincidono";
				Log.getInstance().getLogger().info(error);
				bean.setError(error);
				return bean;
		}		
		Matcher matchPsw = patternPsw.matcher(bean.getPassword2());
		if (!matchPsw.matches()) {
			error = "La password deve contenere almeno 8 cratteri e deve contenere numeri, lettere e un carattere tra:{',','.','&','!','?'} ";
			Log.getInstance().getLogger().info("password non conforme");
			bean.setError(error);
			return bean;
		}
		
		//check sullo username
		Matcher matchUname = patternUname.matcher(bean.getUsername());
		if (!matchUname.matches()) {
			
			error = "Lo username non deve contenere spazi e deve avere dai 4 ai 15 caratteri. Accetta numeri, lettere e {'_','-'}";
			Log.getInstance().getLogger().info(error);		
			bean.setError(error);
			return bean;
		}
		
		bean.setError(error);
		return bean;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
