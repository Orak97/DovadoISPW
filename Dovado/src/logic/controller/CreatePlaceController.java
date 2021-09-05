package logic.controller;

import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Place;

public class CreatePlaceController {
	
	

	// Non serve forse l'id per la creazione di posti, ma basta salvarlo nel JSON per ottenerlo e ricaricarlo ogni volta che lo si mostra all'utente
	// per fare una tabella di posti e/o eventi da far vedere agli utenti.
	public Place createPlace(String name,String address,String city,String region, String civico) {
		return createPlace( name, address, city, region,  civico, null);
	}
	
	public Place createPlace(String name,String address,String city,String region, String civico,Partner owner){
		Place place = new Place(name, address,city,region,civico,owner);
		int id = this.addPlaceToDB(address, name, city, region, civico, owner); 
		if(id < 0)
			Log.getInstance().getLogger().info("\n\nPosto gia creato precedentemente\n\n");
		place.setId(id);
		return place;
	}

	
	public Place getPlaceFromJSON(String name, String city, String region) {
		Place placeFound;
		placeFound = DAOPlace.getInstance().findPlaceInJSON(name, city, region);
		if(placeFound!=null)
			return placeFound;
		return null;
	
	}
	
	private int addPlaceToDB(String address, String name, String city, String region,String civico, Partner owner) {
		int resultId = DAOPlace.getInstance().addPlaceToJSON(address, name, city, region, civico, owner);
		if(resultId>0)
			return resultId;
		return -1;
	
	}
	
}
