package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import logic.controller.CertifiedException;
import logic.controller.CreateActivityController;
import logic.controller.LogExplorerController;
import logic.controller.LogPartnerController;
import logic.controller.NoPartnerException;
import logic.controller.UpdateCertActController;
import logic.model.Activity;
import logic.model.CertifiedActivity;
import logic.model.CreateActivityBean;
import logic.model.DAOActivity;
import logic.model.DAOExplorer;
import logic.model.DAOPartner;
import logic.model.Discount;
import logic.model.DiscountBean;
import logic.model.Log;
import logic.model.Partner;
import logic.model.SuperUser;
import logic.model.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;



@TestMethodOrder(OrderAnnotation.class)
class TestUpdateCertActivityController {
	private static final int USERID = 15;
	private static final Long PARTNER1ID = 16L;
	private static final Long PARTNER2ID = 22L;

	private static final Long CERTACTIVITY = 3L;
	private static final Long NOCERTACTIVITY = 4L;
	private CreateActivityBean bean;
	
	private UpdateCertActController controller;
	
		
	//Controllo che solo il partner possa reclamare un attività
	@Test
	@Order(1)
	void testClaimActivityPartner() {
		try {
			SuperUser u = DAOExplorer.getInstance().getExpInfo(USERID);
			bean= new CreateActivityBean();
			
			controller = new UpdateCertActController(bean, u);
			Assertions.assertThrows(NoPartnerException.class, controller::claimActivity);		
			
		} catch (ClassNotFoundException| NoPartnerException | SQLException e) {
			Log.getInstance().getLogger().warning("testClaimActivityPartner SHOULD NOT THROW exception " + e );
		
		} 
		
	}
	
	//Controllo che solo il partner possa reclamare un attività
		@Test
		@Order(2)
		void testClaimActivitySuccess() {
			try {
				
				DAOActivity.getInstance().resetOwnerForTest(NOCERTACTIVITY.intValue());
				
				SuperUser p = DAOPartner.getInstance().getPartnerInfo(PARTNER1ID.intValue());
				bean= new CreateActivityBean();
				bean.setIdActivity(NOCERTACTIVITY.intValue());
				
				controller = new UpdateCertActController(bean, p);
				
				controller.claimActivity();
				
				
				Activity act = DAOActivity.getInstance().getActivityById(NOCERTACTIVITY);
				Assertions.assertEquals(true , act instanceof CertifiedActivity );		
				
			} catch (ClassNotFoundException| NoPartnerException | SQLException | CertifiedException e) {
				Log.getInstance().getLogger().warning("testClaimActivityPartner SHOULD NOT THROW exception " + e );
			
			} 
			
		}
		
		@Test
		@Order(3)
		void testClaimActivityAlreadyClaimed() {
			try {
				SuperUser p = DAOPartner.getInstance().getPartnerInfo(PARTNER2ID.intValue());
				bean= new CreateActivityBean();
				bean.setIdActivity(NOCERTACTIVITY.intValue());
				controller = new UpdateCertActController(bean, p);
				
				Assertions.assertThrows(CertifiedException.class, controller::claimActivity);	
				
			}  catch (ClassNotFoundException| NoPartnerException | SQLException e) {
				Log.getInstance().getLogger().warning("testClaimActivityAlreadyClaimed SHOULD NOT THROW exception " + e.getMessage()  );
			
			} 
			
		}
	
	//Controllo che funzioni la modifica agli sconti
	@Test
	@Order(4)
	void testModifyDiscount() {
		Partner p = new Partner(PARTNER1ID, null, null);

		
		DiscountBean dBean = new DiscountBean();
		dBean.setActToEdit(CERTACTIVITY.intValue());
		dBean.setDiscount5(false);
		dBean.setDiscount10(false);
		dBean.setDiscount20(true);
		dBean.setDiscount30(false);
		dBean.setDiscount50(false);
		
		
		
		try {
			controller = new UpdateCertActController(dBean,p);
			controller.modifyDiscounts();
			
			Discount discount = DAOActivity.getInstance().viewDiscounts(CERTACTIVITY).get(2);
			
			Assertions.assertEquals(true, discount.isActive());
			
			//ripristiono per i test Successivi poiché ho verificato che funziona 
			
			dBean.setDiscount20(false);
			controller.setDiscountBean(dBean);
			controller.modifyDiscounts();
			
		} catch (ClassNotFoundException | SQLException e) {
			Log.getInstance().getLogger().warning("testModifyDiscount SHOULD NOT THROW exception " + e );

		}
	}
	
	
	
	
}
