package logic.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ExpiringActivity extends FrequencyOfRepeat {
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	public ExpiringActivity(LocalTime openingTime, LocalTime closingTime,LocalDate startDate, LocalDate endDate) {
		super(openingTime, closingTime);
		
		this.startDate=startDate;
		this.endDate=endDate;
		
	}

	
	@Override
	public boolean checkPlayability(LocalDateTime timestamp) {
		
		if(!this.isOnTime(timestamp)) return false;
		
		LocalDate myDate = timestamp.toLocalDate();
	
		return checkDate(myDate);

	} 

	public LocalDate getStartDate() {
		return this.startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public LocalDate getEndDate() {
		return this.endDate;
	}
	
	public String getFormattedStartDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		return startDate.format(formatter);
	}
	
	public String getFormattedEndDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		return endDate.format(formatter);
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public boolean checkDate(LocalDate date) {
		// TODO Auto-generated method stub
		return (startDate.isBefore(date) && endDate.isAfter(date)) || (startDate.isEqual(date) || endDate.isEqual(date));
	}


	@Override
	protected String getStringInfo() {			
		return "Dal "+this.getFormattedStartDate()+" al "+this.getFormattedEndDate()+", dalle ore "+this.getFormattedOpeningTime()+" alle ore "+this.getFormattedClosingTime()+".";
	}
	
	
}
