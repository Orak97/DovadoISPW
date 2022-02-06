package logic.controller;

import java.util.logging.Logger;

import logic.model.CertifiedActivity;
import logic.model.ContinuosActivity;
import logic.model.CreateActivityBean;
import logic.model.DAOActivity;
import logic.model.ExpiringActivity;
import logic.model.Log;
import logic.model.Partner;
import logic.model.PeriodicActivity;
import logic.model.SuperActivity;
import logic.model.User;

public class ClaimActivityController {


	public boolean claimActivityOwnership(Partner owner, SuperActivity a) {

		//L'utente da qui potrà riprendere controllo e gestire un'attività
		//Trasformando l'attività in questione in certificata.
		
		CreateActivityBean cab = new CreateActivityBean();
		cab.fillBean(cab, a, owner);
		UpdateCertActController updCertContrl = new UpdateCertActController(cab);
		try {
			updCertContrl.updateActivity();
		} catch (Exception e) {
			Log.getInstance().getLogger().info("Errore durante l'accesso al database.");
			return false;
		}
		return true;
	}
	
}
