package main;

import java.util.Arrays;
import java.util.HashMap;

public class MonoideTransition {


	HashMap<String,String[]> mot;
	HashMap<String[],String> listeMot;
	HashMap<String,String> listeRegle;
	
	public MonoideTransition() {
		this.mot = new HashMap<>();
		this.listeMot = new HashMap<>();
		this.listeRegle = new HashMap<>();
	}
	
	public void addMot(String mot, String[] correspondant) {
		this.mot.put(mot,correspondant);
		this.listeMot.put(correspondant,mot);
	}
	
	public void addListe(String[] correspondant,String mot) {
		this.listeMot.put(correspondant,mot);
		this.mot.put(mot,correspondant);
	}

	public boolean containtListeMot(String[] correspond) {
		System.out.println(this+"\n"+Arrays.toString(correspond));
		return listeMot.containsKey(correspond);
	}

	public void addRegle(String motlong, String[] correspond) {
		listeRegle.put(motlong, listeMot.get(correspond));			
	}
	
	@Override
	public String toString() {
		return mot.toString()+"\n"+listeRegle.toString();
	}		

}
