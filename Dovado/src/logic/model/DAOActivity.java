package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logic.controller.ActivityType;
import logic.controller.CreateActivityController;
import logic.controller.FindActivityController;

public class DAOActivity {
	
	private static DAOActivity INSTANCE;
	
	//----------database--------------------------------------
	
	private static String USER = "sav"; //DA CAMBIARE
	private static String PASSWORD = "pellegrini"; //DA CAMBIARE
	private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
			
	//------------------------------------------------------------
	
	
	private DAOActivity() {
	}
	
	public static DAOActivity getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOActivity();
		return INSTANCE;
	}
	
	public void createNormalActivity(String activityName, String description, String sito, String prezzo, int place, String proprietario, boolean arte, boolean cibo, boolean musica, boolean sport, boolean social, boolean natura, boolean esplorazione, boolean ricorrenza, boolean moda, boolean shopping, boolean adrenalina, boolean monumenti, boolean relax, boolean istruzione, String tipo, String orApertura, String orChiusura, String dataInizio, String dataFine, String cadenza) throws Exception {
		//metodo per creare nel db una classe Activity
		
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call create_activity(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            
            
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,activityName);
            stmt.setString(2,description);
            stmt.setString(3,sito);
            stmt.setString(4,prezzo);
            stmt.setInt(5,place);
            stmt.setString(6,proprietario);
            stmt.setBoolean(7,arte);
            stmt.setBoolean(8,cibo);
            stmt.setBoolean(9,musica);
            stmt.setBoolean(10,sport);
            stmt.setBoolean(11,social);
            stmt.setBoolean(12,natura);
            stmt.setBoolean(13,esplorazione);
            stmt.setBoolean(14,ricorrenza);
            stmt.setBoolean(15,moda);
            stmt.setBoolean(16,shopping);
            stmt.setBoolean(17,adrenalina);
            stmt.setBoolean(18,monumenti);
            stmt.setBoolean(19,relax);
            stmt.setBoolean(20,istruzione);
            stmt.setString(21,tipo);
            stmt.setString(22,orApertura);
            stmt.setString(23,orChiusura);
            stmt.setString(24,dataInizio);
            stmt.setString(25,dataFine);
            stmt.setString(26,cadenza);
            
            stmt.execute();
            
        }finally {
            // STEP 5.2: Clean-up dell'ambiente
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            	throw(se2);
            }
            try {
                if (conn != null)
                    conn.close();
                	System.out.println("Disconnetted database successfully...");
                	
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        
		
	}
	
	public ArrayList<Activity> getNearbyActivities(double userLat,double userLong, float maxDistance) throws Exception{
		//metodo per ottenere TUTTE le attività entro una maxDistance(Km) partendo da un punto di coordinate geografiche(userLat,userLong)
		
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        ArrayList<Activity> nearbyActivities = new ArrayList<Activity>();
        
        try {
        	
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_nearby_activities(?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setFloat(1, maxDistance);
            stmt.setDouble(2, userLat);
            stmt.setDouble(3, userLong);
            
            if(!stmt.execute()) {
            	Exception e = new Exception("Sembra che non esistano attività vicine a te");
            	throw e;
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	Activity curr;
            	
            	int idActivity = rs.getInt("id");
            	String nome = rs.getString("nome");
            	String descrizione = rs.getString("descrizione");
            	int place = rs.getInt("luogo");
            	
            	
            	
            	//NOTA: andre questo puoi copiarlo e incollarlo quando devi riempire il bean nella view per salvare un attività
            	CreateActivityBean bean = new CreateActivityBean();
            	
            	bean.setIdActivity(idActivity);
            	
            	bean.setActivityName(nome);
            	bean.setActivityDescription(descrizione);
            	bean.setPlace(place);
            	
            	
            	bean.setOpeningTime(rs.getString("orario_apertura"));
            	bean.setClosingTime(rs.getString("orario_chiusura"));
            	
            	bean.setOpeningDate(rs.getString("data_inizio"));
            	bean.setEndDate(rs.getString("data_fine"));
            	
            	//serve per convertire da string -> enum ActivityType
            	bean.setType(ActivityType.valueOf(rs.getString("tipo")));
            	if(rs.getString("cadenza") != null)
            	bean.setCadence(Cadence.valueOf(rs.getString("cadenza")));
            	
            	bean.setArte(rs.getBoolean("arte"));
            	bean.setCibo(rs.getBoolean("cibo"));
            	bean.setMusica(rs.getBoolean("musica"));
            	bean.setSport(rs.getBoolean("sport"));
            	bean.setSocial(rs.getBoolean("social"));
            	bean.setNatura(rs.getBoolean("natura"));
            	bean.setEsplorazione(rs.getBoolean("esplorazione"));
            	bean.setRicorrenze(rs.getBoolean("ricorrenze"));
            	bean.setModa(rs.getBoolean("moda"));
            	bean.setShopping(rs.getBoolean("shopping"));
            	bean.setAdrenalina(rs.getBoolean("adrenalina"));
            	bean.setRelax(rs.getBoolean("relax"));
            	bean.setIstruzione(rs.getBoolean("istruzione"));
            	bean.setIstruzione(rs.getBoolean("monumenti"));
            	
            	bean.setOwner(rs.getInt("proprietario"));
            	//------ fine copia incolla -----------------------------------------------------
            	
            	CreateActivityController createActivity = new CreateActivityController(bean);
            	curr = createActivity.createActivity();
            	
            	nearbyActivities.add(curr);
            }
            
            rs.close();
        	
        }finally {
            // STEP 5.2: Clean-up dell'ambiente
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            	throw(se2);
            }
            try {
                if (conn != null)
                    conn.close();
                	System.out.println("Disconnetted database successfully...");
                	
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        
        return nearbyActivities;
	}
	
	public Activity getActivityById(Long id) throws Exception{
		//metodo per ottenere un attività entro una maxDistance(Km) partendo dal suo id
		
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
       Activity act = null;
        
        try {
        	
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_activity_by_id(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1, id);
            
            if(!stmt.execute()) {
            	Exception e = new Exception("Sembra che non esistano attività vicine a te");
            	throw e;
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	Activity curr;
            	
            	int idActivity = rs.getInt("id");
            	String nome = rs.getString("nome");
            	String descrizione = rs.getString("descrizione");
            	int place = rs.getInt("luogo");
            	
            	
            	
            	//NOTA: andre questo puoi copiarlo e incollarlo quando devi riempire il bean nella view per salvare un attività
            	CreateActivityBean bean = new CreateActivityBean();
            	
            	bean.setIdActivity(idActivity);
            	
            	bean.setActivityName(nome);
            	bean.setActivityDescription(descrizione);
            	bean.setPlace(place);
            	
            	
            	bean.setOpeningTime(rs.getString("orario_apertura"));
            	bean.setClosingTime(rs.getString("orario_chiusura"));
            	
            	bean.setOpeningDate(rs.getString("data_inizio"));
            	bean.setEndDate(rs.getString("data_fine"));
            	
            	//serve per convertire da string -> enum ActivityType
            	bean.setType(ActivityType.valueOf(rs.getString("tipo")));
            	if(rs.getString("cadenza") != null)
            	bean.setCadence(Cadence.valueOf(rs.getString("cadenza")));
            	
            	bean.setArte(rs.getBoolean("arte"));
            	bean.setCibo(rs.getBoolean("cibo"));
            	bean.setMusica(rs.getBoolean("musica"));
            	bean.setSport(rs.getBoolean("sport"));
            	bean.setSocial(rs.getBoolean("social"));
            	bean.setNatura(rs.getBoolean("natura"));
            	bean.setEsplorazione(rs.getBoolean("esplorazione"));
            	bean.setRicorrenze(rs.getBoolean("ricorrenze"));
            	bean.setModa(rs.getBoolean("moda"));
            	bean.setShopping(rs.getBoolean("shopping"));
            	bean.setAdrenalina(rs.getBoolean("adrenalina"));
            	bean.setRelax(rs.getBoolean("relax"));
            	bean.setIstruzione(rs.getBoolean("istruzione"));
            	bean.setIstruzione(rs.getBoolean("monumenti"));
            	
            	bean.setOwner(rs.getInt("proprietario"));
            	//------ fine copia incolla -----------------------------------------------------
            	
            	CreateActivityController createActivity = new CreateActivityController(bean);
            	act = createActivity.createActivity();
            }
            
            rs.close();
        	
        }finally {
            // STEP 5.2: Clean-up dell'ambiente
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            	throw(se2);
            }
            try {
                if (conn != null)
                    conn.close();
                	System.out.println("Disconnetted database successfully...");
                	
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        
        return act;
	}
	
	public ArrayList<Activity> getPartnerActivities(Long idPartner) throws Exception{
		//metodo per ottenere TUTTE le attività entro una maxDistance(Km) partendo da un punto di coordinate geografiche(userLat,userLong)
		
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        ArrayList<Activity> nearbyActivities = new ArrayList<Activity>();
        
        try {
        	
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_partner_activities(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1, idPartner);
            
            if(!stmt.execute()) {
            	Exception e = new Exception("Sembra che non esistano attività vicine a te");
            	throw e;
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	Activity curr;
            	
            	int idActivity = rs.getInt("id");
            	String nome = rs.getString("nome");
            	String descrizione = rs.getString("descrizione");
            	int place = rs.getInt("luogo");
            	
            	
            	
            	//NOTA: andre questo puoi copiarlo e incollarlo quando devi riempire il bean nella view per salvare un attività
            	CreateActivityBean bean = new CreateActivityBean();
            	
            	bean.setIdActivity(idActivity);
            	
            	bean.setActivityName(nome);
            	bean.setActivityDescription(descrizione);
            	bean.setPlace(place);
            	
            	
            	bean.setOpeningTime(rs.getString("orario_apertura"));
            	bean.setClosingTime(rs.getString("orario_chiusura"));
            	
            	bean.setOpeningDate(rs.getString("data_inizio"));
            	bean.setEndDate(rs.getString("data_fine"));
            	
            	//serve per convertire da string -> enum ActivityType
            	bean.setType(ActivityType.valueOf(rs.getString("tipo")));
            	if(rs.getString("cadenza") != null)
            	bean.setCadence(Cadence.valueOf(rs.getString("cadenza")));
            	
            	bean.setArte(rs.getBoolean("arte"));
            	bean.setCibo(rs.getBoolean("cibo"));
            	bean.setMusica(rs.getBoolean("musica"));
            	bean.setSport(rs.getBoolean("sport"));
            	bean.setSocial(rs.getBoolean("social"));
            	bean.setNatura(rs.getBoolean("natura"));
            	bean.setEsplorazione(rs.getBoolean("esplorazione"));
            	bean.setRicorrenze(rs.getBoolean("ricorrenze"));
            	bean.setModa(rs.getBoolean("moda"));
            	bean.setShopping(rs.getBoolean("shopping"));
            	bean.setAdrenalina(rs.getBoolean("adrenalina"));
            	bean.setRelax(rs.getBoolean("relax"));
            	bean.setIstruzione(rs.getBoolean("istruzione"));
            	bean.setIstruzione(rs.getBoolean("monumenti"));
            	
            	bean.setOwner(rs.getInt("proprietario"));
            	//------ fine copia incolla -----------------------------------------------------
            	
            	CreateActivityController createActivity = new CreateActivityController(bean);
            	curr = createActivity.createActivity();
            	
            	nearbyActivities.add(curr);
            }
            
            rs.close();
        	
        }finally {
            // STEP 5.2: Clean-up dell'ambiente
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            	throw(se2);
            }
            try {
                if (conn != null)
                    conn.close();
                	System.out.println("Disconnetted database successfully...");
                	
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        
        return nearbyActivities;
	}
}
