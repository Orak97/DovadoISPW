package logic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Schedule {
	private ArrayList<ScheduledActivity> myActivity;
	private static DAOSchedules daoSc;
	
	public Schedule() {
		myActivity = new ArrayList<ScheduledActivity>();
	}
	
	public void addActivityToSchedule(SuperActivity a, LocalDateTime scheduledTime, LocalDateTime reminderTime, SuperUser usr) {
		ScheduledActivity s = new ScheduledActivity(a,scheduledTime,reminderTime);
		//Salva in persistenza l'attività schedulata:
		daoSc = DAOSchedules.getInstance();

		myActivity.add(s);
		if(daoSc.addScheduletoJSON(this, usr)==false) {
			Log.getInstance().getLogger().warning("Errore nell'aggiunta dell'attività nello schedule.");
			return;
		}
		System.out.println("Aggiunta l'attività nello schedule.");
		
	}
	
	public ArrayList<ScheduledActivity> getScheduledActivities() {
		return this.myActivity;
	} 

	public ArrayList<ScheduledActivity> setScheduledActivities(ArrayList<ScheduledActivity> scheduledActs) {
		return this.myActivity = scheduledActs;
	}
} 
