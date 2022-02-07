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
	
	public static Activity createNormalActivity(Long id,String nome, String description, Place p,LocalTime[] opCloseTime, LocalDate[] startEndDate) {
		newActivity = new NormalActivity(id,nome,description, p,opCloseTime[0],opCloseTime[1],startEndDate[0],startEndDate[1]);
		return newActivity;
	}
	
	public static Activity createNormalActivity(Long id,String nome, String description, Place p,LocalTime[] opCloseTime, LocalDate[] startEndDate, Cadence cadence ) {
		newActivity = new NormalActivity(id,nome,description, p,opCloseTime[0],opCloseTime[1],startEndDate[0],startEndDate[1],cadence);
		return newActivity;
	}
	
	public static Activity createCertifiedActivity(Long id,String nome, String description, Place p,LocalTime openingTime, LocalTime closingTime, Partner owner) {
		newActivity = new CertifiedActivity(id,nome,description, p,openingTime,closingTime, owner);
		return newActivity;
	}
	
	public static Activity createCertifiedActivity(Long id,String nome, String description, Place p,LocalTime[] opCloseTime, LocalDate[] startEndDate, Partner owner) {
		newActivity = new CertifiedActivity(id,nome,description, p,opCloseTime[0],opCloseTime[1],startEndDate[0],startEndDate[1], owner);
		return newActivity;
	}
	
	public static Activity createCertifiedActivity(Long id,String nome, String description, Place p,LocalTime[] opCloseTime, LocalDate[] startEndDate, Cadence cadence, Partner owner) {
		newActivity = new CertifiedActivity(id,nome,description, p,opCloseTime[0],opCloseTime[1],startEndDate[0],startEndDate[1],cadence, owner);
		return newActivity;
	}

}
