package logic.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CertifiedActivity extends SuperActivity implements Activity {
	
	private Partner owner;
	private String site;
	private String price;

	public CertifiedActivity(Long id,String nome, String description, Place p, LocalTime openingTime, LocalTime closingTime, Partner owner) {
		super(id,nome, description, p,openingTime,closingTime);
		this.owner = owner;
	}

	public CertifiedActivity(Long id,String nome, String description, Place p, LocalTime[] openCloseTime, LocalDate[] startEndDate, Partner owner) {
		super(id,nome, description, p,openCloseTime,startEndDate);
		this.owner = owner;
	}

	public CertifiedActivity(Long id,String nome, String description, Place p, LocalTime[] openCloseTime, LocalDate[] startEndDate,Cadence cadence, Partner owner) {
		super(id,nome, description, p,openCloseTime,startEndDate, cadence);
		this.owner = owner;
	}

	@Override
	public void playActivity(User u) {
		Log.getInstance().getLogger().info("Yay hai fatto l'activity certificata e ti becchi la ricompensa");
		u.getReward(100);
	}

	public void setOwner(Partner owner) {
		this.owner = owner;
	}
	
	public Partner getOwner() {
		return this.owner;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getPrice() {
		return price;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	
	
}
