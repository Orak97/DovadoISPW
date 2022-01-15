package logic.controller;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ShowMapController {
	
	//L'unica istanza attiva di ShowMapController.
	private static ShowMapController INSTANCE;
	
	private ShowMapController() {
		//Privato perche e un singleton.
	}
	
	public static ShowMapController getInstance() {
		if(INSTANCE==null) {
			INSTANCE = new ShowMapController();
		}
		return INSTANCE;
	}
	
	public JSONArray getPlaces() {

		JSONParser parser = new JSONParser();
		try {
		
		JSONObject places = (JSONObject) parser.parse(new FileReader("places.json"));
				
		return (JSONArray) places.get("places");
		
		} 
		catch(Exception e) {e.printStackTrace();}
		
		return null;
		
	}
	
	
}
