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
	
	public void addActivityToSchedule(Activity a, LocalDateTime scheduledTime, LocalDateTime reminderTime) {
		ScheduledActivity s = new ScheduledActivity(a,scheduledTime,reminderTime);
		//Salva in persistenza l'attività schedulata:

		myActivity.add(s);
		
		
	}
	
	public List<ScheduledActivity> getScheduledActivities() {
		return this.myActivity;
	}

	public void setScheduledActivities(List<ScheduledActivity> scheduledActs) {
		this.myActivity = (ArrayList<ScheduledActivity>) scheduledActs;
	}
}
