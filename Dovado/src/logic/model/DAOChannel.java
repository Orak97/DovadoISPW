package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import logic.controller.FindActivityController;

public class DAOChannel {
	private static DAOChannel INSTANCE;
	private JSONParser parser; 
	
	private DAOChannel() {
		parser = new JSONParser();
	}
	
	public static DAOChannel getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOChannel();
		return INSTANCE;
	}
	
	//Metodo per aggiornare nella persistenza un canale
		public boolean updateChannelInJSON(Channel ch, List<Message> msges, SuperActivity a) {
			JSONParser parser = new JSONParser();
			
			try 
			{
				
				Object channels = parser.parse(new FileReader("WebContent/channels.json"));
				JSONObject channel = (JSONObject) channels;
				JSONArray channelArray = (JSONArray) channel.get("channels");
				JSONArray newMessageArray = new JSONArray();
				JSONObject result;
				
				for(int j=0; j<channelArray.size();j++) {
					result = (JSONObject)channelArray.get(j);
					
					Long aID = (Long) result.get("activity");
					System.out.println("id attività update:"+ aID);

					if(Long.compare(a.getId(), aID)==0) {
						
						for(int i=0;i<msges.size();i++) {
						//Scrivo il messaggio in JSONObject e lo inserisco nell'
						//array dei messaggi.
							JSONObject message = new JSONObject();
							
							message.put("userID",msges.get(i).getUsr());
							message.put("messtxt",msges.get(i).getMsgText());
							message.put("datesent",msges.get(i).getMsgSentDate());
							
							newMessageArray.add(message);
							
						}
						
						result.remove("messages");
						result.put("messages", newMessageArray);
						
						
						FileWriter file = new FileWriter("WebContent/channels.json");
						file.write(channel.toString());
						file.flush();
						file.close();
						
						return true;
					}
				}
				
				return false;
									
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;}
			catch (Exception e) {
				e.printStackTrace();
				return false;
				}
		
		}

		//Metodo per aggiungere nella persistenza un canale
		public Channel setupChannelJSON(Long aID) {
			JSONParser parser = new JSONParser();
			Channel foundCh = null;
			try 
			{
				
				Object channels = parser.parse(new FileReader("WebContent/channels.json"));
				JSONObject channel = (JSONObject) channels;
				JSONArray channelArray = (JSONArray) channel.get("channels");
				JSONObject result;
				
				if((foundCh = findChannel(aID))!=null) {
					System.out.println("Il canale è già presente nella persistenza.");
					return foundCh;
				}
				
				System.out.println("Il canale non è presente nella persistenza, pertanto sarà aggiunto.");
				
				JSONObject newChannel = new JSONObject();
				JSONArray newMessageArray = new JSONArray();
				
				newChannel.put("activity", aID);
				
				/*Se il canale dovesse essere nullo e quindi appena creato
				 * vado a inserire l'array json di messaggi, vuoto, all'
				 * interno dell'oggetto json del canale e aggiungerlo ai 
				 * canali già esistenti.*/
				newChannel.put("messages",newMessageArray);
				channelArray.add(newChannel);
					
				FileWriter file = new FileWriter("WebContent/channels.json");
				file.write(channel.toString());
				file.flush();
				file.close();
					
				foundCh = new Channel(aID);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;}
			catch (Exception e) {
				e.printStackTrace();
				return null;
				}

			return foundCh;
		}


	public Channel findChannel(Long activityId) {
		Channel chFound=null;
		Long aID;
		try {
			Object channels = parser.parse(new FileReader("WebContent/channels.json"));
			JSONObject channelObj = (JSONObject) channels;
			JSONArray channelArray = (JSONArray) channelObj.get("channels");
			JSONObject result;
			JSONArray resultMss;
				
			for(int i=0; i<channelArray.size();i++) {
				//Recupero dall'array di canali ciascun canale uno alla volta.
				result = (JSONObject)channelArray.get(i);
				
				//Prendo l'id trovato nel JSON
				aID = (Long) result.get("activity");
				System.out.println("id attività trovata nel json find:"+ aID + " id attività cercata "+ activityId);
				
				//Se il JSON trovato ha lo stesso id dell'attività
				if(Long.compare(aID, activityId) == 0) {
					//Ricostruisco il canale compresi i messaggi ad esso collegati.
					resultMss = (JSONArray) result.get("messages");
					chFound = new Channel(aID);
					System.out.println("Ricostruendo l'istanza di canale dalla persistenza.");
					//Per ora faccioo che ogni messaggio della chat viene 
					//inserito uno alla volta all'interno della nuova istanza del canale.
					for(int j=0;j<resultMss.size();j++) {
						JSONObject mss = (JSONObject)resultMss.get(j);
						System.out.println("\n\nMessaggio scritto: "+mss.get("messtxt")+"\n\n");
						chFound.addMsg((Long)mss.get("userID"), (String)mss.get("messtxt"),LocalDateTime.parse((String)mss.get("datesent")));
					}
					return chFound;
				}
				
			}
			
			/*Se non ho trovato niente, restituisco null.*/
			} catch (NullPointerException e) {
					e.printStackTrace();
					return null;
				} catch (ClassCastException e) {
					e.printStackTrace();
					return null;
				}
				catch (IOException | ParseException e) {
						e.printStackTrace();
						return null;
				}
		return null;
	}
}
