package logic.model;

import java.util.regex.Pattern;

import logic.controller.RegExplorerController;

public class RegExpBean extends RegBean{
	private PreferenceBean pref;
	
	public RegExpBean(){
		super();
		pref = new PreferenceBean();
	}
	//Inizio getter e setter delle preferenze
	
	public boolean getArte() {
		return pref.isArte();
	}

	public void setArte(boolean arte) {
		pref.setArte(arte);
	}

	public boolean getCibo() {
		return pref.isCibo();
	}

	public void setCibo(boolean cibo) {
		pref.setCibo(cibo);
	}

	public boolean getMusica() {
		return pref.isMusica();
	}

	public void setMusica(boolean musica) {
		pref.setMusica(musica);
	}

	public boolean getSport() {
		return pref.isSport();
	}

	public void setSport(boolean sport) {
		pref.setSport(sport);
	}

	public boolean getSocial() {
		return pref.isSocial();
	}

	public void setSocial(boolean social) {
		pref.setSocial(social);
	}

	public boolean getNatura() {
		return pref.isNatura();
	}

	public void setNatura(boolean natura) {
		pref.setNatura(natura);
	}

	public boolean getEsplorazione() {
		return pref.isEsplorazione();
	}

	public void setEsplorazione(boolean esplorazione) {
		pref.setEsplorazione(esplorazione);
	}

	public boolean getRicorrenze() {
		return pref.isRicorrenze();
	}

	public void setRicorrenze(boolean ricorrenze) {
		pref.setRicorrenze(ricorrenze);
	}

	public boolean getModa() {
		return pref.isModa();
	}

	public void setModa(boolean moda) {
		pref.setModa(moda);
	}

	public boolean getShopping() {
		return pref.isShopping();
	}

	public void setShopping(boolean shopping) {
		pref.setShopping(shopping);
	}

	public boolean getAdrenalina() {
		return pref.isAdrenalina();
	}

	public void setAdrenalina(boolean adrenalina) {
		pref.setAdrenalina(adrenalina);
	}

	public boolean getRelax() {
		return pref.isRelax();
	}

	public void setRelax(boolean relax) {
		pref.setRelax(relax);
	}

	public boolean getIstruzione() {
		return pref.isIstruzione();
	}

	public void setIstruzione(boolean istruzione) {
		pref.setIstruzione(istruzione);
	}

	public boolean getMonumenti() {
		return pref.isMonumenti();
	}

	public void setMonumenti(boolean monumenti) {
		pref.setMonumenti(monumenti);
	}

}
