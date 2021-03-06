package logic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import logic.model.Message;

public class Channel {
	
	private ArrayList<Message> listOfMsg;
	private int sizeList;
	private long activityReferenced;
	

	
	public Channel (long activityId) {
		Log.getInstance().getLogger().info("Creato il canale da zero");
		this.activityReferenced=activityId;
		this.listOfMsg = new ArrayList<>();
	}
	
	
	
	public long getActivityReferenced() {
		return activityReferenced;
	}


	public void addMsg(String user, String msgText) {

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
	
	public List<Message> getChat(){
		return this.listOfMsg;
	}
	
	
	//IL METODO returnChat SECONDO ME VA NEL CONTROLLER, QUINDI QUESTI PER IL MOMENTO RESTANO INUTILIZZATI
	public List<String> getFormattedChat() {
		return getFormattedChat(0);
	}
	
	public List<String> getFormattedChat(int startIndex) {
		
		ArrayList<String> chat = new ArrayList<>();
		
		for (int i = startIndex; i < sizeList; i++) {
			
			chat.add(listOfMsg.get(i).getUsr() + "::" + listOfMsg.get(i).getMsgSentDate() + " ----> " + listOfMsg.get(i).getMsgText()+"\n");
		}
		/**if (chat.get(0) == null) {
			Log.getInstance().getLogger().info("sono vuoto");
		}
		else Log.getInstance().getLogger().info(+chat.get(0));**/
		return chat;
	}


	public void addMsg(String userName, String message, LocalDateTime time) {

		Message newMessage = new Message(userName, message, time); 
		listOfMsg.add(newMessage);
		
	}

}
