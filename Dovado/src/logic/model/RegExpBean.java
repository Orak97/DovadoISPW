package logic.model;

import java.util.regex.Pattern;

import logic.controller.RegExplorerController;

public class RegExpBean extends RegBean{
	
	private boolean arte = false;
	private boolean cibo = false;
	private boolean musica = false;
	private boolean sport = false;
	private boolean social = false;
	private boolean natura = false;
	private boolean esplorazione = false;
	private boolean ricorrenze = false;
	private boolean moda = false;
	private boolean shopping = false;
	private boolean adrenalina = false;
	private boolean relax = false;
	private boolean istruzione= false;
	private boolean monumenti = false;
	
	//Inizio getter e setter delle preferenze
	
	public boolean getArte() {
		return arte;
	}

	public void setArte(boolean arte) {
		this.arte = arte;
	}

	public boolean getCibo() {
		return cibo;
	}

	public void setCibo(boolean cibo) {
		this.cibo = cibo;
	}

	public boolean getMusica() {
		return musica;
	}

	public void setMusica(boolean musica) {
		this.musica = musica;
	}

	public boolean getSport() {
		return sport;
	}

	public void setSport(boolean sport) {
		this.sport = sport;
	}

	public boolean getSocial() {
		return social;
	}

	public void setSocial(boolean social) {
		this.social = social;
	}

	public boolean getNatura() {
		return natura;
	}

	public void setNatura(boolean natura) {
		this.natura = natura;
	}

	public boolean getEsplorazione() {
		return esplorazione;
	}

	public void setEsplorazione(boolean esplorazione) {
		this.esplorazione = esplorazione;
	}

	public boolean getRicorrenze() {
		return ricorrenze;
	}

	public void setRicorrenze(boolean ricorrenze) {
		this.ricorrenze = ricorrenze;
	}

	public boolean getModa() {
		return moda;
	}

	public void setModa(boolean moda) {
		this.moda = moda;
	}

	public boolean getShopping() {
		return shopping;
	}

	public void setShopping(boolean shopping) {
		this.shopping = shopping;
	}

	public boolean getAdrenalina() {
		return adrenalina;
	}

	public void setAdrenalina(boolean adrenalina) {
		this.adrenalina = adrenalina;
	}

	public boolean getRelax() {
		return relax;
	}

	public void setRelax(boolean relax) {
		this.relax = relax;
	}

	public boolean getIstruzione() {
		return istruzione;
	}

	public void setIstruzione(boolean istruzione) {
		this.istruzione = istruzione;
	}

	public boolean getMonumenti() {
		return monumenti;
	}

	public void setMonumenti(boolean monumenti) {
		this.monumenti = monumenti;
	}

}
