package logic.controller;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.model.DAOExplorer;
import logic.model.Log;
import logic.model.RegBean;
import logic.model.RegExpBean;


public class RegExplorerController {
	//Decidere se i pattern mantenerli qui o sul bean
	private DAOExplorer dao;
	private Pattern patternPsw;
	private Pattern patternEmail;
	private Pattern patternUname;

	public RegExplorerController() {
		dao = DAOExplorer.getInstance();
		patternEmail = Pattern.compile(".+@.+\\.[a-z]+");
		patternPsw = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[,.!?&]).{8,20})");
		patternUname = Pattern.compile("^[a-zA-Z0-9_-]{4,15}");
	}
	
	
	public RegExpBean validateForm(RegExpBean bean) {
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
	
	public RegExpBean addExplorer(RegExpBean bean) throws ClassNotFoundException  {
		boolean[] pref = {bean.getArte(), 
				bean.getCibo(), 
				bean.getMusica(),
				bean.getSport(), 
				bean.getSocial(), 
				bean.getNatura(),
				bean.getEsplorazione(),
				bean.getRicorrenze(),
				bean.getModa(),
				bean.getShopping(),
				bean.getAdrenalina(),
				bean.getMonumenti(),
				bean.getRelax(),
				bean.getIstruzione()};
		try{
			dao.registerExplorer(bean.getUsername(), bean.getEmail(), bean.getPassword(), pref);
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
			else if (e.getErrorCode() == 1048) {
				String excep = e.getMessage().split(" ")[2];	
				
				if (excep.equals("'username'")) {
					bean.setError("L'utente non può essere NULL");
				} else {
					bean.setError("Il campo " + excep + " non può essere vuoto");
				}
			}
			Log.getInstance().getLogger().warning("Errore di codice: "+ e.getErrorCode() + " e mesaggio: " + e.getMessage());	
			Log.getInstance().getLogger().info(bean.getError());	

			return bean;}
	}

}
