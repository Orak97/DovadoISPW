package logic.model;


import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Message {
	private String msgText;
	private long user;
	private LocalDateTime sentDate;
	
	public Message() {
		this(0,"");
	}

	public Message (long user, String msgText) {
		
	   this.user = user;
	   this.msgText = msgText;
	   this.sentDate = LocalDateTime.now();		
	}
	
	public Message (long user, String msgText,LocalDateTime time) {
		
		   this.user = user;
		   this.msgText = msgText;
		   this.sentDate = time;		
		}
	
	//Non faccio Override poiche non controllo gli Objects in generale ma solo i Message
	public boolean equals(Message m) {
		if ( m != null) {
			
			return this.user == m.getUsr() &&
					this.getMsgSentDate().equals(m.getMsgSentDate()) &&
					 this.msgText == m.getMsgText()	;
		}
		else return false;
	}	
	
	public String getMsgText() {
		return msgText;
	
	}
	
	public long getUsr() {
		return this.user;
	}		
	
	
	//"yyyy-MM-dd'T'HH:mm:ss.SSS" -- "yyyy/MM/dd HH:mm:ss"
	public String getMsgSentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy'  'HH:mm");
		return dtf.format(sentDate);
	}
	
	public void setSentDate(String date) {
		this.sentDate = LocalDateTime.parse(date);
	}
	
	public static void main(String[] args) {
		
	}
		
}
