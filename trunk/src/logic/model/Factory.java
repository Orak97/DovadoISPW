package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;


public class Factory {
	private Factory(){}
	
	private static Activity newActivity;
	
	public static Activity createNormalActivity(Long id,String nome, String description, Place p,LocalTime openingTime, LocalTime closingTime) {
		newActivity = new NormalActivity(id,nome,description, p,openingTime,closingTime);
		return newActivity;
	}
	
	public static Activity createNormalActivity(Long id,String nome, String description, Place p,LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate) {
		newActivity = new NormalActivity(id,nome,description, p,openingTime,closingTime,startDate,endDate);
		return newActivity;
	}
	
	public static Activity createNormalActivity(Long id,String nome, String description, Place p,LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate, Cadence cadence ) {
		newActivity = new NormalActivity(id,nome,description, p,openingTime,closingTime,startDate,endDate,cadence);
		return newActivity;
	}
	
	public static Activity createCertifiedActivity(Long id,String nome, String description, Place p,LocalTime openingTime, LocalTime closingTime, Partner owner) {
		newActivity = new CertifiedActivity(id,nome,description, p,openingTime,closingTime, owner);
		return newActivity;
	}
	
	public static Activity createCertifiedActivity(Long id,String nome, String description, Place p,LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate, Partner owner) {
		newActivity = new CertifiedActivity(id,nome,description, p,openingTime,closingTime,startDate,endDate, owner);
		return newActivity;
	}
	
	public static Activity createCertifiedActivity(Long id,String nome, String description, Place p,LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate, Cadence cadence, Partner owner) {
		newActivity = new CertifiedActivity(id,nome,description, p,openingTime,closingTime,startDate,endDate,cadence, owner);
		return newActivity;
	}

}
