package main;

public class Transition {

	Integer etat;
	String etiquette;
	
	public Transition(Integer etat, String etiquette) {
		this.etat = etat;
		this.etiquette = etiquette;
	}

	@Override
	public String toString() {
		return this.etat+":"+this.etiquette;
	}

	public String getEtiquette() {
		return this.etiquette;
	}

}