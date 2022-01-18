package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class CertifiedActivity extends SuperActivity implements Activity {
	
	private Partner owner;

	public CertifiedActivity(Long id,String nome, String description, Place p, LocalTime openingTime, LocalTime closingTime, Partner owner) {
		super(id,nome, description, p,openingTime,closingTime);
		this.owner = owner;
	}

	public CertifiedActivity(Long id,String nome, String description, Place p, LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate, Partner owner) {
		super(id,nome, description, p,openingTime,closingTime,startDate,endDate);
		this.owner = owner;
	}

	public CertifiedActivity(Long id,String nome, String description, Place p, LocalTime openingTime, LocalTime closingTime, LocalDate startDate, LocalDate endDate,Cadence cadence, Partner owner) {
		super(id,nome, description, p,openingTime,closingTime,startDate,endDate, cadence);
		this.owner = owner;
	}

	@Override
	public void playActivity(User u) {
		Log.getInstance().getLogger().info("Yay hai fatto l'activity certificata e ti becchi la ricompensa");
		u.getReward(100);
	}


}
