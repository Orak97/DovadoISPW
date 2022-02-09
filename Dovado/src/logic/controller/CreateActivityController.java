package logic.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import logic.model.Activity;
import logic.model.Channel;
import logic.model.DAOActivity;
import logic.model.DAOChannel;
import logic.model.DAOPartner;
import logic.model.DAOPlace;
import logic.model.Factory;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Place;
import logic.model.Preferences;
import logic.model.SuperUser;
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
	
	//attributi usati per creare le classi e oggetti
	private DAOActivity daoAc;
	private CreateActivityBean bean;
	
	//attributi comuni ad attività certificate e normali
	private Preferences intrestedCategories;
	private Place place;
	private Long idActivity;
	private Channel channel;
	
	//attributi solo per le certificate
	private Partner owner;
		
	public CreateActivityController(CreateActivityBean bean){
		this.bean = bean;
		daoAc = DAOActivity.getInstance();

	}
	
	public CreateActivityController(CreateActivityBean bean, SuperUser session){
		this.bean = bean;
		if(session instanceof Partner) {
			this.owner = (Partner) session;
			this.bean.setOwner(this.owner.getUserID().intValue());
		}
		daoAc = DAOActivity.getInstance();

	}
	
	public Activity createActivity() throws SQLException, ClassNotFoundException {
		Activity newActivity;
		/******************************************************************************************
		 * IMPORTANTISSIMO:																		
		 ******************************************************************************************
		 * metodo usato per creare classi di tipo Activity:
		 * 
		 * usage:
		 * 
		 * 1)riempire il bean di CreateActivityBean
		 *
		 * 2)creare un istanza di createActivity e passare il bean come parametro nel costruttore
		 * 
		 * 3)chiamare createActivity() e fa TUTTO lui.
		 *******************************************************************************************/
				
		//prima creo le preferenze, poi il posto e poi l'id in long siccome usiamo quelli per l'id for some reason
		this.fillPreferences();
		this.retrievePlace();
		this.parseId();
		this.createChannel();
		
		
		if(!this.isCertified()) {
			newActivity=Factory.createNormalActivity(idActivity, bean, place); 
		}
		else {
			
			newActivity=Factory.createCertifiedActivity(idActivity, bean, place, owner); 
		}
		
		newActivity.setIntrestedCategories(intrestedCategories);
		
		newActivity.setChannel(channel);
		
		return newActivity;
		
	}

	
	private void createChannel() throws SQLException, ClassNotFoundException {
		DAOChannel daoc = DAOChannel.getInstance();
		channel = daoc.getChannel(idActivity);
		
	}
	
	private void parseId() {
		// Metodo per riempire l'id che a quanto pare vuole il long
		idActivity = Long.valueOf(bean.getIdActivity());
		
	}
	
	private void fillPreferences() {
		// Usare questo metodo per riempire una classe Preferences (che in questo caso rispecchia le categorie interessate), prima di metterla dentro all'attività
		intrestedCategories = new Preferences(bean);
		
	}
	
	private void retrievePlace() throws ClassNotFoundException, SQLException {
		// Usare questo metodo per ottenere una classe Place prima di creare un oggetto attività
		DAOPlace dao = DAOPlace.getInstance();
		place = dao.getPlace(bean.getPlace());
	}
	
	private void retrievePartner() throws SQLException, ClassNotFoundException{
		//NOTA: questo deve venir chiamato solo da dentro isCertified!!!
		// Usare questo metodo per ottenere una classe Partner prima di creare un oggetto attività certificata
		DAOPartner dao = DAOPartner.getInstance();
		owner = dao.getPartnerInfo(bean.getOwner());
	}
	
	private boolean isCertified() throws SQLException, ClassNotFoundException {
		/*
		 * Metodo che controlla se un'attività è certificata, e lo è se ha un partner, che in caso reperisce subito dopo.
		 * */
		
		if(bean.getOwner() != 0) {
			this.retrievePartner();
			return true;
		}else {
			return false;
		}
	}
	
	public void saveActivity() throws SQLException, ClassNotFoundException , IllegalArgumentException{
		/*
		 * Medoto per chiamare il DAO, quello che controllo è che qua il tipo sia continua, periodica o a scadenza
		 * 
		 * */
		if(bean.getClosingLocalTime().compareTo(bean.getOpeningLocalTime()) < 0) throw new IllegalArgumentException("L\'orario di apertura non può essere prima dell\'orario di chiusura");

		daoAc.createNormalActivity(bean);

	}
	
}
