package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logic.controller.CreateActivityController;
import logic.controller.CreatePlaceController;

public class PlaceBean {
	
	private static PlaceBean INSTANCE;
	private CreatePlaceController createPlaceController;
	
	private PlaceBean() {
		//Lo implemento come singleton.
	}
	
	public static PlaceBean getInstance() {
		if(INSTANCE==null) {
			INSTANCE = new PlaceBean();
		}
		return INSTANCE;
	}
	
	public Place addPlace(String address, String name, String city, String region,String civico, Partner owner) {
		
		createPlaceController = new CreatePlaceController();
		if(owner!=null)
			return createPlaceController.createPlace(address, name, city, region, civico, owner);
		return createPlaceController.createPlace(address, name, city, region, civico);
	}
	
	public Place getPlace(String name, String city, String region) {
		createPlaceController = new CreatePlaceController();
		return createPlaceController.getPlaceFromDB(name, city, region);
	}

}
