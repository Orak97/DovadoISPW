package logic.controller;

import logic.model.Partner;
import logic.model.User;

public class CreateUserController {
	
	DAOSuperUser daoSU;
	
	// NON SO SE E' UTILE CREARE UN INTERO CONTROLLER PER L'AGGIUNTA DI UTENTI, MAGARI IL DAO VIENE CHIAMATO DALLA ENTITY? NON SEMBRA CORRETTO, MA OVVIAMENTE E' MENO CODICE.
	
	public  Partner createPartner(String username){
		daoSU = DAOSuperUser.getInstance();
		daoSU.addUserToJSON(username, 1);
		return new Partner(username);
	}
	
	public User createUser(String username) {
		daoSU = DAOSuperUser.getInstance();
		daoSU.addUserToJSON(username, 0);
		return new User(username);
	}
	
}