package logic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import logic.model.Message;

public class Channel {
	
	private ArrayList<Message> listOfMsg;
	private int sizeList;
	private long activityReferenced;
	
	//Utile solo se vogliamo aggiornare dinamicamente senza prendere tutta la chat. Probabilmente verra cancellato
	private Message lastMessage;

	
	public Channel (long activityId) {
		System.out.println("Creato il canale da zero");
		this.activityReferenced=activityId;
		this.listOfMsg = new ArrayList<Message>();
	}
	
	
	public void addMsg(long user, String msgText) {

		Message newMessage = new Message(user, msgText); 
		listOfMsg.add(newMessage);

	}
		
	
	//Da vedere meglio, potrebbe essere necessario passare gli attributi invece della classe Message. Per il momento non e usato
	public void removeMsg(Message msg) {
		
		for (int i = 0; i < sizeList; i++) {
			
			if (msg.equals(listOfMsg.get(i))){
				
				listOfMsg.remove(i);
			}
		}
	}
	
	public ArrayList<Message> getChat(){
		return this.listOfMsg;
	}
	
	
	//IL METODO returnChat SECONDO ME VA NEL CONTROLLER, QUINDI QUESTI PER IL MOMENTO RESTANO INUTILIZZATI
	public ArrayList<String> getFormattedChat() {
		return getFormattedChat(0);
	}
	
	public ArrayList<String> getFormattedChat(int startIndex) {
		
		ArrayList<String> chat = new ArrayList<String>();
		
		for (int i = startIndex; i < sizeList; i++) {
			
			chat.add(listOfMsg.get(i).getUsr() + "::" + listOfMsg.get(i).getMsgSentDate() + " ----> " + listOfMsg.get(i).getMsgText()+"\n");
		}
		/**if (chat.get(0) == null) {
			System.out.println("sono vuoto");
		}
		else System.out.println(chat.get(0));**/
		return chat;
	}


	public void addMsg(Long userID, String message, LocalDateTime time) {

		Message newMessage = new Message(userID, message, time); 
		listOfMsg.add(newMessage);
		
	}

}
