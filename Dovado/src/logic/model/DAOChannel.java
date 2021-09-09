package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import logic.controller.FindActivityController;

public class DAOChannel {
	private static DAOChannel INSTANCE;
	private JSONParser parser; 

	private static final  String CHANNJSON = "WebContent/channels.json";
	private static final  String CHANNKEY = "channels";
	private static final  String ACTIVITYKEY = "activity";
	private static final  String MTXTKEY = "messtxt";
	private static final  String MESSKEY = "messages";
	private static final  String UIDKEY = "userID";
	private static final  String DATESENTKEY = "datesent";
	
	
	
	private DAOChannel() {
		parser = new JSONParser();
	}
	
	public static DAOChannel getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOChannel();
		return INSTANCE;
	}
	
	//Metodo per aggiornare nella persistenza un canale
		public boolean updateChannelInJSON(List<Message> msges, SuperActivity a) {
			JSONParser pars = new JSONParser(); 
			
			try
			{
				
				Object channels = pars.parse(new FileReader(CHANNJSON));
				JSONObject channel = (JSONObject) channels;
				JSONArray channelArray = (JSONArray) channel.get(CHANNKEY);
				JSONArray newMessageArray = new JSONArray();
				JSONObject result;
				
				for(int j=0; j<channelArray.size();j++) {
					result = (JSONObject)channelArray.get(j);
					
					Long aID = (Long) result.get(ACTIVITYKEY);
					Log.getInstance().getLogger().info("id attività update:"+ aID);

					if(Long.compare(a.getId(), aID)==0) {
						
						for(int i=0;i<msges.size();i++) {
						//Scrivo il messaggio in JSONObject e lo inserisco nell'
						//array dei messaggi.
							JSONObject message = new JSONObject();
							
							message.put(UIDKEY,msges.get(i).getUsr());
							message.put(MTXTKEY,msges.get(i).getMsgText());
							message.put(DATESENTKEY,msges.get(i).getMsgSentDate());
							
							newMessageArray.add(message);
							
						}
						
						result.remove(MESSKEY);
						result.put(MESSKEY, newMessageArray);
						
						try (FileWriter file = new FileWriter(CHANNJSON)) {	
							file.write(channel.toString());
							file.flush();
						}
						return true;
					}
				}
				
				return false;
									
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				}
		
		}

		//Metodo per aggiungere nella persistenza un canale
		public Channel setupChannelJSON(Long aID) {
			JSONParser pars = new JSONParser();
			Channel foundCh = null;
			try 
			{
				
				Object channels = pars.parse(new FileReader(CHANNJSON));
				JSONObject channel = (JSONObject) channels;
				JSONArray channelArray = (JSONArray) channel.get(CHANNKEY);
				
				if((foundCh = findChannel(aID))!=null) {
					Log.getInstance().getLogger().info("Il canale è già presente nella persistenza.");
					return foundCh;
				}
				
				Log.getInstance().getLogger().info("Il canale non è presente nella persistenza, pertanto sarà aggiunto.");
				
				JSONObject newChannel = new JSONObject();
				JSONArray newMessageArray = new JSONArray();
				
				newChannel.put(ACTIVITYKEY, aID);
				
				/*Se il canale dovesse essere nullo e quindi appena creato
				 * vado a inserire l'array json di messaggi, vuoto, all'
				 * interno dell'oggetto json del canale e aggiungerlo ai 
				 * canali già esistenti.*/
				newChannel.put(MESSKEY,newMessageArray);
				channelArray.add(newChannel);
				try (FileWriter file = new FileWriter(CHANNJSON)){	
					file.write(channel.toString());
					file.flush();
				}
				foundCh = new Channel(aID);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
				}

			return foundCh;
		}


	public Channel findChannel(Long activityId) {
		Channel chFound=null;
		Long aID;
		try {
			Object channels = parser.parse(new FileReader(CHANNJSON));
			JSONObject channelObj = (JSONObject) channels;
			JSONArray channelArray = (JSONArray) channelObj.get(CHANNKEY);
			JSONObject result;
			JSONArray resultMss;
				
			for(int i=0; i<channelArray.size();i++) {
				//Recupero dall'array di canali ciascun canale uno alla volta.
				result = (JSONObject)channelArray.get(i);
				
				//Prendo l'id trovato nel JSON
				aID = (Long) result.get(ACTIVITYKEY);
				Log.getInstance().getLogger().info("id attività trovata nel json find:"+ aID + " id attività cercata "+ activityId);
				
				//Se il JSON trovato ha lo stesso id dell'attività
				if(Long.compare(aID, activityId) == 0) {
					//Ricostruisco il canale compresi i messaggi ad esso collegati.
					resultMss = (JSONArray) result.get(MESSKEY);
					chFound = new Channel(aID);
					Log.getInstance().getLogger().info("Ricostruendo l'istanza di canale dalla persistenza.");
					//Per ora faccioo che ogni messaggio della chat viene 
					//inserito uno alla volta all'interno della nuova istanza del canale.
					for(int j=0;j<resultMss.size();j++) {
						JSONObject mss = (JSONObject)resultMss.get(j);
						Log.getInstance().getLogger().info("\n\nMessaggio scritto: "+mss.get(MTXTKEY)+"\n\n");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy'  'HH:mm");
						
						chFound.addMsg((Long)mss.get(UIDKEY), (String)mss.get(MTXTKEY),LocalDateTime.parse((String)mss.get(DATESENTKEY),dtf));
					}
					return chFound;
				}
				
			}
			
			/*Se non ho trovato niente, restituisco null.*/
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return null;
			}
		return null;
	}
}
