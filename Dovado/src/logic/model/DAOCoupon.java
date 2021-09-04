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
	private final String couponJson = "WebContent/coupon.json" ;
	private final String couponsKey = "coupons";
	
	private DAOCoupon() {
		parser = new JSONParser();
	}
	
	public static DAOCoupon getInstance() {
		if(instance==null)
			instance = new DAOCoupon();
		return instance;
	}
	
	public boolean addCoupontoJSON(Coupon coupon) {
		try {
			Object coupons = parser.parse(new FileReader(couponJson));
			JSONObject couponObj = (JSONObject) coupons;
			JSONArray couponArray = (JSONArray) couponObj.get(couponsKey);
			
			
			if (findCoupon(coupon.getCouponCode())==(null)) {				
				JSONObject newCoupon = new JSONObject();

				newCoupon.put("code", coupon.getCouponCode());
				newCoupon.put("user", coupon.getuID());
				newCoupon.put("partner", coupon.getpID());
				newCoupon.put("discount", coupon.getDiscount());
				couponArray.add(newCoupon);

				FileWriter file = new FileWriter(couponJson);
				file.write(couponObj.toString());
				file.flush();
				file.close();
			}
			
			
		} catch(NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Coupon findCoupon(int code) {
		try {		
			Log.getInstance().logger.info("valore code:"+ code);

			Object coupons = parser.parse(new FileReader(couponJson));
			JSONObject couponObj = (JSONObject) coupons;
			JSONArray couponArray = (JSONArray) couponObj.get(couponsKey);
			JSONObject result;
			
			for(int i=0; i<couponArray.size();i++) {
				result = (JSONObject)couponArray.get(i);
				
				Long codeJSON = (Long) result.get("code");
				Log.getInstance().logger.info("valore codeJSON:"+ codeJSON);
				
				try {
					if (codeJSON.equals(Long.valueOf(code))) {
						Log.getInstance().logger.info("coupon trovato");
						Long user = (Long) result.get("user");
						Long partner = (Long) result.get("partner");
						Coupon coupon = new Coupon(user.intValue() , partner.intValue(), ((Long) result.get("discount")).intValue() );
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
				
			
		} catch(NullPointerException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public Coupon findCoupon(int userID, int partnerID) {
		try {		

			Object coupons = parser.parse(new FileReader(couponJson));
			JSONObject couponObj = (JSONObject) coupons;
			JSONArray couponArray = (JSONArray) couponObj.get(couponsKey);
			JSONObject result;
			
			for(int i=0; i<couponArray.size();i++) {
				result = (JSONObject)couponArray.get(i);
				
				Long userJSON = (Long) result.get("user");
				Log.getInstance().logger.info(userJSON +" = "+ userID);

				try {
					if (userJSON.equals(Long.valueOf(userID))) {		
						Log.getInstance().logger.info("user trovato");

						Long partnerJSON = (Long) result.get("partner");

						if(partnerJSON.equals(Long.valueOf(partnerID))) {
							Log.getInstance().logger.info("coupon trovato");
							Long user = (Long) result.get("user");
							Long partner = (Long) result.get("partner");
							Coupon coupon = new Coupon(user.intValue() , partner.intValue(), ((Long) result.get("discount")).intValue() );
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
				
			
		} catch(NullPointerException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
}
