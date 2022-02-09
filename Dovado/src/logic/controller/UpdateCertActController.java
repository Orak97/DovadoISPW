package logic.controller;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;

import logic.model.CertifiedActivity;
import logic.model.ContinuosActivity;
import logic.model.CreateActivityBean;
import logic.model.DAOActivity;
import logic.model.Discount;
import logic.model.DiscountBean;
import logic.model.ExpiringActivity;
import logic.model.Log;
import logic.model.Partner;
import logic.model.PeriodicActivity;
import logic.model.SuperUser;

public class UpdateCertActController {
	private CertifiedActivity activity;
	private DAOActivity daoAc;
	private CreateActivityBean bean;
	private DiscountBean beanDiscount;
	
	/*Rimuovere in caso si sposti il metodo `claimActivity`in un altra classe*/
	private Partner session; 
	
	public UpdateCertActController(CreateActivityBean bean, CertifiedActivity activity) {
		this.activity = activity;
		this.bean = bean;
		daoAc = DAOActivity.getInstance();
	}
	
	/*Rimuovere in caso si sposti il metodo `claimActivity`in un altra classe*/
	public UpdateCertActController(CreateActivityBean bean,Partner session) {
		this.session = session;
		this.bean = bean;
		daoAc = DAOActivity.getInstance();
	}
	
	/*Costruttore che viene chiamato solo quando si vogliono modificare gli sconti di un'attività dal partner, metodo `modifyDiscounts`*/
	public UpdateCertActController(DiscountBean bean, Partner session) {
		this.session = session;
		this.beanDiscount = bean;
		daoAc = DAOActivity.getInstance();
	}
	
	public UpdateCertActController(CreateActivityBean bean, SuperUser session) {
		if(session instanceof Partner) {
			this.session = (Partner)session;
			this.bean = bean;
			bean.setOwner(this.session.getUserID().intValue());
			daoAc = DAOActivity.getInstance();
		} else {
			throw new InvalidParameterException("Si accettano solo partner");
		}
	}
	
	/*-----------------------------------------------------------------------*/
	
	public UpdateCertActController(CreateActivityBean bean) {
		this(bean,(Partner)null);
	}
	
	public boolean updateActivity() throws ClassNotFoundException, SQLException {
		boolean error = false;
		
		if (activity == null) {
			error = updateByBean();
		} else {
			error = updateWithCheck();
		}
		return error;
	}
	
	public boolean updateByBean() throws ClassNotFoundException, SQLException {
		CreateActivityController actController = new CreateActivityController(bean);
		//creo l'attività con i parametri aggiornati
		activity = (CertifiedActivity) actController.createActivity();
		//La passo al DAO
		daoAc.updateCertAcivity(activity);
		return true;
	}
	
	public boolean updateWithCheck() throws ClassNotFoundException, SQLException {
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
	public void claimActivity() throws ClassNotFoundException, SQLException{
		if(session == null || bean.getIdActivity() == 0) throw new NullPointerException();
		
		Long partnerID = session.getUserID();
		Long activityId;
		try {
			activityId = Long.valueOf(bean.getIdActivity());
		}catch(NumberFormatException e) {
			Log.getInstance().getLogger().severe("l'ID dell'attività da reclamare è NULL");
			throw e;
		}
		daoAc.claimActivity(activityId, partnerID);
	}
	
	/*Chiamare questo metodo dopo aver istanziato il controller tramite il costruttore che prende come parametri `DiscountBean` e `Partner`*/
	public void modifyDiscounts() throws ClassNotFoundException, SQLException{
		if(session == null || beanDiscount.getActToEdit() == 0) throw new NullPointerException();
		Long partnerID = session.getUserID();
		Long activityId;
		try {
			activityId = Long.valueOf(beanDiscount.getActToEdit());
		}catch(NumberFormatException e) {
			Log.getInstance().getLogger().severe("l'ID dell'attività da reclamare è NULL");
			throw e;
		}
		
		ArrayList<Discount> discounts = new ArrayList<>();
		discounts.add(new Discount(5,beanDiscount.isDiscount5(),0));
		discounts.add(new Discount(10,beanDiscount.isDiscount10(),0));
		discounts.add(new Discount(20,beanDiscount.isDiscount20(),0));
		discounts.add(new Discount(30,beanDiscount.isDiscount30(),0));
		discounts.add(new Discount(50,beanDiscount.isDiscount50(),0));
		
		daoAc.modifyDiscounts(activityId, partnerID, discounts);
	}
	
}
