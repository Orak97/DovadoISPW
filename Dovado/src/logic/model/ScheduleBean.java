/**
 * 
 */
package logic.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author sav
 *
 */
public class ScheduleBean {
	
	private String scheduledDate;
	private String scheduledTime;
	private String reminderDate;
	private String reminderTime;
	private Long idActivity;
	
	public String getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	public String getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	public String getReminderDate() {
		return reminderDate;
	}
	public void setReminderDate(String reminderDate) {
		this.reminderDate = reminderDate;
	}
	public String getReminderTime() {
		return reminderTime;
	}
	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
	}
	
	public LocalDateTime getScheduledDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.parse(scheduledDate+' '+scheduledTime, formatter);

	}
	public Long getIdActivity() {
		return idActivity;
	}
	public void setIdActivity(Long idActivity) {
		this.idActivity = idActivity;
	}

}
