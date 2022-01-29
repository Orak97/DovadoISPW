<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import = "java.io.*,java.util.*, logic.model.Discount, logic.model.DAOActivity,org.json.simple.JSONObject, org.json.simple.JSONArray, logic.model.User"%>

<% 
	if(request.getParameter("activity")!= null){
		/*preparo i dao*/
		DAOActivity dao = DAOActivity.getInstance();	
		
		
		if(session.getAttribute("user") == null) throw new Exception();
		
		User utente = (User) session.getAttribute("user");
		
		try{
			Long act = Long.parseLong(request.getParameter("activity"));
			ArrayList<Discount> activityDiscounts = dao.viewDiscounts(act);
			
			JSONObject discountInfo = new JSONObject();
			JSONArray discounts = new JSONArray();
		
			discountInfo.put("usrWallet",utente.getBalance());
			
			for(Discount curr: activityDiscounts){
				JSONObject discount = new JSONObject();
				discount.put("percentage",curr.getPercentuale());
				discount.put("price",curr.getPrice());
				
				discounts.add(discount);
			}
			
			discountInfo.put("discounts",discounts);
		
			out.println(discountInfo);
		}catch(NumberFormatException e){			
			out.println("invalid-activity");
		}
	
	}

%>