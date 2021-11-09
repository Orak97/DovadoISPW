<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

<%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.SuperActivity, logic.model.SuperUser, logic.model.User, logic.model.Channel, logic.model.Message, logic.controller.ChannelController, logic.model.Activity" %>

		<%	
		
			
			if(request.getParameter("activity")!= null){
				
				/*preparo i dao*/
				DAOPreferences daoPref = DAOPreferences.getInstance();
				DAOActivity daoAct = DAOActivity.getInstance();
				DAOSuperUser daoSU = DAOSuperUser.getInstance();
				
				
				try{
					SuperUser u = (User) session.getAttribute("user");
					
					Long act = Long.parseLong(request.getParameter("activity"));
				
  					SuperActivity s = daoAct.findActivityByID(daoSU, act);
			  			
  					if(request.getParameter("textMsg")!= null){	
  						String textMsg = request.getParameter("textMsg"); 
						ChannelController c = new ChannelController((Activity)s);
						c.writeMessage(u.getUserID().intValue(), textMsg, s);
  					}
  					
  					Channel cProva = s.getChannel();
			  			
  					
		%>


					<div class="col-8 chat d-flex flex-column" id="chat">
						<div class="chat-bar d-flex">
								<div class="text-center text-white flex-grow-1 fs-2"><%= s.getName() %>'s channel</div>
								<button type="button" class="btn-close btn-close-white me-2 m-auto" aria-label="Close"></button>
						</div>
						<div class="container-fluid chatroom flex-grow-1">
						  		<%-- va implementata la chatroom --%>		
							<% for(Message curr: cProva.getChat()){ %>	
						  		<div class="row d-flex <%= curr.getUsr() == u.getUserID() ? "justify-content-end" : "justify-content-start" %>">
									<div class="toast show message <%= curr.getUsr() == u.getUserID() ? "msg-sent" : "msg-recieved" %>" role="alert" aria-live="assertive" aria-atomic="true">
										<div class="toast-header">
											<strong class="me-auto"><%= curr.getUsr() %></strong>
											<small class="text-muted"><%= curr.getMsgSentDate() %></small>
										</div>
								    	<div class="toast-body">
											<%= curr.getMsgText() %>
										</div>
									</div>
								</div>
							<% } %>
								<!-- fine loop  -->
						</div>
						<div class="d-flex msg-bar">
								<input type="text" class="form-control flex-grow-1" id="chatField" placeholder="scrivi qualcosa"> 
								<button type="button" class="btn btn-outline-light send-btn disabled" onclick="sendMsg(<%=s.getId()%>)" id="send-btn"> <i class="bi bi-send"></i> </button>
						</div>
					</div>
		
		<% 		}catch(NumberFormatException e){
					out.println("poi vedo");
				}
		
			} else { %>
			poi handlo l'errore
		<% 	} %>