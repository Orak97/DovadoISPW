package logic.controller;

import java.sql.SQLException;

import logic.model.DAOPartner;
import logic.model.Log;
import logic.model.LogBean;
import logic.model.Partner;

public class LogPartnerController {
private DAOPartner dao;

	public LogPartnerController() {
	dao=DAOPartner.getInstance();	
	}
	
	public Partner loginPartner(LogBean bean) throws ClassNotFoundException, SQLException  {
		Partner p = dao.login(bean.getEmail(), bean.getPassword());
		if (p == null) {
			Log.getInstance().getLogger().info("DB returned null partner");			
			return p;
		}
		return p;
	}
}
