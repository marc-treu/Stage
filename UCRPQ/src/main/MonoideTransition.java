package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MonoideTransition {


	HashMap<String,List<String>> mot;
	HashMap<List<String>,String> listeMot;
	HashMap<String,String> listeRegle;
	
	public MonoideTransition() {
		this.mot = new HashMap<>();
		this.listeMot = new HashMap<>();
		this.listeRegle = new HashMap<>();
	}
	
	public void addMot(String mot, List<String> correspondant) {
		this.mot.put(mot,correspondant);
		this.listeMot.put(correspondant,mot);
	}
	
	public void addListe(List<String> correspondant,String mot) {
		this.listeMot.put(correspondant,mot);
		this.mot.put(mot,correspondant);
	}

	public boolean containtListeMot(List<String> correspond) {
		System.out.println(this+"\n"+correspond.toString());
		return listeMot.containsKey(correspond);
	}

	public void addRegle(String motlong, List<String> correspond) {
		listeRegle.put(motlong, listeMot.get(correspond));			
	}
	
	@Override
	public String toString() {
		return mot.toString()+"\n"+listeRegle.toString();
	}		

}
