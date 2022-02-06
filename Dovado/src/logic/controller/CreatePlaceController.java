package logic.controller;

import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Place;
import logic.model.PlaceBean;

public class CreatePlaceController {
	
	

	// Non serve forse l'id per la creazione di posti, ma basta salvarlo nel JSON per ottenerlo e ricaricarlo ogni volta che lo si mostra all'utente
	// per fare una tabella di posti e/o eventi da far vedere agli utenti.
	public Place createPlace(String name,String address,String city,String region, String civico) {
		return createPlace( name, address, city, region,  civico, null);
	}
	
	public Place createPlace(String name,String address,String city,String region, String civico,Partner owner){
		PlaceBean placeB = PlaceBean.getInstance();
		return placeB.addPlace(address, name, city, region, civico, owner);
	}

	
	public Place getPlaceFromDB(String name, String city, String region) {
		Place placeFound;
		PlaceBean placeB = PlaceBean.getInstance();
		placeFound = placeB.getPlace(name, city, region);
		
		if(placeFound!=null)
			return placeFound;
		return null;
	
	}
	
}
