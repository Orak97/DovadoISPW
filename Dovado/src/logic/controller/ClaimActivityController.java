package logic.controller;

import logic.model.DAOActivity;
import logic.model.Log;
import logic.model.Partner;
import logic.model.SuperActivity;
import logic.model.User;

public class ClaimActivityController {
	
	private DAOActivity daoAc;
	
	public boolean claimActivityOwnership(Partner owner, SuperActivity a) {
		daoAc=DAOActivity.getInstance();
		if(a.getCreator() instanceof User) {
			a.setCreator(owner);
			if(daoAc.updateActivityJSON(a)==true)
				return true;
			else
			{
				Log.getInstance().logger.warning("Errore incontrato nel processo di aggiunta proprietario.\n");
				return false;
			}
		} else {
			Log.getInstance().logger.info("Esiste gia un partner proprietario dell'attivita.\n");
			return false;	
		}
	}
	
}
