package logic.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOExplorer extends DAO{
	private static DAOExplorer instance;		
	
	public static DAOExplorer getInstance() {
		if(instance == null)
			instance = new DAOExplorer();
		return instance;
	}
	
	public User login(String email, String password) throws SQLException, ClassNotFoundException  {

        User u = null;
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call login_explorer(?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,email);
            stmt.setString(2, password);
            
            if(!stmt.execute()) {
            	Log.getInstance().getLogger().warning("user non trovato");
            }
            
            ResultSet rs = stmt.getResultSet();
            
            u = setExplorer(rs);
            
            rs.close();
            
        } finally {
        	//Clean-up dell'ambiente
        	disconnRoutine();
        } return u;
	}
	public User getExpInfo(int id) throws SQLException, ClassNotFoundException {
		User u = null;
		
		try {
			resetConnection();
			
			String sql = "select idUtente as id, username,email, wallet,Arte,Cibo,Musica,Sport,Social,Natura,Esplorazione,Ricorrenze_locali,Moda,Shopping,Adrenalina,Monumenti,Relax,Istruzione  from dati_esploratore where (idUtente = (?));";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getResultSet();
			
			u = setExplorer(rs);
            
            
            rs.close();
			
			
			
		}finally {
        	//Clean-up dell'ambiente
        	disconnRoutine();
        } return u;
	
	}
	
	private User setExplorer(ResultSet rs) throws SQLException {
		User u= null;
		
		while(rs.next()) {
            u = new User(
            		rs.getString("username"),
            		rs.getString("email"),
            		rs.getLong("id"),
            		rs.getLong("wallet"));
                      
            boolean[] boolPref= {
            		rs.getBoolean("Arte"),
            		rs.getBoolean("Cibo"),
            		rs.getBoolean("Musica"),
            		rs.getBoolean("Sport"),
            		rs.getBoolean("Social"),
            		rs.getBoolean("Natura"),
            		rs.getBoolean("Esplorazione"),
            		rs.getBoolean("Ricorrenze_locali"),
            		rs.getBoolean("Moda"),
            		rs.getBoolean("Shopping"),
            		rs.getBoolean("Adrenalina"),
            		rs.getBoolean("Relax"),
            		rs.getBoolean("Istruzione"),
            		rs.getBoolean("Monumenti")
            	};
            Preferences p = new Preferences(boolPref);
            
            
            u.setPreferences(p);
        }
        
      return u;
		
	}
	public void registerExplorer(String username, String email, String password, boolean[] pref) throws ClassNotFoundException, SQLException{
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call create_explorer(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,username);
            stmt.setString(2,email);
            stmt.setString(3,password);
            for (int i = 0; i < pref.length; i++) {
				stmt.setBoolean(i+4, pref[i]);
			}
            
            
            stmt.execute();
        }finally {
        	//Clean-up dell'ambiente
        	disconnRoutine();
        }
	}
	
	public int getUserWallet(Long userID) throws ClassNotFoundException, SQLException{
		
        int wallet = 0;
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call get_wallet(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1,userID);
            
            if(!stmt.execute()) {   	
            	throw new SQLException("Nessun portafogli per questo utente");
            	}
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	wallet = rs.getInt("wallet");
            }
            
            rs.close();
        }finally{
            //Clean-up dell'ambiente
        	disconnRoutine();
        }
    
        return wallet;
	}
	
	
}
