<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.SuperActivity, logic.model.SuperUser, logic.model.User, logic.model.Channel, logic.model.Message, logic.controller.ChannelController, logic.model.Activity, org.json.simple.JSONObject, org.json.simple.JSONArray" %>

		<%


			if(request.getParameter("activity")!= null){

				/*preparo i dao*/
				DAOPreferences daoPref = DAOPreferences.getInstance();
				DAOActivity daoAct = DAOActivity.getInstance();
				DAOSuperUser daoSU = DAOSuperUser.getInstance();


				try{
					SuperUser u = (User) session.getAttribute("user");

					//il try Catch mi serve per evitare che se activity non è un intero il sistema crashi
					Long act = Long.parseLong(request.getParameter("activity"));

  					SuperActivity s = daoAct.findActivityByID(daoSU, act);

  					if(request.getParameter("textMsg")!= null){
  						String textMsg = request.getParameter("textMsg");

  						//TODO: magari ci mettiamo un'eccezione che è meno incasinato così
  						//if(textMsg.isEmpty()){
							ChannelController c = new ChannelController((Activity)s);
							c.writeMessage(u.getUsername(), textMsg, s);
  						//}
  					}

  					Channel cProva = s.getChannel();


  					JSONObject chatroom = new JSONObject();
  					JSONArray messages = new JSONArray();

  					chatroom.put("activityName",s.getName());
  					chatroom.put("activityId", s.getId());
  					chatroom.put("user",u.getUsername());


  					for(Message curr: cProva.getChat()){
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
