package logic.model;

public class Partner extends SuperUser{

	private String nomeAzienda;
	private String pIVA;
	
	public Partner(String usr, String email,Long id, String nomeAzienda, String pIVA) {
		super(usr, email,id);
		this.setNomeAzienda(nomeAzienda);
		this.setpIVA(pIVA);
	}
	
	public Partner(Long id, String nomeAzienda, String pIVA) {
		this(null,null,id,nomeAzienda,pIVA);
	}


	public String getpIVA() {
		return pIVA;
	}

	public void setpIVA(String pIVA) {
		this.pIVA = pIVA;
	}

	public String getNomeAzienda() {
		return nomeAzienda;
	}

	public void setNomeAzienda(String nomeAzienda) {
		this.nomeAzienda = nomeAzienda;
	}
	

}
