<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import = "java.io.*,java.util.*, logic.model.Discount, logic.model.DAOActivity,org.json.simple.JSONObject, org.json.simple.JSONArray, logic.model.User, logic.model.Partner, logic.controller.UpdateCertActController, logic.model.Log, java.sql.SQLException"%>


<jsp:useBean id="discountBean" scope="request" class="logic.model.DiscountBean" />
<jsp:setProperty name="discountBean" property="*" />


<% 
	if(request.getParameter("activity")!= null && session.getAttribute("user") != null){
		/*preparo i dao*/
		DAOActivity dao = DAOActivity.getInstance();	
		
		User utente = (User) session.getAttribute("user");
		
		try{
			Long act = Long.parseLong(request.getParameter("activity"));
			ArrayList<Discount> activityDiscounts = (ArrayList<Discount>)dao.viewDiscounts(act);
			
			JSONObject discountInfo = new JSONObject();
			JSONArray discounts = new JSONArray();
		
			discountInfo.put("usrWallet",utente.getBalance());
			
			for(Discount curr: activityDiscounts){
				if(curr.isActive()){
					JSONObject discount = new JSONObject();
					discount.put("percentage",curr.getPercentuale());
					discount.put("price",curr.getPrice());
				
					discounts.add(discount);
				}
			}
			
			discountInfo.put("discounts",discounts);
		
			out.println(discountInfo);
		}catch(NumberFormatException e){			
			out.println("invalid-activity");
		}
	
	}

	if(request.getParameter("activity")!=null && session.getAttribute("partner")!=null){
		DAOActivity dao = DAOActivity.getInstance();
		
		Partner partner = (Partner) session.getAttribute("partner");
		
		try{
			Long act = Long.parseLong(request.getParameter("activity"));
			ArrayList<Discount> activityDiscounts = (ArrayList<Discount>)dao.viewDiscounts(act);
			
			JSONArray discounts = new JSONArray();
			
			for(Discount curr: activityDiscounts){
				JSONObject discount = new JSONObject();
				discount.put("percentage",curr.getPercentuale());
				discount.put("price",curr.getPrice());
				discount.put("state",curr.isActive());
				
				discounts.add(discount);
			}
			
			out.println(discounts);
			
		}catch(NumberFormatException e){			
			out.println("invalid-activity");
		}
		
	}
	
	if(request.getParameter("actToEdit")!=null){
		Partner partner = (Partner) session.getAttribute("partner");
		if(partner == null) throw new NullPointerException();
		UpdateCertActController controller = new UpdateCertActController(discountBean,partner);
		try{
			controller.modifyDiscounts();
			response.sendRedirect("HomePartner.jsp?serverResponse=\"Sconti correttamente modificati!\"");
		}catch(NullPointerException se){
			response.sendRedirect("HomePartner.jsp");
		}catch(SQLException se){
			Log.getInstance().getLogger().warning("Errore: " + se.getErrorCode() +" - Messaggio: " + se.getMessage());
			String serverResponse;
			if(se.getSQLState().equals("45000")){
				serverResponse = se.getMessage().substring(12);
			}else{
				serverResponse = "Qualcosa nel server è andato storto";
			}
			response.sendRedirect("HomePartner.jsp?serverResponse=\""+serverResponse+"\"");
		}catch(Exception se){
			response.sendRedirect("HomePartner.jsp?serverResponse=\"Qualcosa nel server è andato storto\"");
		}
	}

%>