/**
 * 
 */
package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import logic.controller.ActivityType;

/**
 * @author sav
 *
 */
public class CreateActivityBean {
	
	private String activityName;
	private String openingDate;
	private String openingTime;
	private String endDate;
	private String closingTime;

	private ActivityType type;
	private Cadence cadence;
	
	public LocalDate getOpeningLocalDate() {
		return stringToLocalDate(openingDate);
	}
	
	public String getOpeningDate() {
		return openingDate;
	}
	
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}
	
	/*********************************************/
	
	public LocalTime getOpeningLocalTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(openingTime, formatter);
	}
	
	public String getOpeningTime() {
		return openingDate;
	}
	
	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}
	
	/*********************************************/
	
	public LocalDate getEndLocalDate() {
		return stringToLocalDate(endDate);
	}
	
	public String getEndDate() {
		return openingDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/*********************************************/

	public LocalTime getClosingLocalTime() {
		return stringToLocalTime(closingTime);
	}
	
	public String getClosingTime() {
		return openingDate;
	}
	
	public void setClosingTime(String closingTime) {
		this.closingTime = closingTime;
	}
	
	/*********************************************/

	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	public Cadence getCadence() {
		return cadence;
	}
	public void setCadence(Cadence cadence) {
		this.cadence = cadence;
	}
	
	private LocalDate stringToLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(date, formatter);
	}
	
	private LocalTime stringToLocalTime(String time){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(time, formatter);
	}
	
	public String prova() {
		return openingDate;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
