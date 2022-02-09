package logic.controller;

import java.sql.SQLException;

import logic.model.DAOExplorer;
import logic.model.Log;
import logic.model.RegBean;
import logic.model.RegPartnerBean;
import logic.model.RegExpBean;


public class RegExplorerController extends RegController{
	//Decidere se i pattern mantenerli qui o sul bean
	private DAOExplorer dao;

	public RegExplorerController() {
		dao = DAOExplorer.getInstance();
	}
	
	
	
	public RegExpBean validateForm(RegBean bean) {
		return (RegExpBean) super.validateForm(bean);
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
