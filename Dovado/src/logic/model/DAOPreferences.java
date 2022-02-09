package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DAOPreferences extends DAO{
	
	private static DAOPreferences INSTANCE;
	
	private DAOPreferences() {
	}
	
	public static DAOPreferences getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOPreferences();
		return INSTANCE;
	}

	public void updateUserPreferences(Long id, boolean[] bool) throws ClassNotFoundException, SQLException {
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call update_user_Preferences(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            
            stmt = conn.prepareCall(call);
            int i=1;
            stmt.setLong(i,id);
            for (boolean b : bool) {
            	i++;
				stmt.setBoolean(i,b);
			}
            stmt.execute();
            
        }finally {
           disconnRoutine();
        }
		
	}
	
}
