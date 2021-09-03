package logic.model;

import java.time.LocalDateTime;

public interface Activity {
	void playActivity(User u);
	
	boolean playableOnThisDate(LocalDateTime timestamp);
	
	Channel getChannel();
}
