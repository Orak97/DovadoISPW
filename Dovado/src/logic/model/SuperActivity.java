package logic.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public abstract class SuperActivity {
	private Long id;
	private String name;
	private String description;
	private Place place;
	private FrequencyOfRepeat frequencyOfRepeat;
	private Channel channel;
	private Preferences intrestedCategories;
	
	
	protected SuperActivity(Long id,String nome, String description, Place place) {
		//chiamare questo metodo quando si vuole creare una attività continua!
		this.id = id;
		this.name= nome;
		this.setDescription(description);
		this.place = place;
		this.frequencyOfRepeat = new ContinuosActivity(null,null);
	}
	
	protected SuperActivity(Long id,String nome, String description, Place p, LocalTime openingTime, LocalTime closingTime) {
		//chiamare questo metodo quando si vuole creare una attività continua con orario apertura e chiusura
		this(id,nome,description,p);
		this.frequencyOfRepeat = new ContinuosActivity(openingTime,closingTime);
	}
	
	protected SuperActivity(Long id,String nome, String description, Place p, LocalTime[] openCloseTime, LocalDate[] startEndDate) {
		//chiamare questo metodo quando si vuole creare una attività a scadenza!
		//NOTA-----> Gli array openCloseTime e startEndDate presentano all'index 0 open/start e all'indice 1closing/end
		this(id,nome,description,p);
		this.frequencyOfRepeat = new ExpiringActivity(openCloseTime[0],openCloseTime[1],startEndDate[0],startEndDate[1]);
	}
	
	protected SuperActivity(Long id,String nome, String description, Place p, LocalTime[] openCloseTime, LocalDate[] startEndDate, Cadence cadence) {
		//chiamare questo metodo quando si vuole creare una attività periodica !
		//NOTA-----> Gli array openCloseTime e startEndDate presentano all'index 0 open/start e all'indice 1closing/end
		this(id,nome,description,p);
		this.frequencyOfRepeat = new PeriodicActivity(openCloseTime[0],openCloseTime[1],startEndDate[0],startEndDate[1],cadence);
	}
	
	
	//Non so se vanno bene getters e setters qui, dato che abbiamo tutte le attivita principalmente salvate nel JSON potremmo usare getters e setters di un bean apposito.

	public String getName() {
		return this.name;
	}
	public Place getPlace() {
		return this.place;
	}
	public Long getId() {
		return this.id;
	}
	
	public FrequencyOfRepeat getFrequency() {
		return this.frequencyOfRepeat;
	}
	
	public void setFrequency(FrequencyOfRepeat frequencyOfRepeat) {
		this.frequencyOfRepeat=frequencyOfRepeat;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public boolean playableOnThisDateTime(LocalDateTime timestamp) {
		/*
		 * Metodo usato per capire se questa attività è fattibile in un dato giorno
		 * 
		 * 
		 */
		return (frequencyOfRepeat.checkPlayability(timestamp));
	}
	
	public boolean isOpenOnThisTime(LocalTime time) {
		return frequencyOfRepeat.isOnTime(time);
	}
	
	public boolean isPlayableOnThisDate(LocalDate date) {
		return frequencyOfRepeat.checkDate(date);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Preferences getIntrestedCategories() {
		return intrestedCategories;
	}

	public void setIntrestedCategories(Preferences intrestedCategories) {
		this.intrestedCategories = intrestedCategories;
	}
	
	public String getPlayabilityInfo() {
		return frequencyOfRepeat.getStringInfo();
	}

	
}
