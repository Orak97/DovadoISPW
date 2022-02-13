package test;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import logic.controller.AddActivityToScheduleController;
import logic.model.DAOPartner;
import logic.model.Log;
import logic.model.Partner;
import logic.model.ScheduleBean;
import logic.model.User;

/*
 *  @author Saverio Procopio
 */

@TestMethodOrder(OrderAnnotation.class)
public class TestAddActivityToScheduleController {
	
	private static final int partnerAuth = 16; //partner proprietario
	private static final int partnerNotAuth = 22; //partner NON proprietario
	private static final String couponToRedeem = "560926";
	private static final Long idNormalActivity = 4L;
	
	/* TEST REDEEM COUPON */
	
	@Test
	@Order(1)
	void testRedeemCouponFailsNotActivityOwner() {
		/*Se un partner non è il proprietario dell'attività per cui il coupon è stato generato 
		 * voglio evitare che sia in grado di riscattarlo
		*/
		
		Partner notAuthorizedPartner;
		try {
			notAuthorizedPartner = DAOPartner.getInstance().getPartnerInfo(partnerNotAuth);
			AddActivityToScheduleController controller = new AddActivityToScheduleController(notAuthorizedPartner,couponToRedeem);
			Assertions.assertThrows(SQLException.class, ()->controller.redeemCoupon());		
		} catch (ClassNotFoundException | SQLException e) {
			Log.getInstance().getLogger().info("error on retrieving the partner :"+e);
			e.printStackTrace();
		}		
	}
	
	@Test
	@Order(2)
	void testAddCertifiedActivityToScheduleFailsNotCert() {
		/*voglio evitare che gli utenti possano aggiungere attività non certificate
		* allo schedulo come se fossero certificate, quindi ad esempio generando coupon per quest'ultime
		* che non avrebbero nessuno che le riscatta
		*/
		
		User explorer = new User(null,null,15L,200L);
		ScheduleBean bean = new ScheduleBean();
		bean.setScheduledDate("2022-02-12");
		bean.setScheduledTime("10:12");
		bean.setIdActivity(idNormalActivity); //attività che si fa tutti i fine settimana -> sabato 12 è ok
		AddActivityToScheduleController controller = new AddActivityToScheduleController(explorer,bean);
		Assertions.assertThrows(SQLException.class, ()->controller.addCertifiedActivityToSchedule());		
	}
	
	
	@Test
	@Order(3)
	void testRemoveScheduleFailsNotOwner() {
		/*voglio evitare che un utente possa cancellare un'attività schedulata 
		* nel caso in cui lui non sia l'utente proprietario dello schedulo 
		*/
		
		User explorer = new User(null,null,18L,200L);
		ScheduleBean bean = new ScheduleBean();
		bean.setScheduleToRemove(36L);
		AddActivityToScheduleController controller = new AddActivityToScheduleController(explorer,bean);
		Assertions.assertThrows(SQLException.class, ()->controller.removeSchedule());
	}

}
