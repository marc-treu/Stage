package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import main.RegExp.Type;


public class Automate {
	
	Set<Etat> table_transition;
	RegExp regexp;
	
	
	public Automate(Set<Etat> table_transition, RegExp regexp) {
		this.table_transition = table_transition;
		this.regexp = regexp;
	}
	
	
	/**
	 * Algorithme de GLUSHKOV
	 * 
	 * 
	 * @param r, l'expression rationnel que l'on cherche a transformé en automate
	 * @return un Automate fini qui correspond au langage décrit par r
	 */
	public Automate (RegExp r) {
		
		//INITIALISATION 
		
		boolean finale = false;
		Set<Etat> tableTransition = new HashSet<>();
		RegExp r_renomer = r.getRename(1);
		

		Etat etat_0 = new Etat("0",true,isfinal(r_renomer));
		// On recupere la liste des initiaux
		for (String e : r_renomer.getInitaux()) {
			etat_0.addTransition(new Transition(e.substring(1),e.substring(0, 1)));
		}
		tableTransition.add(etat_0);				

		
		// Boucle Principal
		for (String s : r_renomer.getEtiquette()) {
			List<String> sv = new ArrayList<>();
			finale = !r_renomer.getSuivant(sv,s);
			Etat etat_n = new Etat(s.substring(1),false,finale);
			for (String e : sv) {
				etat_n.addTransition(new Transition(e.substring(1),e.substring(0, 1)));
			}
			tableTransition.add(etat_n);				
		}
		
		this.table_transition = tableTransition;
		this.regexp = r;
	}
	
	private boolean isfinal(RegExp regexp) {
		for (String e : regexp.getInitaux()) {
			if (!regexp.getSuivant(new ArrayList<>(), e)) 
				return true;
		}
		return false;
	}


	public boolean isDeterminist() {
		return this.table_transition.stream().allMatch(e -> e.isDeterminist());
	}
	
	public Automate getDeterminist() {
		
		if(this.isDeterminist()) // Si l'automate est deja deterministe
			return this;
		
		Set<Etat> tableTransition = new HashSet<>();
		Queue<List<String>> file = new ArrayDeque<>();
		List<String> alphabet = getAlphabet();
		
		List<String> etat_initiaux = new ArrayList<>();
		for (Etat e : this.table_transition) {
			if (e.isInitial())
				etat_initiaux.add(e.getNom());
		}
		Collections.sort(etat_initiaux);
		file.add(etat_initiaux);
		boolean initial = true;

		
		
		while(!file.isEmpty()) {
			List<Etat> l = findEtats(file.poll());
			Collections.sort(l, new Comparator<Etat>() {
		        public int compare(Etat e1, Etat e2) {
		            return  e1.nom.compareTo(e2.nom);
		        }
		    });
			
			boolean finale = l.get(0).isFinale();
			String nom = l.get(0).getNom();
			
			for(int j = 1; j<l.size() ;++j) {
				nom+="."+l.get(j).getNom();
				if(l.get(j).isFinale()) finale=true ;
			}
			
			Etat etat_n = new Etat(nom, initial, finale);
			
			initial = false;

			
			for (String a : alphabet) {
				Set<String> temp = new HashSet<>();
				for(int i = 0 ; i<l.size(); ++i) {
					temp.addAll(l.get(i).getTransitionFromEtiquette(a));
				}
				List<String> suivant = new ArrayList<>(temp);
				Collections.sort(suivant);
				if (!suivant.isEmpty()) {
					String nomTemp = suivant.get(0);
					for(int c = 1; c<suivant.size() ;++c) {
						nomTemp+="."+suivant.get(c);
					}
					
					etat_n.addTransition(new Transition(nomTemp, a));

					if ( findEtats(tableTransition,Arrays.asList(nomTemp)).isEmpty()) {
						file.add(suivant);
					}
				}				
			}	
			if (findEtats(tableTransition,Arrays.asList(etat_n.getNom())).isEmpty() )
				tableTransition.add(etat_n);			
		}	
		
		
		return new Automate(rename(tableTransition),this.regexp);
	}

	
	public List<String> getAlphabet() {
		Set<String> resultat = new HashSet<>();
		for (Etat t : this.table_transition) {
			resultat.addAll(t.getAlphabet());
		}
		return new ArrayList<>(resultat);
	}
	
	
	public List<Etat> findEtats(List<String> noms) {
		List<Etat> resultat = new ArrayList<>();
		for (Etat t : this.table_transition) {
			for (String s : noms) {
				if (t.equals(s)) resultat.add(t.getEtat());
			}
		}
		return resultat;
	}
	
	public List<Etat> findEtats(Set<Etat> set , List<String> noms) {
		List<Etat> resultat = new ArrayList<>();
		for (Etat t : set) {
			for (String s : noms) {
				if (t.equals(s)) resultat.add(t.getEtat());
			}
		}
		return resultat;
	}
	

	private Set<Etat> rename(Set<Etat> tableTransition) {
		Etat [] resultat_etat = new Etat[tableTransition.size()];
		Map<String, String> hm = new HashMap<>();
		int i = 1;
		for (Etat e : tableTransition) {
			if (e.isInitial()) 
				hm.put(e.getNom(), "0");
			else
				hm.put(e.getNom(), String.valueOf(i++));
			resultat_etat[Integer.parseInt(hm.get(e.getNom()))] = new Etat(hm.get(e.getNom()),e.isInitial(),e.isFinale());
		}
		
		for (Etat e : tableTransition) {
			for (Transition t : e.getTransition()) {
				resultat_etat[Integer.parseInt(hm.get(e.getNom()))].addTransition(
						new Transition(hm.get(t.getEtat()), t.getEtiquette()));
			}
		}
		
		return new HashSet<Etat>(Arrays.asList(resultat_etat));
	}
	
	
	/**
	 * 
	 * Methode pour reverser, inverser, l'automate.
	 * C'est a dire que les etats finale devienne initial et inversement,
	 * Et pour chaque transition, l'etat de départ devient finale et inversement.
	 * 
	 * @return L'automate renversé
	 */
	public Automate Renversement() {
		
		Set<Etat> resultat_etat = new HashSet<>(); // Notre set des Etats qui sera notre nouveau automate
		
		for (Etat e : this.table_transition) { // On va d'abord remplir notre set resultat d'etats
			resultat_etat.add(new Etat(e.getNom(),(e.isFinale()),(e.isInitial()))); // On prend soins d'inverser finale et initiale
		}
		
		// On reparcour les etats de notre automate
		for (Etat e : this.table_transition) { // Mais on s'interesse a leur transition
			for (Transition t : e.getTransition()) { // Pour chaque transition, on inverse les etat de départ et d'arrivé
				findEtats(resultat_etat, Arrays.asList(t.getEtat())).get(0).addTransition( // et on ajout la nouvelle 
						new Transition(e.getNom(), t.getEtiquette()));; // Transition dans notre set resultat_etat
			}
		}
		// On renvoie le nouvel automate inversé.
		return new Automate(resultat_etat,this.regexp);
	}
	
	
	/**
	 * 
	 * Algorithme de Brzozowski, qui minimise un automate.
	 * La procédure de minimisation
	 * -inverser l'automate,
	 * -determiniser
	 * -ré-inverser
	 * -re-determiniser
	 * 
	 * @return L'automate minimale
	 */
	public Automate brzozowski() {
		return this.Renversement().getDeterminist().Renversement().getDeterminist();
	}
	
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nRegExp : "+ this.regexp);
		
		for (Etat e : this.table_transition) {
			sb.append("\n"+e.toString());
		}
		
		return sb.toString();
	}
	
	
}
