package logic.model;
import java.util.ArrayList;

public class Place {
	private Long id;
	private String civico;
	private String city;
	private String region;
	private String address;
	private String name;
	private String cap;
	private float latitudine;
	private float longitudine;
	
	//Modificato l'arraylist di attivita in un array di long, in questo modo
	//Verranno indicate le attivita a cui fa riferimento il posto mediante il loro id.
	private ArrayList<Long> hostedActivities;
	
	public Place(Long id,String name,String address,String city,String region,String civico,String cap, float latitudine, float longitudine){
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.region = region;
		this.civico = civico;
		this.setCap(cap);
		this.hostedActivities = new ArrayList<>();
		this.latitudine = latitudine;
		this.longitudine = longitudine;
		
	}

	public void setId(long l) {
		this.id = l;
	}
	
	public void addActivity(Long id) {
		this.hostedActivities.add(id);
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public String getAddress() {
		return this.address;
	}

	public String getCity() {
		return this.city;
	}
	
	public String getRegion() {
		return this.region;
	}

	public Object getCivico() {
		return this.civico;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public float getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(float latitudine) {
		this.latitudine = latitudine;
	}

	public float getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(float longitudine) {
		this.longitudine = longitudine;
	}
}
