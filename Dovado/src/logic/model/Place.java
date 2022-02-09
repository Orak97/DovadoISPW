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
	
	public Place(PlaceBean bean){
		this.id = bean.getId();
		this.name = bean.getName();
		this.address = bean.getAddress();
		this.city = bean.getCity();
		this.region = bean.getRegion();
		this.civico = bean.getCivico();
		this.setCap(bean.getCap());
		this.hostedActivities = new ArrayList<>();
		this.latitudine = bean.getLatitudine();
		this.longitudine = bean.getLongitudine();
		
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
		return this.latitudine;
	}

	public void setLatitudine(float latitudine) {
		this.latitudine = latitudine;
	}

	public float getLongitudine() {
		return this.longitudine;
	}

	public void setLongitudine(float longitudine) {
		this.longitudine = longitudine;
	}
	
	public String getFormattedAddr() {
		String addr = "";
		if(address != null) addr+= address+" ";
		if(civico != null) addr+= civico;
		if(addr != "") addr+=", ";
		if(cap != null) addr += cap+", ";
		if(city != null) addr+= city+", ";
		if(region != null) addr+= region;
		return addr;
	}
}
