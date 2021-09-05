package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class NormalActivity extends SuperActivity implements Activity{

	public NormalActivity(String nome, SuperUser c, Place p, LocalTime openingTime, LocalTime closingTime) {
		super(nome, c, p,openingTime,closingTime);
	}
	
	public NormalActivity(String nome, SuperUser c, Place p, LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate) {
		super(nome, c, p,openingTime,closingTime,startDate,endDate);
	}
	
	public NormalActivity(String nome, SuperUser c, Place p, LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate,Cadence cadence) {
		super(nome, c, p,openingTime,closingTime,startDate,endDate, cadence);
	}

	@Override
	public void playActivity(User u) {
		Log.getInstance().logger.info("Yay hai fatto l'activity");
	}

}
