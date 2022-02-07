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
	//questo metodo è chiamato solo dal PAC
	public ChannelController(Activity activity) {
		this.channel = activity.getChannel();
		}
	//Questo da chat.jsp
	public ChannelController(SuperUser usr,Long idActivity) {
		this.usr = usr;
		this.idActivity = idActivity;
	}
	
	/*SPAGHETTI CODE: 
	 * 
	 * Credo sia necessario che il Controller sia collegato al DAO in maniera migliore
	 * forse dando come parametro una SuperActivity si risparmia ma credo si violi la legge di Demetra
	 * also: come mai channel deve passare la lista di messaggi per salvare in persistenza?
	 * 
	 * */
	

	//metodo da chiamare per inviare il messaggio sul DB --- Referenziato da chat.jsp
	public void sendMessage(String content) throws ClassNotFoundException, SQLException   {
		
		//eseguire qui i controlli del contenuto!!!
		
		Long idSender = usr.getUserID();
		dao.sendMsg(idActivity, content, idSender);
	}
}
