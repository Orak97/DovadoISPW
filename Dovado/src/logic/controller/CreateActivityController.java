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
import logic.model.CreateActivityBean;

public class CreateActivityController {
	
	/*
	 * ---------------------------------------------------------
	 * Tabella delle responsabilità
	 * ---------------------------------------------------------
	 * 
	 * Q: chi si preoccupa di controllare che il luogo esista?
	 * A: IL DMBS quando fa il controllo sulla foreign key 
	 * 
	 * */
	
	private SuperUser u;
	private DAOActivity daoAc;
	private CreateActivityBean bean;
	
	private static final  String ERRNOCREATE = "Attivita non creata.";
	private static final  String SHOWLOGID = "ID: {0}";
		
	public CreateActivityController(SuperUser u, CreateActivityBean bean){
		this.u= u;
		this.bean = bean;
		daoAc = DAOActivity.getInstance();
	}
	
	public CreateActivityController(CreateActivityBean bean){
		this.bean = bean;
		daoAc = DAOActivity.getInstance();

	}
	
	public void createActivity(String n, Place p) {
		//spaghetti code here!!!
				
		Log.getInstance().getLogger().info("Ciao da dentro createActivity");
		switch(bean.getType()) {
		case CONTINUA:
			{	
				createContinueActivity(n, p);
			}
		break;
		case PERIODICA:
			{
				createPeriodicActivity(n, p);
			}
		break;
		case SCADENZA:
			{
				createExpiredActivity(n, p);
			}
		break;
		}
		
	}
	private void createContinueActivity(String name, Place p) {
		Long id;
		SuperActivity newActivity;
		
		if(u instanceof User) {
			
			//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso
			//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
			//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
			
			newActivity=Factory.createNormalActivity(name, u, p, bean.getOpeningLocalTime(), bean.getClosingLocalTime());					
			id = daoAc.addActivityToJSON(p,newActivity,"no");
			if(id<0) {
				Log.getInstance().getLogger().warning(ERRNOCREATE);
				return;
			} else Log.getInstance().getLogger().log(Level.INFO,SHOWLOGID,id);
			
			newActivity.setId(id); 
		}
		else {
			newActivity=Factory.createCertifiedActivity(name, u, p, bean.getOpeningLocalTime(), bean.getClosingLocalTime() );
			id = daoAc.addActivityToJSON(p,newActivity,"yes");	
			if(id<0) {
				Log.getInstance().getLogger().warning(ERRNOCREATE);
				return;
			}else Log.getInstance().getLogger().log(Level.INFO,SHOWLOGID,id);
			newActivity.setId(id);
		}
	}
	
	private void createPeriodicActivity(String name, Place p) {
		Long id;
		SuperActivity newActivity;
		
		if(u instanceof User) {
			
			//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso
			//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
			//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
			
			newActivity=Factory.createNormalActivity(name, u, p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(), bean.getOpeningLocalDate(), bean.getEndLocalDate(), bean.getCadence());				
			//ERRid = daoAc.addActivityToJSON(p,newActivity,"no");
			if(id<0) {
				Log.getInstance().getLogger().warning(ERRNOCREATE);
				return;
			} else Log.getInstance().getLogger().log(Level.INFO,SHOWLOGID,id);
			
			newActivity.setId(id); 
		}
		else {
			newActivity=Factory.createCertifiedActivity(name, u, p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(), bean.getOpeningLocalDate(), bean.getEndLocalDate(), bean.getCadence());
			id = daoAc.addActivityToJSON(p,newActivity,"yes");	
			if(id<0) {
				Log.getInstance().getLogger().warning(ERRNOCREATE);
				return;
			}else Log.getInstance().getLogger().log(Level.INFO,SHOWLOGID,id);
			newActivity.setId(id);
		}
	}
	
	private void createExpiredActivity(String name, Place p) {
		Long id;
		SuperActivity newActivity;
		
		if(u instanceof User) {
			
			//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso
			//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
			//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
			
			newActivity=Factory.createNormalActivity(name, u, p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(), bean.getOpeningLocalDate(), bean.getEndLocalDate());				
			id = daoAc.addActivityToJSON(p,newActivity,"no");
			if(id<0) {
				Log.getInstance().getLogger().warning(ERRNOCREATE);
				return;
			} else Log.getInstance().getLogger().log(Level.INFO,SHOWLOGID,id);
			
			newActivity.setId(id); 
		}
		else {
			newActivity=Factory.createCertifiedActivity(name, u, p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(), bean.getOpeningLocalDate(), bean.getEndLocalDate());
			id = daoAc.addActivityToJSON(p,newActivity,"yes");	
			if(id<0) {
				Log.getInstance().getLogger().warning(ERRNOCREATE);
				return;
			}else Log.getInstance().getLogger().log(Level.INFO,SHOWLOGID,id);
			newActivity.setId(id);
		}
	}
	
	public void saveActivity() throws Exception {
		/*
		 * Medoto per chiamare il DAO, quello che controllo è che qua il tipo sia continua, periodica o a scadenza
		 * 
		 * */
		
		switch(bean.getType()) {
			case CONTINUA:  
				daoAc.createNormalActivity(bean.getActivityName(),bean.getActivityDescription(),null,null,bean.getPlace(),null,bean.isArte(),bean.isCibo(),bean.isMusica(),bean.isSport(),bean.isSocial(),bean.isNatura(),bean.isEsplorazione(),bean.isRicorrenze(),bean.isModa(),bean.isShopping(),bean.isAdrenalina(),bean.isMonumenti(),bean.isRelax(),bean.isIstruzione(),bean.getType().name(),bean.getOpeningLocalTime().toString(), bean.getClosingLocalTime().toString(), null, null,null);
			break;
			case PERIODICA:
				daoAc.createNormalActivity(bean.getActivityName(),bean.getActivityDescription(),null,null,bean.getPlace(),null,bean.isArte(),bean.isCibo(),bean.isMusica(),bean.isSport(),bean.isSocial(),bean.isNatura(),bean.isEsplorazione(),bean.isRicorrenze(),bean.isModa(),bean.isShopping(),bean.isAdrenalina(),bean.isMonumenti(),bean.isRelax(),bean.isIstruzione(),bean.getType().name(),bean.getOpeningLocalTime().toString(), bean.getClosingLocalTime().toString(), bean.getOpeningLocalDate().toString(), bean.getEndLocalDate().toString(),bean.getCadence().name());
			break;
			case SCADENZA:
				daoAc.createNormalActivity(bean.getActivityName(),bean.getActivityDescription(),null,null,bean.getPlace(),null,bean.isArte(),bean.isCibo(),bean.isMusica(),bean.isSport(),bean.isSocial(),bean.isNatura(),bean.isEsplorazione(),bean.isRicorrenze(),bean.isModa(),bean.isShopping(),bean.isAdrenalina(),bean.isMonumenti(),bean.isRelax(),bean.isIstruzione(),bean.getType().name(),bean.getOpeningLocalTime().toString(), bean.getClosingLocalTime().toString(), bean.getOpeningLocalDate().toString(), bean.getEndLocalDate().toString(),null);
			break;
			default:
				//TODO: handle the error;
			break;
			
		}
	}
	
}
