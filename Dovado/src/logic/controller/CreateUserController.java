package logic.controller;

import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.Partner;
import logic.model.User;

public class CreateUserController {
	
	DAOSuperUser daoSU;
	// NON SO SE E' UTILE CREARE UN INTERO CONTROLLER PER L'AGGIUNTA DI UTENTI, MAGARI IL DAO VIENE CHIAMATO DALLA ENTITY? NON SEMBRA CORRETTO, MA OVVIAMENTE E' MENO CODICE.
	// Per il momento non vedo motivo per cuifar ritornare la classe partner/user quindi ho 
	public  boolean createPartner(String email, String username,String psw){
		daoSU = DAOSuperUser.getInstance();
		return daoSU.addUserToJSON(email, username, 1,psw);
	}
	
	public boolean createUser(String email, String username, String psw) {
		daoSU = DAOSuperUser.getInstance();
		return daoSU.addUserToJSON(email, username, 0,psw);
		
	}
	public void printTest(String n) {
		Log.getInstance().getLogger().info(n);
	}
	
}
