package logic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Schedule {
	private ArrayList<ScheduledActivity> myActivity;
	private static DAOSchedules daoSc;
	
	public Schedule() {
		myActivity = new ArrayList<ScheduledActivity>();
	}
	
	public void addActivityToSchedule(Activity a, LocalDateTime scheduledTime, LocalDateTime reminderTime, SuperUser usr) {
		ScheduledActivity s = new ScheduledActivity(a,scheduledTime,reminderTime);
		//Salva in persistenza l'attività schedulata:
		daoSc = DAOSchedules.getInstance();
		
		if(daoSc.addScheduletoJSON(this, usr)==false) {
			System.err.println("\n"+"Errore nell'aggiunta dell'attività nello schedule.");
			return;
		}
	}
	
	public ArrayList<ScheduledActivity> getScheduledActivities() {
		return this.myActivity;
	}

}
