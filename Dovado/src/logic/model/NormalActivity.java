package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class NormalActivity extends SuperActivity implements Activity{

	public NormalActivity(Long id,String nome, String description, Place p, LocalTime openingTime, LocalTime closingTime) {
		super(id,nome, description, p,openingTime,closingTime);
	}
	
	public NormalActivity(Long id,String nome, String description, Place p, LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate) {
		super(id,nome, description, p,openingTime,closingTime,startDate,endDate);
	}
	
	public NormalActivity(Long id,String nome, String description, Place p, LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate,Cadence cadence) {
		super(id,nome, description, p,openingTime,closingTime,startDate,endDate, cadence);
	}

	@Override
	public void playActivity(User u) {
		Log.getInstance().getLogger().info("Yay hai fatto l'activity");
	}

}
