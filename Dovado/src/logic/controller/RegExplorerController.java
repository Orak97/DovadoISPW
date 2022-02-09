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
	
	
	
	public RegExpBean validateForm(RegBean bean) throws AuthException {
		return (RegExpBean) super.validateForm(bean);
	}



	public RegExpBean addExplorer(RegExpBean bean) throws ClassNotFoundException, AuthException {
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
			Log.getInstance().getLogger().warning("Errore di codice: "+ e.getErrorCode() + " e mesaggio: " + e.getMessage());	
			throw new AuthException(e);
		}
	}

}
