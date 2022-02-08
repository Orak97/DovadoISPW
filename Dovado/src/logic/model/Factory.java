package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;

import logic.controller.ActivityType;


public class Factory {
	private Factory(){}
	private static Activity newActivity;

	private static LocalTime[] openCloseTime = {null, null};
	private static LocalDate[] startEndDate = {null, null};
	
	private static void setLocalArray(CreateActivityBean bean) {
		if(bean.getType() != ActivityType.CONTINUA) {
			
			openCloseTime[0]= bean.getOpeningLocalTime();
			openCloseTime[1]= bean.getClosingLocalTime();
			startEndDate[0] = bean.getOpeningLocalDate();
			startEndDate[1] = bean.getEndLocalDate();
		}
	}
	
	public static Activity createNormalActivity(Long id,CreateActivityBean bean,Place p) {
		setLocalArray(bean);
		switch (bean.getType()) {
		case CONTINUA:
			newActivity = new NormalActivity(id, bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime());
			break;
		case PERIODICA:
			newActivity = new NormalActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, openCloseTime ,startEndDate,bean.getCadence());
			break;
		case SCADENZA:
			newActivity = new NormalActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, openCloseTime ,startEndDate);
			break;
		}
		return newActivity;
	}
	
	public static Activity createCertifiedActivity(Long id,CreateActivityBean bean,Place p, Partner owner) {
		setLocalArray(bean);
		switch (bean.getType()) {
		case CONTINUA:
			newActivity = new CertifiedActivity(id, bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(), owner);
			break;
		case PERIODICA:
			newActivity = new CertifiedActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, openCloseTime ,startEndDate,bean.getCadence(), owner);
			break;
		case SCADENZA:
			newActivity = new CertifiedActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, openCloseTime ,startEndDate, owner);
			break;
		}
		return newActivity;
	}

}
