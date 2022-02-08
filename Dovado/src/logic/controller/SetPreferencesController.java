package logic.controller;

import java.sql.SQLException;

import logic.model.DAOActivity;
import logic.model.DAOPreferences;
import logic.model.PreferenceBean;
import logic.model.Preferences;

import logic.model.User;

public class SetPreferencesController {
	
	DAOActivity daoAc;
	DAOPreferences daoPr;
	
	User session;
	PreferenceBean bean;
	
	public SetPreferencesController(User user, PreferenceBean bean){
		this.session = user;
		this.bean = bean;
	}
	
	public void updatePreferences() throws ClassNotFoundException, SQLException{
		Long id = session.getUserID();
		Preferences p = getPreferencesFromBean(bean);
		DAOPreferences.getInstance().updateUserPreferences(id, p.getSetPreferences());
		
		
		
		session.setPreferences(p);
	}
	
	public static Preferences getPreferencesFromBean(PreferenceBean bean) {
		return new Preferences(bean);
	}
	
}
