package logic.controller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logic.model.ContinuosActivity;
import logic.model.DAOActivity;
import logic.model.DateBean;
import logic.model.ExpiringActivity;
import logic.model.Factory;
import logic.model.Log;
import logic.model.Partner;
import logic.model.PeriodicActivity;
import logic.model.Place;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

public class CreateActivityController {
	private SuperUser u;
	private DAOActivity daoAc;
	private CreateActivityBean bean;
	
	private static final  String ERRNOCREATE = "Attivita non creata.";
	private static final  String SHOWLOGID = "ID: {0}";
		
	CreateActivityController(SuperUser u, CreateActivityBean bean){
		this.u= u;
		this.bean = bean;
		daoAc = DAOActivity.getInstance();
	}
	
	public void createActivity(String n, Place p) {
		//spaghetti code here!!!
		Long id;
		
		SuperActivity newActivity;

		Log.getInstance().logger.info("Ciao da dentro createActivity");
		switch(bean.getType()) {
		case CONTINUA:
			{	
				if(u instanceof User) {
					
					//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso;
					//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
					//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
					
					newActivity=Factory.createNormalActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime());					
					id = daoAc.addActivityToJSON(p,newActivity,"no");
					if(id<0) {
						Log.getInstance().logger.warning(ERRNOCREATE);
						return;
					} else Log.getInstance().logger.log(Level.INFO,SHOWLOGID,id);
					
					newActivity.setId(id); 
				}
				else {
					newActivity=Factory.createCertifiedActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime() );
					id = daoAc.addActivityToJSON(p,newActivity,"yes");	
					if(id<0) {
						Log.getInstance().logger.warning(ERRNOCREATE);
						return;
					}else Log.getInstance().logger.log(Level.INFO,SHOWLOGID,id);
					newActivity.setId(id);
				}
			
		}
		break;
		case PERIODICA:
			{
				if(u instanceof User) {
					//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso;
					//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
					//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
					newActivity=Factory.createNormalActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime(), bean.getStartDate(), bean.getEndDate(), bean.getCadence());
					id = daoAc.addActivityToJSON(p,newActivity,"no");
					if(id<0) {
						Log.getInstance().logger.warning(ERRNOCREATE);
						return;
					}else Log.getInstance().logger.log(Level.INFO,SHOWLOGID,id);
					newActivity.setId(id);
				}
				else {
					newActivity=Factory.createCertifiedActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime(), bean.getStartDate(), bean.getEndDate(), bean.getCadence());
					id = daoAc.addActivityToJSON(p,newActivity,"yes");		
					if(id<0) {
						Log.getInstance().logger.warning(ERRNOCREATE);
						return;
					}else Log.getInstance().logger.log(Level.INFO,SHOWLOGID,id);
					newActivity.setId(id);
				}
			}
		break;
		case SCADENZA:
			{
				if(u instanceof User) {
					//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso;
					//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
					//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
					newActivity=Factory.createNormalActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime(), bean.getStartDate(), bean.getEndDate());
					id = daoAc.addActivityToJSON(p,newActivity,"no");
					if(id<0) {
						Log.getInstance().logger.warning(ERRNOCREATE);
						return;
					}else Log.getInstance().logger.log(Level.INFO,SHOWLOGID,id);
					newActivity.setId(id);
				}
				else { 
					newActivity=Factory.createCertifiedActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime(), bean.getStartDate(), bean.getEndDate());
					id = daoAc.addActivityToJSON(p,newActivity,"yes");	
					if(id<0) {
						Log.getInstance().logger.warning(ERRNOCREATE);
						return;
					}else Log.getInstance().logger.log(Level.INFO,SHOWLOGID,id);
					newActivity.setId(id);				
				}
			}
		break;
		}
		
	}
	
}
