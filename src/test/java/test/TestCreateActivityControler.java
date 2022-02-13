package test;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import logic.controller.ActivityType;
import logic.controller.CreateActivityController;
import logic.model.CreateActivityBean;
import logic.model.DAOPartner;
import logic.model.Log;
import logic.model.LogBean;
import logic.model.NormalActivity;
import logic.model.Activity;
import logic.model.CertifiedActivity;

/*
*	@author Andrea Paganelli
*/

@TestMethodOrder(OrderAnnotation.class)
public class TestCreateActivityControler {
	

	private CreateActivityBean bean;
	private CreateActivityController controller;		
	
	public TestCreateActivityControler(){
		new LogBean();
		this.bean = new CreateActivityBean();
	}
	
	/* TEST CREATE NORMAL ACTIVITY */
	@Test
	@Order(1)
	void testCreateNormalActivitySuccesful(){
		boolean[] emptyPreferences = {false,false,false,false,false,false,false,false,false,false,false,false,false,false};
		
		
		try {
			bean.setActivityDescription("Test");
			bean.setActivityName("Test normal activity");
			bean.setPreferences(emptyPreferences);
			bean.setOwner(0);
			bean.setType(ActivityType.CONTINUA);
			bean.setPrice(null);
			bean.setPlace(1);
			bean.setClosingTime("20:20");
			bean.setOpeningTime("10:00");
			
			controller = new CreateActivityController(bean);
			Assertions.assertTrue(controller.createActivity() instanceof NormalActivity);			
		} catch(SQLException | ClassNotFoundException e) {
			Log.getInstance().getLogger().info("testCreateActivity SHOULD NOT throw any exception "+e);
		}
	}
	
	/*TEST CERTIFIED ACTIVITY SUCCESFULL*/
	
	@Test
	@Order(2)
	void testCreateCertifiedActivitySuccesfull() {
		boolean[] emptyPreferences = {false,false,false,false,false,false,false,false,false,false,false,false,false,false};
		
		
		try {
			bean.setActivityDescription("Test certificata");
			bean.setActivityName("Test certificata activity");
			bean.setPreferences(emptyPreferences);
			bean.setOwner(16);
			bean.setType(ActivityType.CONTINUA);
			bean.setPrice("10");
			bean.setPlace(1);
			bean.setClosingTime("20:20");
			bean.setOpeningTime("10:00");

			controller = new CreateActivityController(bean,DAOPartner.getInstance().getPartnerInfo(16));
			Assertions.assertTrue(controller.createActivity() instanceof CertifiedActivity);			
		} catch(SQLException | ClassNotFoundException e) {
			Log.getInstance().getLogger().info("testCreateActivity SHOULD NOT throw any exception "+e);
		}
	}

	
	/*TEST PLACE NOT FOUND EXCEPTION*/
	
	@Test
	@Order(3)
	void testCreateNormalActivityExceptionPlaceNotFound() {
		boolean[] emptyPreferences = {false,false,false,false,false,false,false,false,false,false,false,false,false,false};
		
		bean.setActivityDescription("Test");
		bean.setActivityName("Test normal activity");
		bean.setPreferences(emptyPreferences);
		bean.setOwner(0);
		bean.setType(ActivityType.CONTINUA);
		bean.setPrice("10");
		bean.setPlace(156);
		bean.setClosingTime("20:20");
		bean.setOpeningTime("10:00");
			
		controller = new CreateActivityController(bean);
		Assertions.assertThrows(NullPointerException.class, ()->controller.createActivity());			
	}
	
	

}
