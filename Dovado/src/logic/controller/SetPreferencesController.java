package logic.controller;

import java.util.ArrayList;

import logic.model.DAOActivity;
import logic.model.DAOPreferences;
import logic.model.DAOSuperUser;
import logic.model.SuperActivity;
import logic.model.SuperUser;

public class SetPreferencesController {
	
	DAOSuperUser daoSu;
	DAOActivity daoAc;
	DAOPreferences daoPr;
	
	public boolean updatePreferencesUser(SuperUser su, String name) {
		if(name == null) return false;
		daoSu = DAOSuperUser.getInstance();
		daoPr = DAOPreferences.getInstance();
		//Prendo l'arraylist delle preferenze e lo preparo.
		ArrayList<String> preferences = su.getPreferences();
		
		//Tramite il DAO delle preferenze vado a cercare il suo id usando il nome.
		//Se il nome non esiste il metodo chiamato restituisce l'id dell'appena aggiunta
		//preferenza.
		if(preferences.contains(name.toUpperCase()))
			return true;
		preferences.add(name.toUpperCase());
		su.setPreferences(preferences);
		
		return daoSu.updateUserPreferences(su);
	}
	
	public boolean updatePreferencesActivity(SuperActivity sua, String name) {
		if(name == null) return false;
		daoAc = DAOActivity.getInstance();
		daoPr = DAOPreferences.getInstance();
		//Prendo l'arraylist delle preferenze e lo preparo.
		ArrayList<String> preferences = sua.getPreferences();
		
		//Tramite il DAO delle preferenze vado a cercare il suo id usando il nome.
		//Se il nome non esiste il metodo chiamato restituisce l'id dell'appena aggiunta
		//preferenza.
		if(preferences.contains(name.toUpperCase()))
			return true;
		preferences.add(name.toUpperCase());
		sua.setPreferences(preferences);
		
		return daoAc.updateActivityPreferences(sua);
	}
	
	
}
