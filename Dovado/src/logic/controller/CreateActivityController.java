package logic.controller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logic.model.ContinuosActivity;
import logic.model.DAOActivity;
import logic.model.DateBean;
import logic.model.ExpiringActivity;
import logic.model.Factory;
import logic.model.Partner;
import logic.model.PeriodicActivity;
import logic.model.Place;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

public class CreateActivityController {
	private SuperUser u;
	private DAOActivity daoAc;
	CreateActivityBean bean;
		
	CreateActivityController(SuperUser u, CreateActivityBean bean){
		this.u= u;
		this.bean = bean;
	}
	
	public void createActivity(String n, Place p) {
		//spaghetti code here!!!
		Long id;
		daoAc = DAOActivity.getInstance();
		SuperActivity newActivity;

		System.out.println("Ciao da dentro createActivity");
		switch(bean.getType()) {
		case continua:
			{	
				if(u instanceof User) {
					
					//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso;
					//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
					//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
					
					newActivity=Factory.createNormalActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime());					
					id = daoAc.addActivityToJSON(p,newActivity,"no");
					if(id<0) {
						System.out.println("Attivita non creata.\n");
						return;
					} else System.out.println("ID:\n"+id+"\n");
					
					newActivity.setId(id); 
				}
				else {
					newActivity=Factory.createCertifiedActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime() );
					id = daoAc.addActivityToJSON(p,newActivity,"yes");	
					if(id<0) {
						System.out.println("Attivita non creata.\n");
						return;
					}else System.out.println("ID:\n"+id+"\n");
					newActivity.setId(id);
				}
			
		}
		break;
		case periodica:
			{
				if(u instanceof User) {
					//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso;
					//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
					//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
					newActivity=Factory.createNormalActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime(), bean.getStartDate(), bean.getEndDate(), bean.getCadence());
					id = daoAc.addActivityToJSON(p,newActivity,"no");
					if(id<0) {
						System.out.println("Attivita non creata.\n");
						return;
					}else System.out.println("ID:\n"+id+"\n");
					newActivity.setId(id);
				}
				else {
					newActivity=Factory.createCertifiedActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime(), bean.getStartDate(), bean.getEndDate(), bean.getCadence());
					id = daoAc.addActivityToJSON(p,newActivity,"yes");		
					if(id<0) {
						System.out.println("Attivita non creata.\n");
						return;
					}else System.out.println("ID:\n"+id+"\n");
					newActivity.setId(id);
				}
			}
		break;
		case scadenza:
			{
				if(u instanceof User) {
					//A seconda di che tipo di utente e abbiamo un metodo di aggiunta attivita al file JSON diverso;
					//se l'utente e normale l'attivita non e certificata, mentre se lo e avremo un'attivita certificata.
					//e importante nella ricostruzione delle attivita ricavate dalla persistenza.
					newActivity=Factory.createNormalActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime(), bean.getStartDate(), bean.getEndDate());
					id = daoAc.addActivityToJSON(p,newActivity,"no");
					if(id<0) {
						System.out.println("Attivita non creata.\n");
						return;
					}else System.out.println("ID:\n"+id+"\n");
					newActivity.setId(id);
				}
				else { 
					newActivity=Factory.createCertifiedActivity(n, u, p, bean.getOpeningTime(), bean.getClosingTime(), bean.getStartDate(), bean.getEndDate());
					id = daoAc.addActivityToJSON(p,newActivity,"yes");	
					if(id<0) {
						System.out.println("Attivita non creata.\n");
						return;
					}else System.out.println("ID:\n"+id+"\n");
					newActivity.setId(id);				
				}
			}
		break;
		}
		
	}
	
}
