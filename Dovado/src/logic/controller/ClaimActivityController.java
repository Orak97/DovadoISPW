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
		DAOActivity daoAc=DAOActivity.getInstance();

		//L'utente da qui potrà riprendere controllo e gestire un'attività
		//Trasformando l'attività in questione in certificata.
		
		CreateActivityBean cab = new CreateActivityBean();
		cab.setActivityDescription(a.getDescription());
		cab.setActivityName(a.getName());
		cab.setPlace(a.getPlace().getId().intValue());
		cab.setOwner(owner.getUserID().intValue());
		cab.setIdActivity(a.getId().intValue());
		
		cab.setInterestedCategories(a.getIntrestedCategories().getSetPreferences());
		if(a.getFrequency() instanceof ExpiringActivity) {
			cab.setType(ActivityType.SCADENZA);
			//SE L'ATTIVITA' HA UNA FREQUENZA DI TIPO CONTINUO, ALLORA
			//AVRA' ORARIO DI APERTURA, ORARIO DI CHIUSURA, DATA DI INIZIO
			//E DATA DI FINE; PERTANTO:
			cab.setOpeningTime(a.getFrequency().getFormattedOpeningTime());
			cab.setClosingTime(a.getFrequency().getFormattedClosingTime());
			cab.setEndDate(((ExpiringActivity)(a.getFrequency())).getFormattedEndDate());
			cab.setOpeningDate(((ExpiringActivity)(a.getFrequency())).getFormattedStartDate());
		}
		else if(a.getFrequency() instanceof ContinuosActivity) {
			cab.setType(ActivityType.CONTINUA);
			//SE L'ATTIVITA' HA UNA FREQUENZA DI TIPO CONTINUO, ALLORA
			//AVRA' SOLO ORARIO DI APERTURA E ORARIO DI CHIUSURA; PERTANTO:
			cab.setOpeningTime(a.getFrequency().getFormattedOpeningTime());
			cab.setClosingTime(a.getFrequency().getFormattedClosingTime());
			
		}
		else if(a.getFrequency() instanceof PeriodicActivity) {
			cab.setType(ActivityType.PERIODICA);
			//SE L'ATTIVITA' HA UNA FREQUENZA DI TIPO PERIODICO, ALLORA
			//AVRA' ORARIO DI APERTURA, ORARIO DI CHIUSURA, DATA DI INIZIO,
			//DATA DI FINE ED INFINE LA CADENZA; PERTANTO:
			cab.setOpeningTime(a.getFrequency().getFormattedOpeningTime());
			cab.setClosingTime(a.getFrequency().getFormattedClosingTime());
			cab.setEndDate(((PeriodicActivity)(a.getFrequency())).getFormattedEndDate());
			cab.setOpeningDate(((PeriodicActivity)(a.getFrequency())).getFormattedStartDate());
			cab.setCadence(((PeriodicActivity)(a.getFrequency())).getCadence());
		}
		else {
			Log.getInstance().getLogger().info("L'attività non ha un tipo!");
			return false;
		}
		return true;
	}
	
}
