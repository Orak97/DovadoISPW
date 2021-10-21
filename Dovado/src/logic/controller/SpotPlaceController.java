package logic.controller;

import logic.model.DAOPlace;
import logic.model.Partner;
import logic.model.Place;
import logic.model.SpotPlaceBean;
import logic.model.SuperUser;
import logic.view.Navbar;

public class SpotPlaceController {
	private SuperUser user;
	private SpotPlaceBean bean;
	private DAOPlace daoPl = DAOPlace.getInstance();
	
	public SpotPlaceController(SuperUser user, SpotPlaceBean bean){
		this.user = user;
		this.bean = bean;
	}
	
	public boolean spotPlace() {
		
		String placeName = bean.getPlaceName();
		String region = bean.getRegion();
		String address = bean.getAddress();
		String civico = bean.getStreetNumber();
		String city = bean.getCity();
		
		if(daoPl.findPlaceInJSON(placeName, city, region)!=null) {
			System.out.println("posto già creato da handlare l'exception");
			return false;
		}
		
		Partner owner = null;
		if(user instanceof Partner) owner = (Partner) user;
		
		if(daoPl.addPlaceToJSON(address, placeName, city, region, civico, owner)==-1) { 
			System.out.println("posto non creato");
			return false;
		}
		
		return true;
	
	}
	
	
	

}
