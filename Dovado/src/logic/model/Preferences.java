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
	
	
	
	public Preferences(boolean arte, boolean cibo, boolean musica, boolean sport, boolean social, boolean natura,
			boolean esplorazione, boolean ricorrenzeLocali, boolean moda, boolean shopping, boolean adrenalina,
			boolean relax, boolean istruzione, boolean monumenti) {
		super();
		this.arte = arte;
		this.cibo = cibo;
		this.musica = musica;
		this.sport = sport;
		this.social = social;
		this.natura = natura;
		this.esplorazione = esplorazione;
		this.ricorrenzeLocali = ricorrenzeLocali;
		this.moda = moda;
		this.shopping = shopping;
		this.adrenalina = adrenalina;
		this.relax = relax;
		this.istruzione = istruzione;
		this.monumenti = monumenti;
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
	public void setRicorrenzeLocali(boolean ricorrenze_locali) {
		this.ricorrenzeLocali = ricorrenze_locali;
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
		boolean[] prefSet = {
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
		return prefSet;
	}
	public String[] getPreferencesName() {
		String[] prefNames = {"Arte","Cibo","Musica","Sport","Social","Natura","Esplorazione","Ricorrenze_locali", 
				"Moda","Shopping","Adrenalina","Relax","Istruzione","Monumenti"};
		
		return prefNames;
	}
	/******************************************************************************
	 *  fine getter e setter 
	 ******************************************************************************/

	public int checkCompatibility(Preferences activityPref) {
		int compatibilityRate = 0;
		
		if(this.isArte() && activityPref.isArte()) compatibilityRate++;
		if(this.isCibo() && activityPref.isCibo()) compatibilityRate++;
		if(this.isMusica() && activityPref.isMusica()) compatibilityRate++;
		if(this.isSport() && activityPref.isSport()) compatibilityRate++;
		if(this.isSocial() && activityPref.isSocial()) compatibilityRate++;
		if(this.isNatura() && activityPref.isNatura()) compatibilityRate++;
		if(this.isEsplorazione() && activityPref.isEsplorazione()) compatibilityRate++;
		if(this.isRicorrenzeLocali() && activityPref.isRicorrenzeLocali()) compatibilityRate++;
		if(this.isModa() && activityPref.isModa()) compatibilityRate++;
		if(this.isShopping() && activityPref.isShopping()) compatibilityRate++;
		if(this.isAdrenalina() && activityPref.isAdrenalina()) compatibilityRate++;
		if(this.isRelax() && activityPref.isRelax()) compatibilityRate++;
		if(this.isIstruzione() && activityPref.isIstruzione()) compatibilityRate++;
		if(this.isMonumenti() && activityPref.isMonumenti()) compatibilityRate++;
		
		return compatibilityRate;
		
	}
	
}
