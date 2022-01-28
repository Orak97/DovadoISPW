package logic.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Activity {
	void playActivity(User u);
	
	boolean playableOnThisDateTime(LocalDateTime timestamp);
	
	Channel getChannel();
	
	Long getId();
	
	String getName();
	
	Place getPlace();
	
	FrequencyOfRepeat getFrequency();

	void setIntrestedCategories(Preferences intrestedCategories);

	Preferences getIntrestedCategories();
	
	void setChannel(Channel channel);
	
	String getDescription();
	
	boolean isPlayableOnThisDate(LocalDate date);
	
	String getPlayabilityInfo();
}
