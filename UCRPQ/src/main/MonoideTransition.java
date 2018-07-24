package main;

import java.util.HashMap;
import java.util.List;

public class MonoideTransition {


	HashMap<String,List<String>> mot;
	HashMap<List<String>,String> listemot;
	HashMap<String,String> listeregle;
	
	public MonoideTransition() {
		this.mot = new HashMap<>();
		this.listemot = new HashMap<>();
		this.listeregle = new HashMap<>();
	}
	
	public void addmot(String mot, List<String> correspondant) {
		this.mot.put(mot,correspondant);
		this.listemot.put(correspondant,mot);
	}
	
	public void addliste(List<String> correspondant,String mot) {
		this.listemot.put(correspondant,mot);
		this.mot.put(mot,correspondant);
	}

	public boolean containtListeMot(List<String> correspond) {
		return listemot.containsKey(correspond);
	}

	public void addregle(String motlong, List<String> correspond) {
		listeregle.put(motlong, listemot.get(correspond));			
	}
	
	@Override
	public String toString() {
		return mot.toString()+"\n"+listeregle.toString();
	}		

}
