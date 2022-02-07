package logic.model;

import java.util.Iterator;

public class Preferences {
	private boolean arte; 
	private boolean cibo; 
	private boolean musica;
	private boolean sport;
	private boolean social;
	private boolean natura; 
	private boolean esplorazione; 
	private boolean ricorrenzeLocali;
	private boolean moda;
	private boolean shopping;
	private boolean adrenalina;
	private boolean relax;
	private boolean istruzione; 
	private boolean monumenti;
	
	
	public Preferences(CreateActivityBean bean) {
		boolean[] boolPref = {
				bean.isArte(),
				bean.isCibo(),
				bean.isMusica(),
				bean.isSport(),
				bean.isSocial(),
				bean.isNatura(),
				bean.isEsplorazione(),
				bean.isRicorrenze(),
				bean.isModa(),
				bean.isShopping(),
				bean.isAdrenalina(),
				bean.isRelax(),
				bean.isIstruzione(),
				bean.isMonumenti()};
		setInterestedCategories(boolPref);
	}
	
	public Preferences(PreferenceBean bean) {
		boolean[] boolPref = {
				bean.isArte(),
				bean.isCibo(),
				bean.isMusica(),
				bean.isSport(),
				bean.isSocial(),
				bean.isNatura(),
				bean.isEsplorazione(),
				bean.isRicorrenze(),
				bean.isModa(),
				bean.isShopping(),
				bean.isAdrenalina(),
				bean.isRelax(),
				bean.isIstruzione(),
				bean.isMonumenti()};
		setInterestedCategories(boolPref);
	}
	
	public Preferences(boolean[] interestedCategories) {
		setInterestedCategories(interestedCategories);
	}

	/******************************************************************************
	 *  getter e setter 
	 ******************************************************************************/
	public boolean isArte() {
		return arte;
	}
	public void setArte(boolean arte) {
		this.arte = arte;
	}
	public boolean isCibo() {
		return cibo;
	}
	public void setCibo(boolean cibo) {
		this.cibo = cibo;
	}
	public boolean isMusica() {
		return musica;
	}
	public void setMusica(boolean musica) {
		this.musica = musica;
	}
	public boolean isSport() {
		return sport;
	}
	public void setSport(boolean sport) {
		this.sport = sport;
	}
	public boolean isSocial() {
		return social;
	}
	public void setSocial(boolean social) {
		this.social = social;
	}
	public boolean isNatura() {
		return natura;
	}
	public void setNatura(boolean natura) {
		this.natura = natura;
	}
	public boolean isEsplorazione() {
		return esplorazione;
	}
	public void setEsplorazione(boolean esplorazione) {
		this.esplorazione = esplorazione;
	}
	public boolean isRicorrenzeLocali() {
		return ricorrenzeLocali;
	}
	public void setRicorrenzeLocali(boolean ricorrenzeLocali) {
		this.ricorrenzeLocali = ricorrenzeLocali;
	}
	public boolean isModa() {
		return moda;
	}
	public void setModa(boolean moda) {
		this.moda = moda;
	}
	public boolean isShopping() {
		return shopping;
	}
	public void setShopping(boolean shopping) {
		this.shopping = shopping;
	}
	public boolean isAdrenalina() {
		return adrenalina;
	}
	public void setAdrenalina(boolean adrenalina) {
		this.adrenalina = adrenalina;
	}
	public boolean isRelax() {
		return relax;
	}
	public void setRelax(boolean relax) {
		this.relax = relax;
	}
	public boolean isIstruzione() {
		return istruzione;
	}
	public void setIstruzione(boolean istruzione) {
		this.istruzione = istruzione;
	}
	public boolean isMonumenti() {
		return monumenti;
	}
	public void setMonumenti(boolean monumenti) {
		this.monumenti = monumenti;
	}

	public void setInterestedCategories(boolean[] interestedCategories) {
		this.setArte(interestedCategories[0]);
		this.setCibo(interestedCategories[1]);
		this.setMusica(interestedCategories[2]);
		this.setSport(interestedCategories[3]);
		this.setSocial(interestedCategories[4]);
		this.setNatura(interestedCategories[5]);
		this.setEsplorazione(interestedCategories[6]);
		this.setRicorrenzeLocali(interestedCategories[7]);
		this.setModa(interestedCategories[8]);
		this.setShopping(interestedCategories[9]);
		this.setAdrenalina(interestedCategories[10]);
		this.setRelax(interestedCategories[11]);
		this.setIstruzione(interestedCategories[12]);
		this.setMonumenti(interestedCategories[13]);
	}
	
	public boolean[] getSetPreferences() {
		return new boolean[]	{
				isArte(),
				isCibo(),
				isMusica(),
				isSport(),
				isSocial(),
				isNatura(),
				isEsplorazione(),
				isRicorrenzeLocali(),
				isModa(),
				isShopping(),
				isAdrenalina(),
				isRelax(),
				isIstruzione(),
				isMonumenti()};
				}
	
	public String[] getPreferencesName() {
		return new String[] {"Arte","Cibo","Musica","Sport","Social","Natura",
				"Esplorazione","Ricorrenze_locali", 
				"Moda","Shopping","Adrenalina","Relax","Istruzione","Monumenti"};
	}
	/******************************************************************************
	 *  fine getter e setter 
	 ******************************************************************************/

	public int checkCompatibility(Preferences activityPref) {
		int compatibilityRate = 0;
		
		boolean[] thisPref = getSetPreferences();
		boolean[] compPref = activityPref.getSetPreferences();
		
		int i = 0;
		for(boolean pref : thisPref) {
			if(pref && compPref[i]) {
				compatibilityRate++;
			}
			i++;
		}	
		return compatibilityRate;
		
	}
	
}
