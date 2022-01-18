package logic.model;


import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Message {
	private String msgText;
	private String userName;
	private LocalDateTime sentDate;
	
	public Message() {
		this("","");
	}

	public Message (String user, String msgText) {
		
	   this.userName = user;
	   this.msgText = msgText;
	   this.sentDate = LocalDateTime.now();		
	}
	
	public Message (String user, String msgText,LocalDateTime time) {
		
		   this.userName = user;
		   this.msgText = msgText;
		   this.sentDate = time;		
		}
	
	//Non faccio Override poiche non controllo gli Objects in generale ma solo i Message
	public boolean equals(Message m) {
		if ( m != null) {
			
			return this.userName == m.getUsr() &&
					this.getMsgSentDate().equals(m.getMsgSentDate()) &&
					 this.msgText == m.getMsgText()	;
		}
		else return false;
	}	
	
	public String getMsgText() {
		return msgText;
	
	}
	
	public String getUsr() {
		return this.userName;
	}		
	
	
	//"yyyy-MM-dd'T'HH:mm:ss.SSS" -- "yyyy/MM/dd HH:mm:ss"
	public String getMsgSentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy'  'HH:mm");
		return dtf.format(sentDate);
	}
	
	public void setSentDate(String date) {
		this.sentDate = LocalDateTime.parse(date);
	}
	
}
