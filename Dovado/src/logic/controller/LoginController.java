package logic.controller;

import logic.model.DAOSuperUser;
import logic.model.SuperUser;


public class LoginController {
private DAOSuperUser dao;

	public LoginController(){
	dao=DAOSuperUser.getInstance();	
	}
	
	public SuperUser findUser(String email, String password) {
				
		return dao.findSuperUser(email, password, null);
		
	}
}
