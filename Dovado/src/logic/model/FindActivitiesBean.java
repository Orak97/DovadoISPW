package logic.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FindActivitiesBean {
	private String zone;
	private String date;
	
	private String keywords;

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public LocalDate getLocalDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(date,formatter);	
	}
	

}
