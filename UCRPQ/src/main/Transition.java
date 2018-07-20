package main;

public class Transition {

	String etat;
	String etiquette;
	
	public Transition(String etat, String etiquette) {
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

	public String getEtat() {
		return this.etat;
	}

}