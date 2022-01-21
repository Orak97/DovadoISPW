package logic.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ContinuosActivity extends FrequencyOfRepeat {

	public ContinuosActivity(LocalTime openingTime, LocalTime closingTime) {
		super(openingTime, closingTime);
	}

	@Override
	public boolean checkPlayability(LocalDateTime timestamp) {
		return this.isOnTime(timestamp);
	}

	@Override
	public boolean checkDate(LocalDate date) {
		return true;
	}

}
