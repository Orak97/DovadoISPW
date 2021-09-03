package logic.controller;

import logic.model.DAOPlace;
import logic.model.Partner;
import logic.model.Place;

public class ClaimAPlaceController {
	
	
	
	public boolean claimPlaceOwnership(Partner owner, Place p) {
		DAOPlace daoPl=DAOPlace.getInstance();
		if(p.getOwner()==(null)) {
			p.setOwner(owner);
			if(daoPl.updatePlaceJSON(p))
				return true;
			else
			{
				System.err.println("\n"+"Errore incontrato nel processo di aggiunta proprietario.\n");
				return false;
			}
		}
		return false;
	}
	
}
