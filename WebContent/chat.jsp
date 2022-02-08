<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

<%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.SuperActivity, logic.model.SuperUser, logic.model.User, logic.model.Channel, logic.model.Message, logic.controller.ChannelController, logic.model.Activity, logic.model.DAOChannel ,org.json.simple.JSONObject, org.json.simple.JSONArray" %>

		<%	
		
			
			if(request.getParameter("activity")!= null && (session.getAttribute("user") != null || session.getAttribute("partner") != null)){
				
				/*preparo i dao*/
				DAOActivity daoAct = DAOActivity.getInstance();
				DAOChannel daoCh = DAOChannel.getInstance();
				
				
				try{
					SuperUser u = (SuperUser) session.getAttribute("user");
					if(u == null) u = (SuperUser) session.getAttribute("partner");
					
					//il try Catch mi serve per evitare che se activity non è un intero il sistema crashi
					Long act = Long.parseLong(request.getParameter("activity"));
				
  					Activity activity = daoAct.getActivityById(act);
			  		
					
							
  					if(request.getParameter("textMsg")!= null){	
  						String textMsg = request.getParameter("textMsg");
  						
  						//TODO: magari ci mettiamo un'eccezione che è meno incasinato così 
						ChannelController c = new ChannelController(u, activity.getId());
						c.sendMessage(textMsg);
						System.out.println(textMsg);
  					
  					}
  					
  					Channel channel = daoCh.getChannel(act);
			  			
  					
  					JSONObject chatroom = new JSONObject();
  					JSONArray messages = new JSONArray();
  					
  					chatroom.put("activityName",activity.getName());
  					chatroom.put("activityId", activity.getId());
  					chatroom.put("user",u.getUsername());
  					
  					
  					for(Message curr: channel.getChat()){
  						JSONObject msg = new JSONObject();
  						msg.put("sender", curr.getUsr());
  						msg.put("sendDate", curr.getMsgSentDate());
  						msg.put("text", curr.getMsgText());
  						
  						messages.add(msg);
  					}
  					
  					chatroom.put("messages",messages);
  					
  					
  					out.println(chatroom);
  					
		
		
		}catch(NumberFormatException e){
					//TODO			
					out.println("poi vedo");
				}
		
			} else { //TODO %>
			
			poi handlo l'errore
		<% 	} %>