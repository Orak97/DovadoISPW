package logic.controller;

import logic.model.DAOExplorer;
import logic.model.DAOSchedules;
import logic.model.DAOSuperUser;
import logic.model.SuperUser;
import logic.model.User;


public class LoginController {
private DAOSuperUser dao;

	public LoginController(){
	dao=DAOSuperUser.getInstance();	
	}
	
	public SuperUser findUser(String email, String password) {
				
		return dao.findSuperUser(email, password, null);
		
	}
	
	public User loginExplorer(String email, String password) throws Exception {
		User u = DAOExplorer.getInstance().login(email, password);
		u.setSchedule(DAOSchedules.getInstance().getSchedule(u.getUserID()));
		return u;
	}
}
