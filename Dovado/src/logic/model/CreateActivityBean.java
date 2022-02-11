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
public class CreateActivityBean extends PreferenceBean{
	
	private String activityName;
	private String activityDescription;
	private String site;
	private String price;
	private int place;
	private String openingDate;
	private String endDate;
	private String openingTime;
	private String closingTime;

	private ActivityType type;
	private Cadence cadence;
	
	private int idActivity;
	private int owner = 0;
	
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
		return endDate;
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

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	//-------inizio getter e setter delle preferenze----------------
	

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

	public void setInterestedCategories(boolean[] interestedCategories) {
		this.setArte(interestedCategories[0]);
		this.setCibo(interestedCategories[1]);
		this.setMusica(interestedCategories[2]);
		this.setSport(interestedCategories[3]);
		this.setSocial(interestedCategories[4]);
		this.setNatura(interestedCategories[5]);
		this.setEsplorazione(interestedCategories[6]);
		this.setRicorrenze(interestedCategories[7]);
		this.setModa(interestedCategories[8]);
		this.setShopping(interestedCategories[9]);
		this.setAdrenalina(interestedCategories[10]);
		this.setRelax(interestedCategories[11]);
		this.setIstruzione(interestedCategories[12]);
		this.setMonumenti(interestedCategories[13]);
	}
	
	public CreateActivityBean fillBean(CreateActivityBean cab, SuperActivity a, Partner owner) {
		cab.setActivityDescription(a.getDescription());
		cab.setActivityName(a.getName());
		cab.setPlace(a.getPlace().getId().intValue());
		if(owner!=null) {
			cab.setOwner(owner.getUserID().intValue());
		}else {
			cab.setOwner(0);
		}
		cab.setIdActivity(a.getId().intValue());
		
		cab.setOpeningTime(a.getFrequency().getFormattedOpeningTime());
		cab.setClosingTime(a.getFrequency().getFormattedClosingTime());
		
		cab.setInterestedCategories(a.getIntrestedCategories().getSetPreferences());
		if(a.getFrequency() instanceof ExpiringActivity) {
			cab.setType(ActivityType.SCADENZA);
			//SE L'ATTIVITA' HA UNA FREQUENZA DI TIPO CONTINUO, ALLORA
			//AVRA' ORARIO DI APERTURA, ORARIO DI CHIUSURA, DATA DI INIZIO
			//E DATA DI FINE; PERTANTO:
			
			cab.setEndDate(((ExpiringActivity)(a.getFrequency())).getFormattedEndDate());
			cab.setOpeningDate(((ExpiringActivity)(a.getFrequency())).getFormattedStartDate());
		}
		else if(a.getFrequency() instanceof ContinuosActivity) {
			cab.setType(ActivityType.CONTINUA);
			
		}
		else if(a.getFrequency() instanceof PeriodicActivity) {
			cab.setType(ActivityType.PERIODICA);
			//SE L'ATTIVITA' HA UNA FREQUENZA DI TIPO PERIODICO, ALLORA
			//AVRA' ORARIO DI APERTURA, ORARIO DI CHIUSURA, DATA DI INIZIO,
			//DATA DI FINE ED INFINE LA CADENZA; PERTANTO:
			cab.setEndDate(((PeriodicActivity)(a.getFrequency())).getFormattedEndDate());
			cab.setOpeningDate(((PeriodicActivity)(a.getFrequency())).getFormattedStartDate());
			cab.setCadence(((PeriodicActivity)(a.getFrequency())).getCadence());
		}
		else {
			Log.getInstance().getLogger().info("L'attivita non ha un tipo!");
			return null;
		}
		return cab;
	}


}
