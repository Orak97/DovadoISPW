package logic.controller;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.model.DAOPartner;
import logic.model.Log;
import logic.model.RegPartnerBean;

public class RegPartnerController extends RegController{
	//Decidere se i pattern mantenerli qui o sul bean
	private DAOPartner dao;
	private Pattern patternPIVA;

	
	public RegPartnerController() {
		dao = DAOPartner.getInstance();
		patternPIVA = Pattern.compile("[0-9]{11}");
	}
	
	public RegPartnerBean validateForm(RegPartnerBean bean) throws AuthException{
		String error = null;
		bean =(RegPartnerBean) super.validateForm(bean);
		
		//check sulla P.IVA
		Matcher matchPIVA = patternPIVA.matcher(bean.getpIVA());
		if (!matchPIVA.matches()) {
			error = "La partita IVA deve essere composta da esattamente 11 cifre";
			throw new AuthException(error);
		}
		
		//check sul nome della compagnia 
		if(bean.getCompName().length() > 80 || bean.getCompName().length() < 4) {
			error = "La compagnia ha tra i 4 e gli 80 caratteri";
			throw new AuthException(error);
		}
		bean.setError(error);
		return bean;
	}
	
	public RegPartnerBean addPartner(RegPartnerBean bean) throws ClassNotFoundException, AuthException {
		
		try{
			dao.registerPartner(bean.getUsername(), bean.getEmail(), bean.getPassword(), String.valueOf(bean.getpIVA()), bean.getCompName());
			return bean;			
		}catch(SQLException e){
			//Questo vuol dire che abbiamo un duplicato nel BB
				Log.getInstance().getLogger().warning("Errore di codice: "+ e.getErrorCode() + " e mesaggio: " + e.getMessage());
				throw new AuthException(e);
		}
	}

}
