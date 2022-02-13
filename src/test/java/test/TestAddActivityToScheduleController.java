package test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import logic.controller.AddActivityToScheduleController;
import logic.model.DAOPartner;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Schedule;
import logic.model.ScheduleBean;
import logic.model.ScheduledActivity;
import logic.model.User;

/*
 *  @author Saverio Procopio
 */

@TestMethodOrder(OrderAnnotation.class)
public class TestAddActivityToScheduleController {
	
	private static final int partnerAuth = 16; //partner proprietario
	private static final int partnerNotAuth = 22; //partner NON proprietario
	private static String couponToRedeem;
	private static final Long idNormalActivity = 4L;
	private static final Long idCertifiedActivity = 6L;
	private static Long myScheduleId = null;
	
	private ScheduleBean bean;
	
	
	public TestAddActivityToScheduleController() {
		 bean = new ScheduleBean();
		 bean.setScheduledDate("2022-03-17");
		 bean.setScheduledTime("10:12");
		 
	}
	
	/* test Add activity to schedule */
	
	@Test
	@Order(1)
	void testAddCertifiedActivityToScheduleFailsNotCert() {
		/*voglio verificare che gli utenti non possano aggiungere attività normali nello schedulo come se fossero certificate
		* evitando quindi di generare coupon per attività normali che non avrebbero nessuno che lo riscatta
		*/
		
		User explorer = new User(null,null,15L,200L);
		bean.setIdActivity(idNormalActivity); //attività che si fa tutti i fine settimana -> sabato 12 è ok
		AddActivityToScheduleController controller = new AddActivityToScheduleController(explorer,bean);
		Assertions.assertThrows(SQLException.class, () -> controller.addCertifiedActivityToSchedule());		
	}
	
	@Test
	@Order(2)
	void testAddCertifiedActivityToScheduleSuccess() {
		/*voglio verificare che gli utenti siano in grado di aggiungere attività certificate
		* allo schedulo, generando un coupon per quest'ultime
		*/
		
		User explorer = new User(null,null,15L,200L);
		bean.setIdActivity(idCertifiedActivity); //attività che si fa tutti i fine settimana -> sabato 12 è ok
		AddActivityToScheduleController controller = new AddActivityToScheduleController(explorer,bean);
		try {
			controller.addCertifiedActivityToSchedule();
			Schedule s = explorer.getSchedule();
			ArrayList<ScheduledActivity> sActivities = (ArrayList<ScheduledActivity>) s.getScheduledActivities();
			ScheduledActivity mySchedule = null;
			
			for(ScheduledActivity curr: sActivities) {
				if(curr.getReferencedActivity().getId() == idCertifiedActivity && curr.getScheduledTime().isEqual(bean.getScheduledDateTime())) mySchedule = curr;
			}
			
			if(mySchedule != null) couponToRedeem = String.valueOf(mySchedule.getCoupon().getCouponCode());
			System.out.println("il coupon è"+couponToRedeem);
			assertNotNull(mySchedule);
			
		} catch (ClassNotFoundException | SQLException e) {
			Log.getInstance().getLogger().info("error durate la generazione del coupon:"+e);
			e.printStackTrace();
		}		
	}
	
	@Test
	@Order(3)
	void testAddNormalActivityToScheduleSuccess() {
		/*voglio verificare che gli utenti possano aggiungere attività normali
		* allo schedulo
		*/
		
		User explorer = new User(null,null,15L,200L);
		bean.setIdActivity(idNormalActivity); //attività che si fa tutti i fine settimana -> sabato 12 è ok
		AddActivityToScheduleController controller = new AddActivityToScheduleController(explorer,bean);
		try {
			controller.addActivityToSchedule();
			Schedule s = explorer.getSchedule();
			ArrayList<ScheduledActivity> sActivities = (ArrayList<ScheduledActivity>) s.getScheduledActivities();
			ScheduledActivity mySchedule = null;
			
			for(ScheduledActivity curr: sActivities) {
				if(curr.getReferencedActivity().getId() == idNormalActivity && curr.getScheduledTime().isEqual(bean.getScheduledDateTime())) mySchedule = curr;
			}
			
			if(mySchedule != null) myScheduleId = mySchedule.getId();
			
			assertNotNull(mySchedule);
			
		} catch (ClassNotFoundException | SQLException e) {
			Log.getInstance().getLogger().info("error durate la generazione del coupon:"+e);
			e.printStackTrace();
		}		
	}
	
	
	
	@Test
	@Order(4)
	void testRedeemCouponFailsNotActivityOwner() {
		/*Voglio verificare che se un partner non è il proprietario dell'attività per cui il coupon è stato generato 
		 *non sia in grado di riscattarlo
		*/
		
		Partner notAuthorizedPartner;
		try {
			notAuthorizedPartner = DAOPartner.getInstance().getPartnerInfo(partnerNotAuth);
			System.out.println(notAuthorizedPartner);
			AddActivityToScheduleController controller = new AddActivityToScheduleController(notAuthorizedPartner,couponToRedeem);
			Assertions.assertThrows(SQLException.class, () -> controller.redeemCoupon());		
		} catch (ClassNotFoundException | SQLException e) {
			Log.getInstance().getLogger().info("error on retrieving the partner :"+e);
			e.printStackTrace();
		}		
	}
	
	@Test
	@Order(5)
	void testRedeemCouponSuccessActivityOwner() {
		/*Voglio verificare che se il partner è il proprietario dell'attività per cui il coupon è stato generato 
		 * sia in grado di riscattarlo
		*/
		
		Partner authorizedPartner;
		try {
			authorizedPartner = DAOPartner.getInstance().getPartnerInfo(partnerAuth);
			AddActivityToScheduleController controller = new AddActivityToScheduleController(authorizedPartner,couponToRedeem);
			Assertions.assertDoesNotThrow(()->controller.redeemCoupon());		
		} catch (ClassNotFoundException | SQLException e) {
			Log.getInstance().getLogger().info("error on retrieving the partner :"+e);
			e.printStackTrace();
		}		
	}
	
	
	@Test
	@Order(6)
	void testRemoveScheduleFailsNotOwner() {
		/*voglio verificare che un utente non possa cancellare un'attività schedulata 
		* nel caso in cui lui non sia l'utente proprietario dello schedulo
		*/
		
		User explorer = new User(null,null,18L,200L);
		bean.setScheduleToRemove(myScheduleId);
		AddActivityToScheduleController controller = new AddActivityToScheduleController(explorer,bean);
		Assertions.assertThrows(SQLException.class, () -> controller.removeSchedule());
	}
	
	@Test
	@Order(7)
	void testRemoveScheduleSuccessOwner() {
		/*voglio verificare che un utente possa cancellare un'attività schedulata 
		* nel caso in cui lui sia l'utente proprietario dello schedulo 
		*/
		
		User explorer = new User(null,null,15L,200L);
		bean.setScheduleToRemove(myScheduleId);
		AddActivityToScheduleController controller = new AddActivityToScheduleController(explorer,bean);
		Assertions.assertDoesNotThrow(()-> controller.removeSchedule());
	}

}
