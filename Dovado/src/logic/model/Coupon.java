package logic.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Coupon {
	private int userID;
	private int activityID;
	private int couponCode;
	private int discount;
	private LocalDateTime scadenza;
	
	private String usernameExplorer;
	private String nomeAttivita;
	private LocalDateTime scheduledDate;
	
	public Coupon(int user, int activity, int couponCode,int discount, LocalDateTime scadenza) {
		this.userID = user;
		this.activityID = activity;
		this.couponCode = couponCode;
		this.discount = discount;
		this.scadenza = scadenza;
	}
	
	//costruttore per il partner in modo che vede tutte le info necessarie
	public Coupon(int couponCode,String usernameExplorer, String nomeAttivita,int sconto,LocalDateTime scadenza ,LocalDateTime scheduledDate) {
		this.couponCode = couponCode;
		this.usernameExplorer=usernameExplorer;
		this.discount = sconto;
		this.nomeAttivita = nomeAttivita;
		this.scadenza= scadenza;
		this.scheduledDate = scheduledDate;
	}

	
	public int getuID() {
		return userID;
	}

	public void setuID(int uID) {
		this.userID = uID;
	}

	public int getpID() {
		return activityID;
	}

	public void setpID(int pID) {
		this.activityID = pID;
	}

	public int getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(int couponCode) {
		this.couponCode = couponCode;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}



	public LocalDateTime getScadenza() {
		return scadenza;
	}



	public String getUsernameExplorer() {
		return usernameExplorer;
	}



	public String getNomeAttivita() {
		return nomeAttivita;
	}



	public LocalDateTime getScheduledDate() {
		return scheduledDate;
	}
	
	public String getFormattedScheduledDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return scheduledDate.format(formatter);
	}
	
}
