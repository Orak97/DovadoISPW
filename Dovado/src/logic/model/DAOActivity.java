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
	
	private static final String USER = "dovado"; //DA CAMBIARE
	private static final String PASSWORD = "dovadogang"; //DA CAMBIARE
	private static final String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
			
	//----------log message-----------------------------------------------
	private static final String LOGDBCONN = "Connected database successfully...";
	private static final String LOGDBDISCONN = "Disconnetted database successfully...";
	
	//--------------------------------------------------------------
	private  Connection conn ;
	private  CallableStatement stmt;
	
	private DAOActivity() {
	}
	
	public static DAOActivity getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOActivity();
		return INSTANCE;
	}
        
	public void createNormalActivity(CreateActivityBean bean) throws SQLException, ClassNotFoundException {
		//metodo per creare nel db una classe Activity      
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call create_activity(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            
            //TODO proprietario == null da modificare
            
            stmt = conn.prepareCall(call);
           
            stmt.setString(1,bean.getActivityName());
            stmt.setString(2,bean.getActivityDescription());
            stmt.setString(3,bean.getSite());
            stmt.setString(4,bean.getPrice());
            stmt.setInt(5,bean.getPlace());
            stmt.setString(6,bean.getOwner() > 0 ? String.valueOf(bean.getOwner()) : null );
            stmt.setBoolean(7,bean.isArte());
            stmt.setBoolean(8,bean.isCibo());
            stmt.setBoolean(9,bean.isMusica());
            stmt.setBoolean(10,bean.isSport());
            stmt.setBoolean(11,bean.isSocial());
            stmt.setBoolean(12,bean.isNatura());
            stmt.setBoolean(13,bean.isEsplorazione());
            stmt.setBoolean(14,bean.isRicorrenze());
            stmt.setBoolean(15,bean.isModa());
            stmt.setBoolean(16,bean.isShopping());
            stmt.setBoolean(17,bean.isAdrenalina());
            stmt.setBoolean(18,bean.isMonumenti());
            stmt.setBoolean(19,bean.isRelax());
            stmt.setBoolean(20,bean.isIstruzione());
            stmt.setString(21,bean.getType().name());
            stmt.setString(22,bean.getOpeningLocalTime().toString());
            stmt.setString(23,bean.getClosingLocalTime().toString());
            stmt.setString(24,bean.getOpeningDate() != null ? bean.getOpeningLocalDate().toString() : null);
            stmt.setString(25,bean.getEndDate() != null ? bean.getEndLocalDate().toString() : null);
            stmt.setString(26,bean.getCadence()!=null ? bean.getCadence().name() : null);
            
            stmt.execute();
            
        }finally {
        	//Clean-up dell'ambiente
            disconnRoutine();
        }
        
		
	}
	
	public ArrayList<Activity> getNearbyActivities(double userLat,double userLong, float maxDistance) throws ClassNotFoundException, SQLException {
		//metodo per ottenere TUTTE le attività entro una maxDistance(Km) partendo da un punto di coordinate geografiche(userLat,userLong)
		
		// STEP 1: dichiarazioni
        
        ArrayList<Activity> nearbyActivities = new ArrayList<>();
        
        try {
        	
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_nearby_activities(?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setFloat(1, maxDistance);
            stmt.setDouble(2, userLat);
            stmt.setDouble(3, userLong);
            
            if(!stmt.execute()) {
            	 throw new SQLException("Sembra che non esistano attività vicine a te");
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	Activity curr;
            	
            	//NOTA: andre questo puoi copiarlo e incollarlo quando devi riempire il bean nella view per salvare un attività
            	CreateActivityBean bean = fillBean(rs);
            	
            	CreateActivityController createActivity = new CreateActivityController(bean);
            	curr = createActivity.createActivity();
            	
            	nearbyActivities.add(curr);
            }
            
            rs.close();
        	
        }finally {
        	//Clean-up dell'ambiente
            disconnRoutine();
        }
        
        return nearbyActivities;
	}
	
	public Activity getActivityById(Long id) throws SQLException, ClassNotFoundException{
		//metodo per ottenere un attività entro una maxDistance(Km) partendo dal suo id
		
		// STEP 1: dichiarazioni
        
        
       Activity act = null;
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_activity_by_id(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1, id);
            
            if(!stmt.execute()) {	
            	throw new SQLException("Sembra che non esistano attività con questo ID");
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {    	
            	//NOTA: andre questo puoi copiarlo e incollarlo quando devi riempire il bean nella view per salvare un attività
            	CreateActivityBean bean = fillBean(rs);
            	
            	
            	CreateActivityController createActivity = new CreateActivityController(bean);
            	act = createActivity.createActivity();
            }
            
            rs.close();
        	
        }finally {
        	//Clean-up dell'ambiente
            disconnRoutine();
        }
        
        return act;
	}
	
	public ArrayList<CertifiedActivity> getPartnerActivities(Long idPartner) throws ClassNotFoundException, SQLException {
		//metodo per ottenere TUTTE le attività entro una maxDistance(Km) partendo da un punto di coordinate geografiche(userLat,userLong)
		        
        ArrayList<CertifiedActivity> partnerActivities = new ArrayList<>();
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_partner_activities(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1, idPartner);
            
            if(!stmt.execute()) {
            	throw new SQLException("Sembra che non esistano attività per questo partner");
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	Activity curr;
            	
            	//NOTA: andre questo puoi copiarlo e incollarlo quando devi riempire il bean nella view per salvare un attività
            	CreateActivityBean bean = fillBean(rs);
            	
            	CreateActivityController createActivity = new CreateActivityController(bean);
            	curr = createActivity.createActivity();
            	
            	partnerActivities.add((CertifiedActivity) curr);
            }
            
            rs.close();
        	
        }finally {
        	//Clean-up dell'ambiente
            disconnRoutine();
        }
        
        return partnerActivities;
	}
	
	public void updateCertAcivity(CertifiedActivity activity) throws ClassNotFoundException, SQLException {
	        //Lo statment dell'altra store procedure lo inizializzo nel modulo resetConnection
			CallableStatement stmtPref = null;
	         
	        try {
	        	resetConnection();
	           
	            
	            //STEP4.1: preparo le stored procedure
	            String call =  "{call update_cert_activity(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	            String callPref =  "{call update_pref_activity(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	            
	            //effettuo la prima Store procedure 
	            stmt = conn.prepareCall(call);
	            
	            stmt.setInt(1, activity.getId() != null ? activity.getId().intValue() : null);
	            stmt.setInt(2,activity.getOwner().getUserID() != null ? activity.getOwner().getUserID().intValue() : null);
	            stmt.setString(3,activity.getName());
	            stmt.setString(4,activity.getDescription());
	            stmt.setString(5,activity.getSite());
	            stmt.setString(6,activity.getPrice());
	            stmt.setInt(7,activity.getPlace().getId().intValue());
	            
	            String type = null;
	            String cadence = null;
	            String startDate = null;
	            String endDate = null;
	            
	            if (activity.getFrequency() instanceof ContinuosActivity) {
	            	type = "CONTINUA";
	            } else if(activity.getFrequency() instanceof PeriodicActivity) {
	            	type = "PERIODICA";
	            	cadence = ((PeriodicActivity)activity.getFrequency()).getCadence().name();
	            	startDate = ((PeriodicActivity)activity.getFrequency()).getStartDate().toString();
	            	endDate = ((PeriodicActivity)activity.getFrequency()).getEndDate().toString();
	            	
	            } else if(activity.getFrequency() instanceof ExpiringActivity) {
	            	type = "SCADENZA";
	            	startDate = ((ExpiringActivity)activity.getFrequency()).getStartDate().toString();
	            	endDate = ((ExpiringActivity)activity.getFrequency()).getEndDate().toString();
	            }

	            stmt.setString(8,type);
	            stmt.setString(9,activity.getFrequency().getOpeningTime().toString());
	            stmt.setString(10,activity.getFrequency().getClosingTime().toString());
	            stmt.setString(11,startDate);
	            stmt.setString(12,endDate);
	            stmt.setString(13,cadence);
	            
	            stmt.execute();
	            
	            //effettuo la seconda store procedure
	            stmtPref = conn.prepareCall(callPref);
	            
	            stmtPref.setInt(1, activity.getId() != null ? activity.getId().intValue() : null);
	            stmtPref.setInt(2,activity.getOwner().getUserID() != null ? activity.getOwner().getUserID().intValue() : null);
	            stmtPref.setBoolean(3,activity.getIntrestedCategories().isArte());
	            stmtPref.setBoolean(4,activity.getIntrestedCategories().isCibo());
	            stmtPref.setBoolean(5,activity.getIntrestedCategories().isMusica());
	            stmtPref.setBoolean(6,activity.getIntrestedCategories().isSport());
	            stmtPref.setBoolean(7,activity.getIntrestedCategories().isSocial());
	            stmtPref.setBoolean(8,activity.getIntrestedCategories().isNatura());
	            stmtPref.setBoolean(9,activity.getIntrestedCategories().isEsplorazione());
	            stmtPref.setBoolean(10,activity.getIntrestedCategories().isRicorrenzeLocali());
	            stmtPref.setBoolean(11,activity.getIntrestedCategories().isModa());
	            stmtPref.setBoolean(12,activity.getIntrestedCategories().isShopping());
	            stmtPref.setBoolean(13,activity.getIntrestedCategories().isAdrenalina());
	            stmtPref.setBoolean(14,activity.getIntrestedCategories().isMonumenti());
	            stmtPref.setBoolean(15,activity.getIntrestedCategories().isRelax());
	            stmtPref.setBoolean(16,activity.getIntrestedCategories().isIstruzione());

	            
	            //TODO Chiedere a Sav se necessario il throw
	        }finally {
	            // STEP 5.2: Clean-up dell'ambiente
	            try {
	                if (stmt != null)
	                    stmt.close();
	                if (stmtPref != null)
	                    stmtPref.close();
	            } catch (SQLException se2) {
	            	Log.getInstance().getLogger().warning("Errore di codice: "+ se2.getErrorCode() + " e mesaggio: " + se2.getMessage());
	            }
	            try {
	                if (conn != null)
	                    conn.close();
	                Log.getInstance().getLogger().info(LOGDBDISCONN);
	                	
	            } catch (SQLException se) {
	            	Log.getInstance().getLogger().warning("Errore di codice: "+ se.getErrorCode() + " e mesaggio: " + se.getMessage());
	                se.printStackTrace();
	            }
	        }
		}
		
	
	
	public ArrayList<Activity> findActivitiesByZone(String zone) throws ClassNotFoundException, SQLException {
		//metodo per ottenere TUTTE le attività partendo da una zona (CAP, Città, Regione)
        
        ArrayList<Activity> searchedActivities = new ArrayList<>();
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_activities_by_zone(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1, zone);
            
            if(!stmt.execute()) {
            	throw new SQLException("Sembra che non esistano attività in questa zona!");
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	Activity curr;
            	
            	//NOTA: andre questo puoi copiarlo e incollarlo quando devi riempire il bean nella view per salvare un attività
            	CreateActivityBean bean = fillBean(rs);
            	
            	CreateActivityController createActivity = new CreateActivityController(bean);
            	curr = createActivity.createActivity();
            	
            	searchedActivities.add(curr);
            }
            
            rs.close();
        	
        }finally {
        	//Clean-up dell'ambiente
            disconnRoutine();
        }
        
        return searchedActivities;
	}
	
	public ArrayList<Discount> viewDiscounts (Long idActivity) throws ClassNotFoundException, SQLException {
		// STEP 1: dichiarazioni
       
        ArrayList<Discount> scontiDisponibili = new ArrayList<>();
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call view_discounts(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1, idActivity);
            
            if(!stmt.execute()) {
            	throw new SQLException("Sembra che non esistono Coupon per questa attività");
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	int percentuale = rs.getInt("percentuale");
            	boolean abilitato = rs.getBoolean("abilitato");
            	int prezzo = rs.getInt("prezzo");
            	
            	Discount sconto = new Discount(percentuale, abilitato, prezzo);
            	
            	scontiDisponibili.add(sconto);
            }
            
            rs.close();
            
        
        }finally {
        	//Clean-up dell'ambiente
            disconnRoutine();
        }
		return scontiDisponibili;
	}	
	
	public void claimActivity(Long activity, Long partner) throws ClassNotFoundException, SQLException {
		// STEP 1: dichiarazioni
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call claim_activity(?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1, activity);
            stmt.setLong(2, partner);
            
            stmt.execute();
        	
        }finally {
            //Clean-up dell'ambiente
            disconnRoutine();
        }
        
	}
	
	private void resetConnection() throws ClassNotFoundException, SQLException {
		conn = null;
		stmt = null;

		Class.forName(DRIVER_CLASS_NAME);

		// apertura connessione
        conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Log.getInstance().getLogger().info(LOGDBCONN);
	}
	
	private void disconnRoutine() throws SQLException {
		
		try {
	        if (stmt != null)
	            stmt.close();
	    } catch (SQLException se2) {
	    	Log.getInstance().getLogger().warning("Errore di codice: "+ se2.getErrorCode() + " e mesaggio: " + se2.getMessage());
	    	se2.printStackTrace();
	    	throw se2;
	    }
	    try {
	        if (conn != null)
	            conn.close();
	    	Log.getInstance().getLogger().info(LOGDBDISCONN);

	    } catch (SQLException se) {
	    	Log.getInstance().getLogger().warning("Errore di codice: "+ se.getErrorCode() + " e mesaggio: " + se.getMessage());
	    	se.printStackTrace();
	    }
	}
	
	public CreateActivityBean fillBean(ResultSet rs) throws SQLException {
	        	
           	//NOTA: andre questo puoi copiarlo e incollarlo quando devi riempire il bean nella view per salvare un attività
        	CreateActivityBean bean = new CreateActivityBean();
        	
        	bean.setIdActivity(rs.getInt("id"));
        	
        	bean.setActivityName(rs.getString("nome"));
        	bean.setActivityDescription(rs.getString("descrizione"));
        	bean.setPlace(rs.getInt("luogo"));
        	
        	
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
        	bean.setMonumenti(rs.getBoolean("monumenti"));
        	
        	bean.setOwner(rs.getInt("proprietario"));
        	
        	return bean;

	}
}
