package logic.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import logic.model.Activity;
import logic.model.DAOActivity;
import logic.model.DAOCoupon;
import logic.model.DAOSchedules;
import logic.model.DateBean;
import logic.model.Partner;
import logic.model.Schedule;
import logic.model.ScheduleBean;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

/*
 * @Author: sav :---------------------------------------------------------------------------------------------------------------------------------
 * associata la responsabilità di creare scheduled activity a schedule
 * plus l'utente è esonerato dalla gestione creando la classe schedule di mezzo
 * 
 * runtime:
 * 
 *1- controller di playActivity chiama controller di AddActivity to schedule
 * 
 * 	gli consegnerà: attività a cui si fa riferimento e bean contenente la data presa da una gui
 * 
 *2 -il controller tramite il metodo add Activity to schedule chiama il metodo di schedule che permetterà di:
 *
 *		2.1: creare un oggetto di tipo Scheduled Activity
 *		2.2: inserirlo nell'arrayList
 *
 *nota:
 *ha senso inserire uno Schedule piuttosto che delegare il compito all'utente?
 *
 *il motivo principale che mi viene in mente è che questo schedulo di attività va getito e mantenuto, nel caso non esistesse la classe Schedule
 *questo compito spetterebbe alla classe utente, andando ad appesantire il contenuto di quella classe e quindi con il rischio di renderla una classe factotum 
 * 
 * 
 * aggiunto timer in scheduled activity, ora quando una scheduled activity viene creata viene aggiunto un timer
 * 
 * se non viene specificato il timer di reminder viene impostato come timer la data di schedulo!
 * 
 * 
 * ----------------------------------------------------------------------------------------------------------------------------------------------
 * 
 * */

public class AddActivityToScheduleController {
	private User session;
	private ScheduleBean editBean;
	
	private Partner sessionPartner;
	private Object couponToRedeem;
	
	public AddActivityToScheduleController(User session, ScheduleBean editBean) {
		this.session = session;
		this.editBean = editBean;
	}
	
	public AddActivityToScheduleController(Partner partner,Object couponToRedeem) {
		//usare questo costruttore solo se si è partner e si vuole riscattare un coupon
		this.sessionPartner = partner;
		this.couponToRedeem = couponToRedeem;
	}
	
	//questo metodo andrebbe chiamato dal controller di playActivity
	public void addActivityToSchedule() throws ClassNotFoundException, SQLException  {
		//qua vanno eseguiti i vari controlli prima della modifica, se è il caso
		
		DAOSchedules daoSc = DAOSchedules.getInstance();
		Long idUser = session.getUserID();
		Long idActivity = editBean.getIdActivity();
		LocalDateTime scheduledDate = editBean.getScheduledDateTime();
		LocalDateTime reminderDate;
		
		//se non è disponibile il reminder Date dal bean, imposta come ora di reminder lo scheduledtime - 1h
		try{
			reminderDate = editBean.getReminderDateTime();
		}catch(DateTimeParseException e) {
			reminderDate = scheduledDate.minusHours(1);
		}
		
		daoSc.addActivityToSchedule(idUser,idActivity, scheduledDate, reminderDate);
		
		session.setSchedule(daoSc.getSchedule(session.getUserID()));
	}
	
	public void modifySchedule() throws ClassNotFoundException, SQLException   {
		//qua vanno eseguiti i vari controlli prima della modifica, se è il caso
		
		DAOSchedules daoSc = DAOSchedules.getInstance();
		Long idSchedule = editBean.getIdSchedule();
		LocalDateTime scheduledDate = editBean.getScheduledDateTime();
		LocalDateTime reminderDate;
		
		//se non è disponibile il reminder Date dal bean, imposta come ora di reminder lo scheduledtime - 1h
		try{
			reminderDate = editBean.getReminderDateTime();
		}catch(Exception e) {
			reminderDate = scheduledDate.minusHours(1);
		}
		
		daoSc.changeSchedule(idSchedule, scheduledDate, reminderDate);
		
		session.setSchedule(daoSc.getSchedule(session.getUserID()));
		
	}
	
	public void removeSchedule() throws ClassNotFoundException, SQLException {
		//qua vanno eseguiti i vari controlli prima della modifica, se è il caso
		
		DAOSchedules daoSc = DAOSchedules.getInstance();
		Long scheduleToRemove = editBean.getScheduleToRemove();
		Long idUser = session.getUserID();
		
		daoSc.removeActFromSchedule(scheduleToRemove,idUser);
		
		session.setSchedule(daoSc.getSchedule(session.getUserID()));
		
	}
	
	public void addCertifiedActivityToSchedule() throws ClassNotFoundException, SQLException {
		

		DAOSchedules daoSc = DAOSchedules.getInstance();
		Long idUser = session.getUserID();
		Long idActivity = editBean.getIdActivity();
		LocalDateTime scheduledDate = editBean.getScheduledDateTime();
		LocalDateTime reminderDate;
		int percentage = editBean.getSelectedCoupon();

		//se non è disponibile il reminder Date dal bean, imposta come ora di reminder lo scheduledtime - 1h
		try{
			reminderDate = editBean.getReminderDateTime();
		}catch(Exception e) {
			reminderDate = scheduledDate.minusHours(1);
		}
		
		daoSc.addCertifiedActivityToSchedule(idUser,idActivity, scheduledDate, reminderDate, percentage, scheduledDate.plusDays(1));
		
		session.setSchedule(daoSc.getSchedule(session.getUserID()));
		
		session.refershWallet();
	}
	
	public void redeemCoupon() throws ClassNotFoundException, SQLException {
		//usare questo metodo solo se si è partner e si vuole riscattare un coupon
		if(sessionPartner == null || couponToRedeem == null) throw new NullPointerException();
		int coupon;
		try {
			coupon = Integer.parseInt((String) couponToRedeem);
		}catch(NumberFormatException e) {
			throw e;
		}
		
		DAOCoupon dao = DAOCoupon.getInstance();
		Long myId = sessionPartner.getUserID();
		
		dao.redeemCoupon(coupon,myId);
		
	}

}
