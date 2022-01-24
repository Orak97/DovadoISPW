package logic.controller;

import logic.model.DAOSuperUser;
import logic.model.Log;


public class RegExplorerController {
	//Decidere se i pattern mantenerli qui o sul bean
	DAOSuperUser dao;

	public RegExplorerController() {
		dao = DAOSuperUser.getInstance();
	}
	
	public boolean addUser(String email, String username, String psw, int partner) {
		
		if (dao.findSuperUserByEmail(email) != null) {
			Log.getInstance().getLogger().info("L'utente esiste");
			return false;
		}
		else {
			Log.getInstance().getLogger().info("Non esiste l'utente");
			dao.addUserToJSON(email, username, partner, psw);
			return true;}	
	}

}
