package logic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
	private ArrayList<ScheduledActivity> myActivity;
	
	public Schedule() {
		myActivity = new ArrayList<>();
	}
	
	public void addActivityToSchedule(Long idScheduledActivity,Activity a, LocalDateTime scheduledTime, LocalDateTime reminderTime, Coupon c) {
		ScheduledActivity s = new ScheduledActivity(idScheduledActivity,a,scheduledTime,reminderTime,c);
		//Salva in persistenza l'attività schedulata:

		myActivity.add(s);
	}
	
	public void addActivityToSchedule(Long idScheduledActivity,Activity a, LocalDateTime scheduledTime, LocalDateTime reminderTime) {
		ScheduledActivity s = new ScheduledActivity(idScheduledActivity,a,scheduledTime,reminderTime);
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
