package logic.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class PeriodicActivity extends FrequencyOfRepeat{


	private LocalDate startDate;
	private LocalDate endDate;
	private Cadence cadence;
	
	public PeriodicActivity(LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate, Cadence cadence) {
		super(openingTime, closingTime);
		
		this.startDate=startDate;
		this.endDate=endDate;
		
		this.cadence = cadence;
	}

	public Cadence getCadence() {
		return this.cadence;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public String getFormattedStartDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return startDate.format(formatter);
	}
	
	public String getFormattedEndDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return endDate.format(formatter);
	}
	public LocalDate getEndDate() {
		return this.endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public boolean checkPlayability(LocalDateTime timestamp) {
		
		if(!this.isOnTime(timestamp)) return false;
		
		LocalDate myDate = timestamp.toLocalDate();
		
		return checkDate(myDate);
	}

	@Override
	public boolean checkDate(LocalDate date) {
		switch(cadence) {
		case WEEKLY:
			{	
				return checkWeeklyDate(date);
				
			}
		case MONTHLY:
			{	
				return checkMonthlyDate(date);
			}
		case ANNUALLY: 
			{	
				
				return checkAnnnualyDate(date);				
			} 
		default:
				throw new NullPointerException("La cadenza non può essere null");		
		}
	}
	private boolean checkWeeklyDate(LocalDate date) {
		// reference for DayOfWeek = https://docs.oracle.com/javase/8/docs/api/java/time/DayOfWeek.html#getValue--
		DayOfWeek myDay = date.getDayOfWeek();
		
		DayOfWeek startingDay = startDate.getDayOfWeek();
		DayOfWeek endDay = endDate.getDayOfWeek();
		
		if( startingDay.getValue() < endDay.getValue() )
			//l'evento si verifica durante la settimana
			return ( myDay.getValue() >= startingDay.getValue() && myDay.getValue() <= endDay.getValue());
		if( startingDay.getValue() > endDay.getValue() )
			//l'evento si verifica a cavallo di due settimane
			return ( myDay.getValue() >= startingDay.getValue() || myDay.getValue() <= endDay.getValue());
		
		return true;
	}
	
	private boolean checkMonthlyDate(LocalDate date) {
		//getDayOfMonth restutisce un int che va da 1 a 31 in base al giorno!
		int myDate = date.getDayOfMonth();
		
		int startDay = startDate.getDayOfMonth();
		int endDay = endDate.getDayOfMonth();
		
		if(startDay < endDay)
			//l'evento si verifica all'interno di uno stesso mese
			return (myDate >= startDay && myDate <= endDay);
		if(startDay > endDay)
			//l'evento si verifica a cavallo di due mesi diversi
			return (myDate >= startDay || myDate <= endDay);
			//l'evento si verifica sempre
		return true;
	}	
	private boolean checkAnnnualyDate(LocalDate date) {
		int myMonth = date.getMonthValue();
		int startMonth = startDate.getMonthValue();
		int endMonth = endDate.getMonthValue();
		int myDate = date.getDayOfMonth();
		int startDay = startDate.getDayOfMonth();
		int endDay = endDate.getDayOfMonth();
		
		if(startMonth == endMonth) {
			if(myMonth != startMonth) return false;	
			return checkDays(startDay,endDay,myDate);
		}
		
		if(startMonth < endMonth) {
			//se mese inizio < mese fine sono nello stesso anno, controllo che il mese in cui voglio fare l'attività sia mese inizio<= mio mese<= mese fine
			if(!(myMonth >= startMonth && myMonth <= endMonth)) return false;
		} else {
			if(!(myMonth >= startMonth || myMonth <= endMonth)) return false;
		}
	
		if(myMonth == startMonth) {
			return (startDay <= myDate);
		}
			
		if(myMonth == endMonth) {
			return (endDay >= myDate);
		}	
		
		return true;
		
	}	
	private boolean checkDays(int startDay, int endDay, int myDate) {
		if(startDay < endDay)
			//l'evento si verifica all'interno di uno stesso mese
			return (myDate >= startDay && myDate <= endDay);
		if(startDay > endDay)
			//l'evento si verifica a cavallo di due mesi diversi
			return (myDate >= startDay || myDate <= endDay);
		
		return true;
	}

	@Override
	protected String getStringInfo() {
		String infos = "Ogni ";
		switch(cadence) {
		case WEEKLY:
			infos+= "settimana da ";
			infos+= getFormatDayOfWeek(startDate);
			infos+= " a ";
			infos+= getFormatDayOfWeek(endDate);
			infos+= ".";
			break;
		case MONTHLY:
			infos+= "mese dal "+startDate.getDayOfMonth()+" al "+endDate.getDayOfMonth()+".";
			
			break;
		case ANNUALLY:
			infos +="anno dal "+startDate.getDayOfMonth()+"/"+startDate.getMonthValue()+" al "+endDate.getDayOfMonth()+"/"+endDate.getMonthValue()+".";
			break;
		}
		return infos;
	}
	
	protected String getFormatDayOfWeek(LocalDate myDate) {
		return myDate.getDayOfWeek().getDisplayName(TextStyle.FULL,Locale.ITALIAN);
	}
	
}
