package logic.controller;

import logic.model.DAOExplorer;
import logic.model.DAOSchedules;
import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.LogBean;
import logic.model.SuperUser;
import logic.model.User;


public class LogExplorerController {
private DAOExplorer dao;

	public LogExplorerController(){
	dao=DAOExplorer.getInstance();	
	}

	
	public User loginExplorer(LogBean bean) throws Exception {
		User u = dao.login(bean.getEmail(), bean.getPassword());
		if (u == null) {
			Log.getInstance().getLogger().info("DB returned null explorer");
			return u;
		}
		u.setSchedule(DAOSchedules.getInstance().getSchedule(u.getUserID()));
		return u;
	}
}
