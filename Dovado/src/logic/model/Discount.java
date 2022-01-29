package logic.model;

public class Discount {
	int percentuale;
	boolean active;
	int price;
	
	public Discount(int percentuale, boolean active, int price) {
		this.percentuale = percentuale;
		this.active = active;
		this.price = price;
	}
	
	public int getPercentuale() {
		return this.percentuale;
	}
	
	public int getPrice() {
		return this.price;
	}
}
