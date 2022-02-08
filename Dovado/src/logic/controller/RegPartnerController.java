package logic.controller;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.model.DAOPartner;
import logic.model.Log;
import logic.model.RegBean;

public class RegPartnerController {
	//Decidere se i pattern mantenerli qui o sul bean
	private DAOPartner dao;
	private Pattern patternPsw;
	private Pattern patternEmail;
	private Pattern patternUname;
	private Pattern patternPIVA;

	
	public RegPartnerController() {
		dao = DAOPartner.getInstance();
		patternEmail = Pattern.compile(".+@.+\\.[a-z]+");
		patternPsw = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[,.!?&]).{8,20})");
		patternUname = Pattern.compile("^[a-zA-Z0-9_-]{4,15}");
		patternPIVA = Pattern.compile("[0-9]{11}");
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
		
		//check sulla P.IVA
		Matcher matchPIVA = patternPIVA.matcher(bean.getpIVA());
		if (!matchPIVA.matches()) {
			error = "La partita IVA deve essere composta da esattamente 11 cifre";
			bean.setError(error);
			return bean;
		}
		
		//check sul nome della compagnia 
		if(bean.getCompName().length() > 80 || bean.getCompName().length() < 4) {
			error = "La compagnia ha tra i 4 e gli 80 caratteri";
			bean.setError(error);
			return bean;
		}
		bean.setError(error);
		return bean;
	}
	
	public RegBean addPartner(RegBean bean) throws ClassNotFoundException {
		
		try{
			dao.registerPartner(bean.getUsername(), bean.getEmail(), bean.getPassword(), String.valueOf(bean.getpIVA()), bean.getCompName());
			return bean;			
		}catch(SQLException e){
			//Questo vuol dire che abbiamo un duplicato nel BB
			if(e.getErrorCode() == 1062) {
				// Qui isoliamo e individuiamo la specifica eccezione
				String excep = e.getMessage().split(" ")[6];
				
				switch (excep) {
				case "'email_UNIQUE'":
					bean.setError("Esiste già un utente con questa mail");
					break;
				case "'username_UNIQUE'":
					bean.setError("Esiste già un utente con questo Username");
					break;

				default:
					bean.setError("Errore duplicazione di: "+excep);
					break;
				}
			}
			Log.getInstance().getLogger().warning("Errore di codice: "+ e.getErrorCode() + " e mesaggio: " + e.getMessage());
			Log.getInstance().getLogger().info(bean.getError());	

			return bean;}
	}

}
