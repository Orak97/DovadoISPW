package logic.controller;

import logic.model.DAOPlace;
import logic.model.Partner;
import logic.model.Place;
import logic.model.SpotPlaceBean;
import logic.model.SuperUser;
import logic.view.Navbar;

public class SpotPlaceController {
	/************************************************************
	 * Tabella delle responsabilità:
	 * **********************************************************
	 * 	Q:	Chi crea il posto? 
	 * 	A:	il dao perché è l'information expert
	 * 	
	 * 	Q:	Chi si preoccupa se il posto esiste sulla mappa?
	 * 	A:	questa classe, perché è il controller
	 * 
	 * 	Q: Chi si preoccupa di controllare che il posto non sia un duplicato?
	 * 	A: Il Trigger Before insert nel database relazionale, perché è la via più ottimale
	 * 
	 * Se hai altre repsonabilità da verificare aggiungile qui
	 * ********************************************************** 
	 * */
	
	private SpotPlaceBean bean;
	private DAOPlace daoPl = DAOPlace.getInstance();
	
	public SpotPlaceController(SpotPlaceBean bean){
		this.bean = bean;
	}
	
	public boolean spotPlace() {
		
		String placeName = bean.getPlaceName();
		String region = bean.getRegion();
		String address = bean.getAddress();
		String civico = bean.getStreetNumber();
		String city = bean.getCity();
		String cap = bean.getCap();
		double[] coord = {bean.getLatitude(),bean.getLongitude()};
		
				
		try{
			daoPl.spotPlace(address, placeName, city, region, civico, cap,coord);
		}
		catch(Exception e) {
			System.out.println("posto non creato");
			return false;
		}
		
		return true;
	
	}

}
