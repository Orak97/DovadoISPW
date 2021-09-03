package logic.controller;

import logic.model.DAOSuperUser;
import logic.model.Log;


public class RegisterController {
	//Decidere se i pattern mantenerli qui o sul bean
	
	private DAOSuperUser dao;
	
	public RegisterController() {
	}
	
	public boolean addUser(String email, String username, String psw) {
		
		dao = DAOSuperUser.getInstance();
	
		if (dao.findSuperUser(email) != null) {
			Log.getInstance().logger.info("L'utente esiste");
			return false;
		}
		else {
			Log.getInstance().logger.info("Non esiste l'utente");
			dao.addUserToJSON(email, username, 0, psw);
			return true;}	
	}

}
