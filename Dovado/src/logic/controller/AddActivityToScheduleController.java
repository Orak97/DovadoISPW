package logic.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import logic.model.Activity;
import logic.model.DAOActivity;
import logic.model.DAOSchedules;
import logic.model.DateBean;
import logic.model.Schedule;
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
 * TODO : pensare a un notification system!
 * ----------------------------------------------------------------------------------------------------------------------------------------------
 * 
 * */

public class AddActivityToScheduleController {
	private User session;
	private DateBean timestamp;
	
	private Schedule schedule = new Schedule();
	
	public AddActivityToScheduleController(Schedule schedule) {
		this.schedule = schedule;
	}
	
	public AddActivityToScheduleController(User usr, DateBean timestamp) {
		session = usr;
		this.timestamp= timestamp;
	}
	
	//questo metodo andrebbe chiamato dal controller di playActivity
	public void addActivityToSchedule(Long activity) throws Exception {
		Schedule s = session.getSchedule();
		DAOSchedules daoSc = DAOSchedules.getInstance();
		
		Activity a = DAOActivity.getInstance().getActivityById(activity);
		
		s.addActivityToSchedule(a, timestamp.getScheduledTime(), timestamp.getReminderTime());
		daoSc.addActivityToSchedule(session.getUserID(),activity,timestamp.getScheduledTime(),timestamp.getReminderTime());
	}
	
	//questo metodo va chiamato dal DAOSchedules!!
	public void addActivityToSchedule(Long activity, String scheduled_time, String reminder_time) throws Exception {		
		Activity a = DAOActivity.getInstance().getActivityById(activity);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime scheduledTime = LocalDateTime.parse(scheduled_time,dtf);
		LocalDateTime reminderTime = LocalDateTime.parse(reminder_time,dtf);
		
		schedule.addActivityToSchedule(a, scheduledTime, reminderTime);
	}
	
	

}
