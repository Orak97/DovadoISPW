package logic.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import logic.model.Activity;
import logic.model.DAOActivity;
import logic.model.DAOPreferences;
import logic.model.DAOSuperUser;
import logic.model.PreferenceBean;
import logic.model.Preferences;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

public class SetPreferencesController {
	
	DAOSuperUser daoSu;
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
		
		DAOPreferences.getInstance().updateUserPreferences(
				id,
				bean.isArte(),
				bean.isCibo(),
				bean.isMusica(),
				bean.isSport(),
				bean.isSocial(),
				bean.isNatura(),
				bean.isEsplorazione(),
				bean.isRicorrenze(),
				bean.isModa(),
				bean.isShopping(),
				bean.isAdrenalina(),
				bean.isRelax(),
				bean.isIstruzione(),
				bean.isMonumenti()
				);
		
		Preferences p = getPreferencesFromBean(bean);
		
		session.setPreferences(p);
	}
	
	public static Preferences getPreferencesFromBean(PreferenceBean bean) {
		return new Preferences(bean.isArte(),
				bean.isCibo(),
				bean.isMusica(),
				bean.isSport(),
				bean.isSocial(),
				bean.isNatura(),
				bean.isEsplorazione(),
				bean.isRicorrenze(),
				bean.isModa(),
				bean.isShopping(),
				bean.isAdrenalina(),
				bean.isRelax(),
				bean.isIstruzione(),
				bean.isMonumenti());
	}
	
	public void updatePreferencesUser() {
		//TODO;
	}
	
	public void updatePreferencesActivity() {
		//TODO;
	}
	
	
}
