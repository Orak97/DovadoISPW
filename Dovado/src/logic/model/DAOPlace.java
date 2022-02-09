package logic.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOPlace extends DAO {
	
	private static DAOPlace INSTANCE;
		
	private DAOPlace() {
	}
	
	public static DAOPlace getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOPlace();
		return INSTANCE;
	}
	
	
	public int spotPlace(String address, String placeName, String city, String region, String civico, String cap, double[] coord) throws ClassNotFoundException, SQLException {
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call create_place(?,?,?,?,?,?,?,?)}";
            
            
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,placeName);
            stmt.setString(2,address);
            stmt.setString(3,city);
            stmt.setString(4,region);
            stmt.setString(5,civico);
            stmt.setString(6,cap);
            stmt.setDouble(7, coord[0]);//lat
            stmt.setDouble(8, coord[1]);
              
            stmt.execute();
            
        }finally {
        	//Clean-up dell'ambiente
        	disconnRoutine();
        }
        
        
		return 1;
	}
	
	public List<Place> searchPlaces(String str) throws ClassNotFoundException, SQLException {
		        
        ArrayList<Place> places = new ArrayList<>();
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call search_places(?)}";
            
            stmt = conn.prepareCall(call);
            stmt.setString(1,str);
            
            if(!stmt.execute()) {
            	throw new SQLException("Sembra che non esista nessun luogo per: "+str);
            }
            
            ResultSet rs = stmt.getResultSet();
            
            
            while(rs.next()) {
            	Place p = new Place(fillBean(rs));
            	places.add(p);
            }
            
            rs.close();
        }
        finally {
        	//Clean-up dell'ambiente
        	disconnRoutine();
        }
        
        return places;
	}

	
	public Place getPlace(int id) throws ClassNotFoundException, SQLException  {
        
        PlaceBean searchedPlace = null;
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call search_place_by_id(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setInt(1,id);
            
            if(!stmt.execute()) {
            	//NOTA: restituisce true solo è un result set, quindi non usare per le operazioni CURD!!!
            	throw new SQLException("Sembra che non esista nessun luogo per l'id: "+id);
            }
            
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	searchedPlace = fillBean(rs);
            }
            if(searchedPlace == null) {
             	Log.getInstance().getLogger().warning("STAROBA E NULLLLLLLLL BROOOOOOOOOOOOOOOOOOOOOOOOOOO!!!!!");
            }
            rs.close();
        }finally {
        	//Clean-up dell'ambiente
        	disconnRoutine();
        }
        
        return new Place(searchedPlace);
	}
	
	private PlaceBean fillBean(ResultSet rs) throws SQLException {
		PlaceBean searchedPlace = new PlaceBean();
        	
		searchedPlace.setId(rs.getLong("idLuogo"));
		searchedPlace.setName(rs.getString("nome"));
		searchedPlace.setAddress(rs.getString("indirizzo"));
		searchedPlace.setCity(rs.getString("citta"));
		searchedPlace.setRegion(rs.getString("regione"));
		searchedPlace.setCivico(rs.getString("civico"));
		searchedPlace.setCap(rs.getString("cap"));
		searchedPlace.setLatitudine(rs.getFloat("latitudine"));
		searchedPlace.setLongitudine(rs.getFloat("longitudine"));
			
		return searchedPlace;
	}
	
}