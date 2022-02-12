package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import logic.controller.LogExplorerController;
import logic.controller.LogPartnerController;
import logic.controller.UpdateCertActController;
import logic.model.DAOExplorer;
import logic.model.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;



@TestMethodOrder(OrderAnnotation.class)
class TestUpdateCertActivityController {
	private static final int USER = 15;
	
	private UpdateCertActController controller;
	private LogExplorerController logExp;
	private LogPartnerController logPartner;
	
	//Test if claim Activity work
	@Test
	@Order(1)
	void testClaimActivity() {
		try {
			User u = DAOExplorer.getInstance().getExpInfo(USER);
			
			Assertions.assertEquals(350, u.getBalance());
		
		} catch(Exception e){
			System.out.println("Errore nel test");
		}
	}

}
