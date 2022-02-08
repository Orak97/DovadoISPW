package logic.model;

public class DiscountBean {
	private int actToEdit;
	private boolean dicount5;
	private boolean dicount10;
	private boolean dicount20;
	private boolean dicount30;
	private boolean dicount50;
	
	public boolean isDicount5() {
		return dicount5;
	}
	public void setDicount5(boolean dicount5) {
		this.dicount5 = dicount5;
	}
	public boolean isDicount10() {
		return dicount10;
	}
	public void setDicount10(boolean dicount10) {
		this.dicount10 = dicount10;
	}
	public boolean isDicount20() {
		return dicount20;
	}
	public void setDicount20(boolean dicount20) {
		this.dicount20 = dicount20;
	}
	public boolean isDicount30() {
		return dicount30;
	}
	public void setDicount30(boolean dicount30) {
		this.dicount30 = dicount30;
	}

	public boolean isDicount50() {
		return dicount50;
	}
	public void setDicount50(boolean dicount50) {
		this.dicount50 = dicount50;
	}
	public int getActToEdit() {
		return actToEdit;
	}
	public void setActToEdit(int actToEdit) {
		this.actToEdit = actToEdit;
	}

}
