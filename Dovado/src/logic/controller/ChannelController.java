package logic.controller;

import java.util.ArrayList;
import java.util.List;

import logic.model.*;
public class ChannelController {
	
	private Channel channell;	
	
	public ChannelController(Activity activity) {
		this.channell = activity.getChannel();
		}
	
	public void writeMessage(String user, String textMsg) {
		this.channell.addMsg(user, textMsg);
	}
	
	
	public List<String[]> formattedChat(String user){
		ArrayList<Message> listOfMsg = (ArrayList<Message>) this.channell.getChat();
		ArrayList<String[]> chat = new ArrayList<>();
		String[] msg;
		if (listOfMsg.size() == 1) {
			return chat;
		}
		for (int i = 1; i < listOfMsg.size() ; i++) {
			msg = new String[2];
			if (listOfMsg.get(i).getUsr().equals(user)) {
				msg[0] = "0";
			}
			else msg[0] = "1";
			
			msg[1] = listOfMsg.get(i).getMsgSentDate() + " ----> " + listOfMsg.get(i).getMsgText()+"\n";
			chat.add(msg);
		}
		return chat;
	}
}
