package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;


public class Factory {
	private Factory(){}
	
	private static Activity newActivity;
	
	public static Activity createNormalActivity(Long id,CreateActivityBean bean,Place p) {
		switch (bean.getType()) {
		case CONTINUA:
			newActivity = new NormalActivity(id, bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime());
			break;
		case PERIODICA:
			newActivity = new NormalActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(),bean.getOpeningLocalDate(), bean.getEndLocalDate(),bean.getCadence());
			break;
		case SCADENZA:
			newActivity = new NormalActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(),bean.getOpeningLocalDate(), bean.getEndLocalDate());
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
			newActivity = new CertifiedActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(),bean.getOpeningLocalDate(), bean.getEndLocalDate(),bean.getCadence(), owner);
			break;
		case SCADENZA:
			newActivity = new CertifiedActivity(id,bean.getActivityName(), bean.getActivityDescription(), p, bean.getOpeningLocalTime(), bean.getClosingLocalTime(),bean.getOpeningLocalDate(), bean.getEndLocalDate(), owner);
			break;
		}
		return newActivity;
	}

}
