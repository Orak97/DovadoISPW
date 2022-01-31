package logic.model;

import java.time.LocalDateTime;


public class Coupon {
	private int userID;
	private int activityID;
	private int couponCode;
	private int discount;
	private LocalDateTime scadenza;
	
	public Coupon(int user, int activity, int couponCode,int discount, LocalDateTime scadenza) {
		this.userID = user;
		this.activityID = activity;
		this.couponCode = couponCode;
		this.discount = discount;
		this.scadenza = scadenza;
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
	
}
