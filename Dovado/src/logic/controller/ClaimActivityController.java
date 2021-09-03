package logic.controller;

import logic.model.DAOActivity;
import logic.model.Partner;
import logic.model.SuperActivity;
import logic.model.User;

public class ClaimActivityController {


	public boolean claimActivityOwnership(Partner owner, SuperActivity a) {
		DAOActivity daoAc=DAOActivity.getInstance();
		if(a.getCreator() instanceof User) {
			a.setCreator(owner);
			if(daoAc.updateActivityJSON(a))
				return true;
			else
			{
				System.err.println("\n"+"Errore incontrato nel processo di aggiunta proprietario.\n");
				return false;
			}
		} else {
			System.err.println("\n"+"Esiste gia un partner proprietario dell'attivita.\n");
			return false;
		}
	}

}
