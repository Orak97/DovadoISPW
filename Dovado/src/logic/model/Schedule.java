package logic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
	private ArrayList<ScheduledActivity> myActivity;
	private static DAOSchedules daoSc = DAOSchedules.getInstance();
	
	public Schedule() {
		myActivity = new ArrayList<>();
	}
	
	public void addActivityToSchedule(SuperActivity a, LocalDateTime scheduledTime, LocalDateTime reminderTime, SuperUser usr) {
		ScheduledActivity s = new ScheduledActivity(a,scheduledTime,reminderTime);
		//Salva in persistenza l'attività schedulata:

		myActivity.add(s);
		if(!daoSc.addScheduletoJSON(this, usr)) {
			Log.getInstance().getLogger().warning("Errore nell'aggiunta dell'attività nello schedule.");
			return;
		}
		Log.getInstance().getLogger().info("Aggiunta l'attività nello schedule.");
		
	}
	
	public List<ScheduledActivity> getScheduledActivities() {
		return this.myActivity;
	}

	public void setScheduledActivities(List<ScheduledActivity> scheduledActs) {
		this.myActivity = (ArrayList<ScheduledActivity>) scheduledActs;
	}
}
