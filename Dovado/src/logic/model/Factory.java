package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;


public class Factory {
	private Factory(){}
	private static Activity newActivity;
	private static LocalTime[] openCloseTime;
	private static LocalDate[] startEndDate;
	
	private static void setLocalArray(CreateActivityBean bean) {
		openCloseTime[0]= bean.getOpeningLocalTime();
		openCloseTime[1]= bean.getClosingLocalTime();
		startEndDate[0] = bean.getOpeningLocalDate();
		startEndDate[1] = bean.getEndLocalDate();
	}
	
	public static Activity createNormalActivity(Long id,CreateActivityBean bean,Place p) {
		
		switch (bean.getType()) {
		case CONTINUA:
			newActivity = new NormalActivity(id, bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime());
			break;
		case PERIODICA:
			setLocalArray(bean);
			newActivity = new NormalActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, openCloseTime ,startEndDate,bean.getCadence());
			break;
		case SCADENZA:
			setLocalArray(bean);
			newActivity = new NormalActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, openCloseTime ,startEndDate);
			break;
		}
		return newActivity;
	}
	
	public static Activity createCertifiedActivity(Long id,CreateActivityBean bean,Place p, Partner owner) {
		switch (bean.getType()) {
		case CONTINUA:
			newActivity = new CertifiedActivity(id, bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(), owner);
			break;
		case PERIODICA:
			setLocalArray(bean);
			newActivity = new CertifiedActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, openCloseTime ,startEndDate,bean.getCadence(), owner);
			break;
		case SCADENZA:
			setLocalArray(bean);
			newActivity = new CertifiedActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, openCloseTime ,startEndDate, owner);
			break;
		}
		return newActivity;
	}

}
