package logic.model;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import logic.controller.FindActivityController;

public class DAOChannel extends DAO{
	private static DAOChannel INSTANCE;

	
	
	
	private DAOChannel() {
	}
	
	public static DAOChannel getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOChannel();
		return INSTANCE;
	}

	
	public Channel getChannel(Long idActivity) throws ClassNotFoundException, SQLException  {
		//metodo per ottenere un channel partendo dall'id dell'attività
		Channel channel = new Channel(idActivity);
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_channel(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1,idActivity);
            
            if(stmt.execute()) {
            
            	 //ottengo il resultSet
                ResultSet rs = stmt.getResultSet();
                
                while(rs.next()) {
                	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                	String username = rs.getString("usernameSender");
                	String content = rs.getString("contenuto");
                	String timestamp = rs.getString("data_invio");
                	
                	channel.addMsg(username, content, LocalDateTime.parse(timestamp,dtf));
                	
                }
            	
            }
            
            
            
        }finally {
        	//Clean-up dell'ambiente
            disconnRoutine();
        }
		return channel;
	}
	
	public void sendMsg(Long idActivity, String content, Long idSender) throws SQLException, ClassNotFoundException {
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call send_msg(?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1,idActivity);
            stmt.setString(2, content);
            stmt.setLong(3, idSender);
            
            stmt.execute();
        	
        }finally {
        	//Clean-up dell'ambiente
            disconnRoutine();
        }
	}

}
