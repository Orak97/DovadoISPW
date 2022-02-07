package logic.model;

public class PlaceBean {
	private Long id;
	private String civico;
	private String city;
	private String region;
	private String address;
	private String name;
	private String cap;
	private float latitudine;
	private float longitudine;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
