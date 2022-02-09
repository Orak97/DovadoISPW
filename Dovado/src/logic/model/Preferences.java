package logic.model;

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
	
	private PreferenceBean prefs;
	
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
		prefs.setPreferences(boolPref);
	}
	
	public Preferences(PreferenceBean bean) {
		prefs = bean;
	}
	
	public Preferences(boolean[] interestedCategories) {
		prefs.setPreferences(interestedCategories);
	}

	/******************************************************************************
	 *  getter e setter 
	 ******************************************************************************/
	public boolean isArte() {
		return prefs.isArte();
	}

	public void setArte(boolean arte) {
		prefs.setArte(arte);
	}

	public boolean isCibo() {
		return prefs.isCibo();
	}

	public void setCibo(boolean cibo) {
		prefs.setCibo(cibo);
	}

	public boolean isMusica() {
		return prefs.isMusica();
	}

	public void setMusica(boolean musica) {
		prefs.setMusica(musica);
	}

	public boolean isSport() {
		return prefs.isSport();
	}

	public void setSport(boolean sport) {
		prefs.setSport(sport);
	}

	public boolean isSocial() {
		return prefs.isSocial();
	}

	public void setSocial(boolean social) {
		prefs.setSocial(social);
	}

	public boolean isNatura() {
		return prefs.isNatura();
	}

	public void setNatura(boolean natura) {
		prefs.setNatura(natura);
	}

	public boolean isEsplorazione() {
		return prefs.isEsplorazione();
	}

	public void setEsplorazione(boolean esplorazione) {
		prefs.setEsplorazione(esplorazione);
	}

	public boolean isRicorrenzeLocali() {
		return prefs.isRicorrenze();
	}

	public void setRicorrenzeLocali(boolean ricorrenze) {
		prefs.setRicorrenze(ricorrenze);
	}

	public boolean isModa() {
		return prefs.isModa();
	}

	public void setModa(boolean moda) {
		prefs.setModa(moda);
	}

	public boolean isShopping() {
		return prefs.isShopping();
	}

	public void setShopping(boolean shopping) {
		prefs.setShopping(shopping);
	}

	public boolean isAdrenalina() {
		return prefs.isAdrenalina();
	}

	public void setAdrenalina(boolean adrenalina) {
		prefs.setAdrenalina(adrenalina);
	}

	public boolean isRelax() {
		return prefs.isRelax();
	}

	public void setRelax(boolean relax) {
		prefs.setRelax(relax);
	}

	public boolean isIstruzione() {
		return prefs.isIstruzione();
	}

	public void setIstruzione(boolean istruzione) {
		prefs.setIstruzione(istruzione);
	}

	public boolean isMonumenti() {
		return prefs.isMonumenti();
	}

	public void setMonumenti(boolean monumenti) {
		prefs.setMonumenti(monumenti);
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
