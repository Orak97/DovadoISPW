package logic.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.model.*;
public class ChannelController {
	
	private Channel channel;	
	private DAOChannel dao = DAOChannel.getInstance();
	private SuperUser usr;
	
	private Long idActivity;
	
	public ChannelController(Activity activity) {
		this.channel = activity.getChannel();
		}
	
	public ChannelController(SuperUser usr,Long idActivity) {
		this.usr = usr;
		this.idActivity = idActivity;
	}
	
	public void writeMessage(String user, String textMsg) {
		this.channel.addMsg(user, textMsg);
	}
	
	/*SPAGHETTI CODE:
	 * 
	 * Credo sia necessario che il Controller sia collegato al DAO in maniera migliore
	 * forse dando come parametro una SuperActivity si risparmia ma credo si violi la legge di Demetra
	 * also: come mai channel deve passare la lista di messaggi per salvare in persistenza?
	 * 
	 * */
	public void writeMessage(String user, String textMsg, SuperActivity activity) {
		this.channel.addMsg(user, textMsg);
		this.dao.updateChannelInJSON(this.channel.getChat(), activity);
	}
	
	
	public List<String[]> formattedChat(String user){
		ArrayList<Message> listOfMsg = (ArrayList<Message>) this.channel.getChat();
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
	
	//metodo da chiamare per inviare il messaggio sul DB
	public void sendMessage(String content) throws ClassNotFoundException, SQLException   {
		
		//eseguire qui i controlli del contenuto!!!
		
		Long idSender = usr.getUserID();
		dao.sendMsg(idActivity, content, idSender);
	}
}
