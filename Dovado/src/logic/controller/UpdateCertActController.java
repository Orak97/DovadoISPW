package logic.controller;

import logic.model.CertifiedActivity;
import logic.model.ContinuosActivity;
import logic.model.CreateActivityBean;
import logic.model.DAOActivity;
import logic.model.ExpiringActivity;
import logic.model.Partner;
import logic.model.PeriodicActivity;

public class UpdateCertActController {
	private CertifiedActivity activity;
	private DAOActivity daoAc;
	private CreateActivityBean bean;
	
	/*Rimuovere in caso si sposti il metodo `claimActivity`in un altra classe*/
	private Partner session; 
	
	public UpdateCertActController(CreateActivityBean bean, CertifiedActivity activity) {
		this.activity = activity;
		this.bean = bean;
		daoAc = DAOActivity.getInstance();
	}
	
	/*Rimuovere in caso si sposti il metodo `claimActivity`in un altra classe*/
	public UpdateCertActController(Partner session,CreateActivityBean bean) {
		this.session = session;
		this.bean = bean;
		daoAc = DAOActivity.getInstance();
	}
	/*-----------------------------------------------------------------------*/
	
	public UpdateCertActController(CreateActivityBean bean) {
		this(bean,null);
	}
	
	public boolean updateActivity() throws Exception{
		boolean error = false;
		
		if (activity == null) {
			error = updateByBean();
		} else {
			error = updateWithCheck();
		}
		return error;
	}
	
	public boolean updateByBean() throws Exception{
		CreateActivityController actController = new CreateActivityController(bean);
		//creo l'attività con i parametri aggiornati
		activity = (CertifiedActivity) actController.createActivity();
		//La passo al DAO
		daoAc.updateCertAcivity(activity);
		return true;
	}
	
	public boolean updateWithCheck() throws Exception{
		if(bean.getActivityName() != null) {
			activity.setName(bean.getActivityName());
		}
		if(bean.getActivityDescription() != null) {
			activity.setDescription(bean.getActivityDescription());
		}
		
		if(activity.getFrequency() instanceof PeriodicActivity) {
         	if(bean.getOpeningLocalDate() != null) {
         		((PeriodicActivity)activity.getFrequency()).setStartDate(bean.getOpeningLocalDate());
         	}
         	if(bean.getEndLocalDate() != null) {
         		((PeriodicActivity)activity.getFrequency()).setEndDate(bean.getEndLocalDate());
         	}
         	
         } else if(activity.getFrequency() instanceof ExpiringActivity) {
        	if(bean.getOpeningLocalDate() != null) {
          		((ExpiringActivity)activity.getFrequency()).setStartDate(bean.getOpeningLocalDate());
          	}
          	if(bean.getEndLocalDate() != null) {
          		((ExpiringActivity)activity.getFrequency()).setEndDate(bean.getEndLocalDate());
          	}
         }		
		//Gestire il place se vogliamo cambiarlo
		if(bean.getOpeningLocalTime() != null) {
			activity.getFrequency().setOpeningTime(bean.getOpeningLocalTime());
		}
		if(bean.getClosingLocalTime() != null) {
			activity.getFrequency().setClosingTime(bean.getClosingLocalTime());
		}
		
		//Aggiorno le preferenze
		activity.getIntrestedCategories().setAdrenalina(bean.isAdrenalina());
		activity.getIntrestedCategories().setArte(bean.isArte());
		activity.getIntrestedCategories().setCibo(bean.isCibo());
		activity.getIntrestedCategories().setEsplorazione(bean.isEsplorazione());
		activity.getIntrestedCategories().setIstruzione(bean.isIstruzione());
		activity.getIntrestedCategories().setModa(bean.isModa());
		activity.getIntrestedCategories().setMonumenti(bean.isMonumenti());
		activity.getIntrestedCategories().setMusica(bean.isMusica());
		activity.getIntrestedCategories().setNatura(bean.isNatura());
		activity.getIntrestedCategories().setRelax(bean.isRelax());
		activity.getIntrestedCategories().setRicorrenzeLocali(bean.isRicorrenze());
		activity.getIntrestedCategories().setShopping(bean.isShopping());		
		activity.getIntrestedCategories().setSocial(bean.isSocial());
		activity.getIntrestedCategories().setSport(bean.isSport());		
		
		//Ora che ho aggiornato l'attività chiamo il dao e faccio l'update
		daoAc.updateCertAcivity(activity);
		return true;
	}
	
	/*Rimuovere in caso si sposti il metodo `claimActivity`in un altra classe*/
	public void claimActivity() throws Exception{
		if(session == null || bean.getIdActivity() == 0) throw new NullPointerException();
		
		Long partnerID = session.getUserID();
		Long activityId;
		try {
			activityId = Long.valueOf(bean.getIdActivity());
		}catch(Exception e) {
			throw e;
		}
		
		daoAc.claimActivity(activityId, partnerID);
	}
}
