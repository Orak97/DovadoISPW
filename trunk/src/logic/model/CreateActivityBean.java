/**
 * 
 */
package logic.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import logic.controller.ActivityType;

/**
 * @author sav
 *
 */
public class CreateActivityBean {
	
	private String activityName;
	private String activityDescription;
	private int place;
	private String openingDate;
	private String endDate;
	private String openingTime;
	private String closingTime;

	private ActivityType type;
	private Cadence cadence;
	
	private boolean arte = false;
	private boolean cibo = false;
	private boolean musica = false;
	private boolean sport = false;
	private boolean social = false;
	private boolean natura = false;
	private boolean esplorazione = false;
	private boolean ricorrenze = false;
	private boolean moda = false;
	private boolean shopping = false;
	private boolean adrenalina = false;
	private boolean relax = false;
	private boolean istruzione= false;
	private boolean monumenti = false;
	
	private int idActivity;
	private int owner;
	
	public LocalDate getOpeningLocalDate() {
		return stringToLocalDate(openingDate);
	}
	
	public String getOpeningDate() {
		return openingDate;
	}
	
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}
	
	/*********************************************/
	
	public LocalTime getOpeningLocalTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(openingTime, formatter);
	}
	
	public String getOpeningTime() {
		return openingTime;
	}
	
	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}
	
	/*********************************************/
	
	public LocalDate getEndLocalDate() {
		return stringToLocalDate(endDate);
	}
	
	public String getEndDate() {
		return openingDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/*********************************************/

	public LocalTime getClosingLocalTime() {
		return stringToLocalTime(closingTime);
	}
	
	public String getClosingTime() {
		return closingTime;
	}
	
	public void setClosingTime(String closingTime) {
		this.closingTime = closingTime;
	}
	
	/*********************************************/

	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	public Cadence getCadence() {
		return cadence;
	}
	public void setCadence(Cadence cadence) {
		this.cadence = cadence;
	}
	
	private LocalDate stringToLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(date, formatter);
	}
	
	private LocalTime stringToLocalTime(String time){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(time, formatter);
	}
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}


	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public boolean isArte() {
		return arte;
	}

	public void setArte(boolean arte) {
		this.arte = arte;
	}

	public boolean isCibo() {
		return cibo;
	}

	public void setCibo(boolean cibo) {
		this.cibo = cibo;
	}

	public boolean isMusica() {
		return musica;
	}

	public void setMusica(boolean musica) {
		this.musica = musica;
	}

	public boolean isSport() {
		return sport;
	}

	public void setSport(boolean sport) {
		this.sport = sport;
	}

	public boolean isSocial() {
		return social;
	}

	public void setSocial(boolean social) {
		this.social = social;
	}

	public boolean isNatura() {
		return natura;
	}

	public void setNatura(boolean natura) {
		this.natura = natura;
	}

	public boolean isEsplorazione() {
		return esplorazione;
	}

	public void setEsplorazione(boolean esplorazione) {
		this.esplorazione = esplorazione;
	}

	public boolean isRicorrenze() {
		return ricorrenze;
	}

	public void setRicorrenze(boolean ricorrenze) {
		this.ricorrenze = ricorrenze;
	}

	public boolean isShopping() {
		return shopping;
	}

	public void setShopping(boolean shopping) {
		this.shopping = shopping;
	}

	public boolean isModa() {
		return moda;
	}

	public void setModa(boolean moda) {
		this.moda = moda;
	}

	public boolean isAdrenalina() {
		return adrenalina;
	}

	public void setAdrenalina(boolean adrenalina) {
		this.adrenalina = adrenalina;
	}

	public boolean isRelax() {
		return relax;
	}

	public void setRelax(boolean relax) {
		this.relax = relax;
	}

	public boolean isIstruzione() {
		return istruzione;
	}

	public void setIstruzione(boolean istruzione) {
		this.istruzione = istruzione;
	}

	public boolean isMonumenti() {
		return monumenti;
	}

	public void setMonumenti(boolean monumenti) {
		this.monumenti = monumenti;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

}
