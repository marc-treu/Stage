package main;

import java.lang.reflect.MalformedParametersException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonoideTransition {


	HashMap<String,List<String>> mot;
	HashMap<List<String>,String> listeMot;
	HashMap<String,String> listeRegle;
	HashMap<String,Set<String>> lClasse;
	HashMap<String,Set<String>> rClasse;
	
	
	public MonoideTransition() {
		this.mot = new HashMap<>();
		this.listeMot = new HashMap<>();
		this.listeRegle = new HashMap<>();
		this.lClasse = new HashMap<>();
		this.rClasse = new HashMap<>();
	}
	
	public void ajoute(String mot, List<String> correspondant) {
		this.mot.put(mot,correspondant);
		this.listeMot.put(correspondant,mot);
	}
	
	public void ajoute(List<String> correspondant,String mot) {
		ajoute(mot, correspondant);
	}

	public boolean contient(List<String> correspond) {
		return this.listeMot.containsKey(correspond);
	}
	
	public boolean contient(String mot) {
		return this.mot.containsKey(mot);
		
	}
	
	public void ajouteRegle(String motlong, List<String> correspond) {
		this.listeRegle.put(motlong, listeMot.get(correspond));			
	}
	
	@Override
	public String toString() {
		return mot.toString()+"\n"+listeRegle.toString();
	}		

	
	/**
	 * Methode qui calcule si le monoide est aperiodique.
	 * On va la utiliser la relation de green, plus precisement
	 * la H-classe. Il faut calculer la L et la R-classe.
	 * Et si pour tous les elements du monoide, il n'existe pas
	 * deux elements distinct a et b, tel que aLb et aRb, alors
	 * le monoide est aperiodique
	 * 
	 * @return true si le monoide est apériodique, false sinon
	 */
	public boolean estAperiodique() {
				
		// On calculer les classes de relation si besoin
		if (this.lClasse.isEmpty())	this.calculerRelationLclasse();
		if (this.rClasse.isEmpty()) this.calculerRelationRclasse();
		
		for (String a : this.lClasse.keySet()) {
			for (String b : this.lClasse.keySet()) {
				if (!a.equals(b) 
						&& this.lClasse.get(a).equals(this.lClasse.get(b))
						&& this.rClasse.get(a).equals(this.rClasse.get(b))) 
					return false;				
			}
		}
		
		return true;
	}
	
	/**
	 * Methode qui calcule la R-classe dans le monoide
	 * avec S l'ensemble des elements du monoide, on calcule pour chaque
	 * s de S, Ss
	 */
	protected void calculerRelationLclasse() {
		calculerRelationXclasse(true);
	}
	
	/**
	 * Methode qui calcule la R-classe dans le monoide
	 * avec S l'ensemble des elements du monoide, on calcule pour chaque
	 * s de S, sS
	 */
	protected void calculerRelationRclasse() {
		calculerRelationXclasse(false);
	}
	
	/**
	 * Methode qui calcule la L-classe, si le paramettre est true
	 * Sinon la R-classe
	 * 
	 * @param lClasse, boolean qui indique quelle classe a calculer
	 */
	protected void calculerRelationXclasse(boolean lClasse) {
		Set<String> elementsMonoide = this.mot.keySet();
		
		for (String element : elementsMonoide) {
			Set<String> xClasseMot = new HashSet<>();
			for (String element2 : elementsMonoide) {
				// On teste si on cherche la L-classe ou la R-classe
				xClasseMot.add(lClasse ? calcule(element2,element) : calcule(element,element2));
			}
			if (lClasse)
				this.lClasse.put(element, xClasseMot);
			else
				this.rClasse.put(element, xClasseMot);
			
		}
	}
	
	/**
	 * Methode qui pour un mot (element+element2) renvoie l'element
	 * Du monoide qui correspond.
	 * Puisque la fonction est appelé pour calculer les R(L)-classe
	 * On sais que element et element2 sont bien des elements du
	 * Monoide, Donc il existe forcement une regle a appliquer
	 * 
	 * @param element Le debut de la chaine
	 * @param element2 La fin de la chaine
	 * @return Un element du monoide
	 */
	protected String calcule(String element, String element2) {
		
		StringBuilder sb = new StringBuilder(element+element2);
		
		if (this.contient(sb.toString())) // Si on est deja dans le monoide
			return sb.toString();
		
		if (this.listeRegle.containsKey(sb.toString())) // Si on peut appliquer une regle a element+element2
			return calcule(listeRegle.get(sb.toString()),"");
		
		if (this.listeRegle.containsKey(element)) { // Si on peut appliquer une regle a element
			this.listeRegle.put(sb.toString(), listeRegle.get(element)); // On va ajouter une nouvelle regle qui va de element+element2 vers la regle d'element
			return calcule(listeRegle.get(element),element2);
		}
		
		if (element2.isEmpty()) // Normalement puisque element et element2 sont dans le monoide, il y aura toujour une regle a appliquer
			throw new MalformedParametersException();
		
		// Sinon on va re-appliquer la methode calcule en deplacent la frontiere d'un entre les deux string pour trouver une régle
		return calcule(String.format(element, element2.charAt(0)),element2.substring(1));
	}

	
}
