package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import logic.controller.FindActivityController;

public class DAOCoupon {
	private static DAOCoupon instance;
	private JSONParser parser; 
	private static final  String COUPONJSON = "WebContent/coupon.json" ;
	private static final  String COUPONSKEY = "coupons";
	private static final  String PARTNERKEY = "partner";
	private static final  String DISCOUNTKEY = "discount";
	
	private DAOCoupon() {
		parser = new JSONParser();
	}
	
	public static DAOCoupon getInstance() {
		if(instance==null)
			instance = new DAOCoupon();
		return instance;
	}
	
	public boolean addCoupontoJSON(Coupon coupon) {
		try (FileWriter file  = new FileWriter(COUPONJSON)){
			Object coupons = parser.parse(new FileReader(COUPONJSON));
			JSONObject couponObj = (JSONObject) coupons;
			JSONArray couponArray = (JSONArray) couponObj.get(COUPONSKEY);
			
			
			if (findCoupon(coupon.getCouponCode())==(null)) {				
				JSONObject newCoupon = new JSONObject();

				newCoupon.put("code", coupon.getCouponCode());
				newCoupon.put("user", coupon.getuID());
				newCoupon.put(PARTNERKEY, coupon.getpID());
				newCoupon.put(DISCOUNTKEY, coupon.getDiscount());
				couponArray.add(newCoupon);
				
				
				file.write(couponObj.toString());
				file.flush();
			}
			
			
		} catch(Exception e) {
			//removed exeption for future use: NullPointerException|FileNotFoundException|IOException
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	public Coupon findCoupon(int code) {
		try {		
			Log.getInstance().getLogger().info("valore code:"+ code);

			Object coupons = parser.parse(new FileReader(COUPONJSON));
			JSONObject couponObj = (JSONObject) coupons;
			JSONArray couponArray = (JSONArray) couponObj.get(COUPONSKEY);
			JSONObject result;
			
			for(int i=0; i<couponArray.size();i++) {
				result = (JSONObject)couponArray.get(i);
				
				Long codeJSON = (Long) result.get("code");
				Log.getInstance().getLogger().info("valore codeJSON:"+ codeJSON);
				
				try {
					if (codeJSON.equals(Long.valueOf(code))) {
						Log.getInstance().getLogger().info("coupon trovato");
						Long user = (Long) result.get("user");
						Long partner = (Long) result.get(PARTNERKEY);
						Coupon coupon = new Coupon(user.intValue() , partner.intValue(), ((Long) result.get(DISCOUNTKEY)).intValue() );
						coupon.setCouponCode(((Long) result.get("code")).intValue());
						
						return coupon;
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					return null;
				} catch (ClassCastException e) {
					e.printStackTrace();
					return null;
				}
			}
				
			
		} catch(Exception e) {
			//removed exeption for future use: NullPointerException|FileNotFoundException|IOException
			e.printStackTrace();
			return null;
		} 
		return null;
	}
	
	public Coupon findCoupon(int userID, int partnerID) {
		try {		

			Object coupons = parser.parse(new FileReader(COUPONJSON));
			JSONObject couponObj = (JSONObject) coupons;
			JSONArray couponArray = (JSONArray) couponObj.get(COUPONSKEY);
			JSONObject result;
			
			for(int i=0; i<couponArray.size();i++) {
				result = (JSONObject)couponArray.get(i);
				
				Long userJSON = (Long) result.get("user");
				Log.getInstance().getLogger().info(userJSON +" = "+ userID);

				try {
					if (userJSON.equals(Long.valueOf(userID))) {		
						Log.getInstance().getLogger().info("user trovato");

						Long partnerJSON = (Long) result.get(PARTNERKEY);

						if(partnerJSON.equals(Long.valueOf(partnerID))) {
							Log.getInstance().getLogger().info("coupon trovato");
							Long user = (Long) result.get("user");
							Long partner = (Long) result.get(PARTNERKEY);
							Coupon coupon = new Coupon(user.intValue() , partner.intValue(), ((Long) result.get(DISCOUNTKEY)).intValue() );
							coupon.setCouponCode(((Long) result.get("code")).intValue());
											
							return coupon;
						}
						
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					return null;
				} catch (ClassCastException e) {
					e.printStackTrace();
					return null;
				}
			}
				
			
		} catch (Exception e) {
			//removed exeption for future use: NullPointerException|FileNotFoundException|IOException
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
}
