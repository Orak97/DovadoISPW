<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import = "java.io.*,java.util.*,org.json.simple.JSONObject, org.json.simple.JSONArray, logic.model.Partner, logic.model.Coupon, logic.model.DAOCoupon"%>

<% 
	if(request.getParameter("idCoupon")!= null){
		try{
			int code = Integer.parseInt(request.getParameter("idCoupon"));
			DAOCoupon dao = DAOCoupon.getInstance();
			Coupon coupon = dao.findCouponPartner(code);
			
			JSONObject couponInfo = new JSONObject();
			
			couponInfo.put("response", "ok");
			couponInfo.put("code", coupon.getCouponCode());
			couponInfo.put("username", coupon.getUsernameExplorer());
			couponInfo.put("nomeAttivita", coupon.getNomeAttivita());
			couponInfo.put("discount", coupon.getDiscount());
			couponInfo.put("dataSchedulo", coupon.getFormattedScheduledDate());
			
			out.println(couponInfo);
			
		}catch(Exception e){
			JSONObject couponInfo = new JSONObject();
			couponInfo.put("response", "input error");
			out.println(couponInfo);
		}
	}
%>