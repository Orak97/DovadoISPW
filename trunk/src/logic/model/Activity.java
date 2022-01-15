package logic.model;

import java.time.LocalDateTime;

public interface Activity {
	void playActivity(User u);
	
	boolean playableOnThisDate(LocalDateTime timestamp);
	
	Channel getChannel();
	
	Long getId();
	
	String getName();
	
	Place getPlace();
	
	FrequencyOfRepeat getFrequency();

	void setIntrestedCategories(Preferences intrestedCategories);

	void setChannel(Channel channel);
	
	String getDescription();
}
