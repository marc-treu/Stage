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
	
	Set<Etat> tableTransition;
	RegExp regexp;
	
	public Automate(Set<Etat> tableTransition, RegExp regexp) {
		this.tableTransition = tableTransition;
		this.regexp = regexp;
	}
	
	
	/**
	 * Algorithme de GLUSHKOV, cree un automate a partir d'une expression reguliere 
	 * 
	 * 
	 * @param r, l'expression rationnel que l'on cherche a transforme en automate
	 * @return un Automate fini qui correspond au langage decrit par r
	 */
	public Automate (RegExp r) {
		
		//INITIALISATION 
		boolean finale = false;
		Set<Etat> tableTransition = new HashSet<>(); // Notre set resultat des Etats
		RegExp rRenommer = r.getRename(1); // On renomme notre RegExp, de tel sorte
		// Que chaque atom soit unique.		

		Etat etat0 = new Etat("0",true,estFinal(rRenommer)); // On cree l'etat initial
		for (String e : rRenommer.getInitaux()) { // On recupere la liste successeur de l'etat initial
			etat0.ajoutTransition(new Transition(e.substring(1),e.substring(0, 1))); // On ajout les transitions
		} // e va etre de la forme a4 par exemple, avec toujour la premiere lettre qui correspond au chemins,
		// et le nombre qui suit l'etat vers lequel on va
		
		tableTransition.add(etat0); // On ajout cet Etat initial.
		
		// Boucle Principal
		for (String nomEtat : rRenommer.getEtiquette()) { // Pour chaque Atom
			List<String> listeSuccesseur = new ArrayList<>(); // On initialise la liste des successeurs
			finale = !rRenommer.getSuivant(listeSuccesseur,nomEtat); // Qui sera remplie par getSuivant
			// getSuivant revoie false si l'atom est peu etre final dans la RegExp
			
			Etat etatN = new Etat(nomEtat.substring(1),false,finale); // On cree l'etat
			for (String successeur : listeSuccesseur) { // Pour chaque successeur, on ajout la transition
				etatN.ajoutTransition(new Transition(successeur.substring(1),successeur.substring(0, 1)));
			}
			tableTransition.add(etatN);				
		}
		
		this.tableTransition = tableTransition;
		this.regexp = r;
	}
	
	
	/**
	 * Methode qui determine si une RegExp accepte le mot vide
	 * 
	 * @param regexp renomer pour pouvoir utliser getSuivant
	 * @return true si le langage accepte epsilon, false sinon
	 */
	private boolean estFinal(RegExp regexp) {
		if (regexp.type() == Type.Star) // Si l'expression est une etoile
			return true; // Alors on renvoie directement true
		for (String e : regexp.getInitaux()) { // Pour chaque etat que l'etat initial peu atteindre
			if (!regexp.getSuivant(new ArrayList<>(), e)) // On regarde si il y en a un qui est finale
				return true; // Si oui alors le langage accepte le mot vide
		}
		return false; // Sinon, on renvoie false
	}


	/**
	 * Methode qui determine si l'automate est deterministe.
	 * 
	 * @return true si c'est le cas, false sinon
	 */
	public boolean estDeterministe() {
		int nombreEtatInitial = 0;
		for (Etat etat : this.tableTransition) 
			nombreEtatInitial += etat.estInitial() ? 1 : 0;
		return nombreEtatInitial==1 && this.tableTransition.stream().allMatch(etat -> etat.estDeterministe());
	}

	
	/**
	 * Methode qui determine un automate
	 * 
	 * @return
	 */
	public Automate getDeterministe() {
		
		if(this.estDeterministe()) // Si l'automate est deja deterministe
			return this;
		
		// Initialisation
		Set<Etat> tableTransition = new HashSet<>();
		Queue<List<String>> file = new ArrayDeque<>(); 
		List<String> alphabet = getAlphabet(); // On recuperer l'alphabet de l'automate
		
		// On s'occupe de l'etat initial
		List<String> etatInitiaux = new ArrayList<>();
		for (Etat e : this.tableTransition) { // Pour tous les etats
			if (e.estInitial()) // On teste si il sont initiaux
				etatInitiaux.add(e.getNom()); // si oui, on les 
		}
		Collections.sort(etatInitiaux); // On les trie 
		file.add(etatInitiaux); // On les ajoute a notre file
		boolean initial = true; // Le premiere etat que l'on va traiter sera l'etat initial.
		// Le boolean va permettre a la boucle while de savoir qu'il s'agit du premiere etat.
		
		while(!file.isEmpty()) { // tant que la file n'est pas vide
			List<Etat> l = trouveEtats(this.tableTransition,file.poll()); // On recupere les etats associer a la liste dans la tete de la file
			Collections.sort(l, new Comparator<Etat>() { // On les trie, mais normalement, lorsque l'on 
		        public int compare(Etat e1, Etat e2) { // ajout la liste des noms des etat dans la file
		            return  e1.nom.compareTo(e2.nom); // On les trie deja
		        }
		    });
			
			// On cree le  nouvelle etat, qui peut etre l'association de plusieur
			boolean finale = l.get(0).estFinale();
			String nom = l.get(0).getNom();
			
			for(int j = 1; j<l.size() ;++j) { // Sont nom sera la forme 
				nom+="."+l.get(j).getNom(); // 1.3.5 par exemple.
				if(l.get(j).estFinale()) finale=true ; // Si un des etats qui le compose est finale, alors il sera finale
			}
			
			Etat etatN = new Etat(nom, initial, finale);
			
			initial = false; // Si on etait a la premiere iteration, on viens de cree l'etat initial, est donc on passe la variable a false

			
			for (String a : alphabet) { // Pour chaque lettre de l'alphabet
				Set<String> temp = new HashSet<>(); 
				for(int i = 0 ; i<l.size(); ++i) { // Pour chaque etat qui compose notre nouvelle etat
					temp.addAll(l.get(i).getTransitionAvecEtiquette(a)); // On recuperer tous les etat voisins par la lettre a 
				}
				List<String> suivant = new ArrayList<>(temp); // On en fait une liste
				Collections.sort(suivant); // Que l'on trie
				if (!suivant.isEmpty()) { // Si la liste n'est pas vide
					String nomTemp = suivant.get(0); // On va cree le nom de notre etat voisin par la lettre a 
					for(int c = 1; c<suivant.size() ;++c) { // Qui est une composition de tous les noms des etats qui le compose
						nomTemp+="."+suivant.get(c); // Par exemple 2.4
					}
					
					etatN.ajoutTransition(new Transition(nomTemp, a)); // On a notre transition pour la lettre a
					// Transition qui est donc unique pour chaque lettre

					if ( trouveEtats(tableTransition,Arrays.asList(nomTemp)).isEmpty()) { // Si le nouvelle etat n'est pas encore dans notre automate resultat
						file.add(suivant); // On l'ajoute a la file pour cree son etat plus tard
					}
				}				
			}	
			if (trouveEtats(tableTransition,Arrays.asList(etatN.getNom())).isEmpty() ) // Si le nouvelle etat que l'on viens de cree n'est pas encore dans notre automate resultat
				tableTransition.add(etatN); // Alors on l'ajoute
		}	
		
		
		return new Automate(renommer(tableTransition),this.regexp);
	}

	
	/**
	 * Methode qui renvoie tous l'alphabet utiliser par un automate.
	 * 
	 * @return La liste des lettre contenue dans l'alphabet
	 */
	private List<String> getAlphabet() {
		Set<String> resultat = new HashSet<>(); // On fait un set pour ne pas avoir de doublon
		for (Etat t : this.tableTransition) { // Pour chauque etat
			resultat.addAll(t.getAlphabet()); // On ajout son alphabet
		}
		return new ArrayList<>(resultat);
	}
	
	
	/**
	 * Methode qui renvoie les etats correspond a une liste de noms.
	 * equivalent a un getter pour les Set
	 * 	
	 * @param set, l'ensemble dans lequel on cherche
	 * @param noms, les noms des etats que l'on cherche
	 * @return Un liste des etats qui correspond au noms donne en paramettre.
	 */
	private List<Etat> trouveEtats(Set<Etat> set , List<String> listeNoms) {
		List<Etat> resultat = new ArrayList<>();
		for (Etat t : set) { // Pour chaque etat de l'automate
			for (String nom : listeNoms) { // Pour chaque noms
				if (t.equals(nom)) resultat.add(t.getEtat()); // Si le nom est celuis de l'etat alors on l'ajoute au resultat
			}
		}
		return resultat;
	}
	

	/**
	 * Methode pour renommer les etats d'un automate au plus simple.
	 * C'est a dire supprimer les etats qui ont pour noms n1.n2,
	 * Ce qui peux arriver lors de la determinisation de l'automate
	 * Pour ne plus avoir que des nom entre 0 et n.
	 * 
	 * @param tableTransition, le set d'etat a renommer
	 * @return Le même set d'etat, avec les mêmes transitions, a un renommage pres
	 */
	private Set<Etat> renommer(Set<Etat> tableTransition) {
		
		// On cree un tableau d'etats, les indice du tableau serviront a renommer les etats a la fin
		Etat [] resultatEtat = new Etat[tableTransition.size()];
		
		// La HashMap nous donnera pour chaque nom d'etat sa position dans le tableau resultatEtat
		Map<String, String> hm = new HashMap<>();  
		
		int i = 1; // les Indices dans le tableau, on ne commence pas a 0, car 0 est reserve pour l'etat Initial
		for (Etat e : tableTransition) { // Pour chaque etat de l'automate, on va l'ajouter a notre tableau de renommage
			if (e.estInitial()) // Si c'est l'etat inital
				hm.put(e.getNom(), "0"); // On lui assigne la case 0
			else // Sinon, On lui assigne une autre case,
				hm.put(e.getNom(), String.valueOf(i++)); // On rentre nos etat dans la hashmap, pour garder l'information de la case
			resultatEtat[Integer.parseInt(hm.get(e.getNom()))] = new Etat(hm.get(e.getNom()),e.estInitial(),e.estFinale()); // et finalement dans le tableau
		}
		
		for (Etat e : tableTransition) { // On va reparcourrir nos etats
			for (Transition t : e.getTransition()) { // Pour chaque transition
				resultatEtat[Integer.parseInt(hm.get(e.getNom()))].ajoutTransition( // On trouve le bon etat renommer correspondant 
						new Transition(hm.get(t.getEtat()), t.getEtiquette())); // On lui ajout la transition, avec l'etat finale bien renommer
			}
		}
		
		return new HashSet<Etat>(Arrays.asList(resultatEtat));
	}
	
	
	/**
	 * Methode pour reverser, inverser, l'automate.
	 * C'est a dire que les etats finale devienne initial et inversement,
	 * Et pour chaque transition, l'etat de depart devient finale et inversement.
	 * 
	 * @return L'automate renverse
	 */
	public Automate transposition() {
		
		Set<Etat> resultatEtat = new HashSet<>(); // Notre set des Etats qui sera notre nouveau automate
		
		for (Etat etat : this.tableTransition) { // On va d'abord remplir notre set resultat d'etats
			resultatEtat.add(new Etat(etat.getNom(),(etat.estFinale()),(etat.estInitial()))); // On prend soins d'inverser finale et initiale
		}
		
		// On reparcour les etats de notre automate
		for (Etat etat : this.tableTransition) { // Mais on s'interesse a leur transition
			for (Transition tran : etat.getTransition()) { // Pour chaque transition, on inverse les etat de depart et d'arrive
				trouveEtats(resultatEtat, Arrays.asList(tran.getEtat())).get(0).ajoutTransition( // et on ajout la nouvelle 
						new Transition(etat.getNom(), tran.getEtiquette()));// Transition dans notre set resultatEtat
			}
		}
		// On renvoie le nouvel automate inverse.
		return new Automate(resultatEtat,this.regexp);
	}
	
	
	/**
	 * 
	 * Algorithme de Brzozowski, qui minimise un automate.
	 * La procedure de minimisation
	 * -inverser l'automate,
	 * -determiniser
	 * -re-inverser
	 * -re-determiniser
	 * 
	 * @return L'automate minimale
	 */
	public Automate brzozowski() {
		return this.transposition().getDeterministe().transposition().getDeterministe();
	}
	
	/**
	 * Methode qui complete l'automate this
	 * En ajoutant un puit ou toute les etiquettes manquante y finissent
	 * sans pouvoir aller vers un autre etat
	 * 
	 * @return l'automate complet
	 */
	public Automate getAutomateComplet() {
		
		Set<Etat> resultatEtat = new HashSet<>(); 
		List<String> alphabet = getAlphabet();
		
		String nomPuit = String.valueOf(this.tableTransition.size());

		Etat puit = new Etat(nomPuit,false,false); // On Construit le puit
		// qui sera la destination de toutes les transition qui'il faut pour complete l'automate
		
		for (String a : alphabet) {
			puit.ajoutTransition(new Transition(nomPuit,a)); // Le puit ne peux que aller vers lui même
		}
		
		resultatEtat.add(puit);
		
		for (Etat etat : this.tableTransition) {
			Etat nouvelleEtat = new Etat(etat.getNom(),etat.estInitial(),etat.estFinale());

			Set<String> transitionExiste = new HashSet<>(); // On ajoutera les transitions qui existe deja dans cette ensemble
			for (Transition t : etat.getTransition()) {
				nouvelleEtat.ajoutTransition(new Transition(t.getEtat(), t.getEtiquette()));
				transitionExiste.add(t.getEtiquette());
			}
			
			for (String a : alphabet) { // Pour chaque lettre 
				if (!transitionExiste.contains(a)) // Si la transition n'existe pas 
					nouvelleEtat.ajoutTransition(new Transition(nomPuit,a)); // Alors on ajouts l'etiquettes qui manque vers le puit
			}
			resultatEtat.add(nouvelleEtat); // On ajout l'etat bien construit dans notre set resultat

		}
		
		
		return new Automate(resultatEtat,this.regexp);
	}
	
	/**
	 * Methode pour obtenir le monoide de transition d'un automate complet et deterministe
	 * On suppose que l'automate a ete renommer selon le procede de renommer()
	 * Donc le nom des n etats est compris entre 0 et n-1
	 * 
	 * @return le monoide de transition
	 */
	public MonoideTransition getMonoideTransition() {
		
		MonoideTransition monoide = new MonoideTransition();
		
		ArrayList<Etat> listeEtat = new ArrayList<Etat>(this.tableTransition);
		Collections.sort(listeEtat, new Comparator<Etat>() { 
	        public int compare(Etat e1, Etat e2) { 
	            return  e1.nom.compareTo(e2.nom); 
	        }
	    });		
		getMonoideAux(monoide, listeEtat, Arrays.asList(""), getAlphabet());
		
		return monoide;
	}
		
	public void getMonoideAux(MonoideTransition monoide, List<Etat> listeEtat, List<String> mot, List<String> alphabet) {
		
		List<String> correspond = new ArrayList<>(listeEtat.size());
		String indiceEtat = "0";
		for (int i = 0; i<listeEtat.size(); ++i) {
			indiceEtat = String.valueOf(i);
			for (int j = 0; j<mot.size(); ++j) {
				List<String> temp = listeEtat.get(Integer.parseInt(indiceEtat)).getTransitionAvecEtiquette(mot.get(j));
				if (temp.size() != 0) // Si l'automate est complet et deterministe, temp devrais toujours etre de taille 1
					indiceEtat = temp.get(0);
			}
			correspond.add(indiceEtat);
		}
		if (!monoide.contient(correspond)) { // Si on a pas encore rencontré la liste correspondante
			monoide.ajoute(String.join("", mot), correspond); // on ajout la correspondance au monoide
		
			for (String a : alphabet) { // Pour chaque lettre de l'alphabet
				List<String> nouveauMot = new ArrayList<>(mot);
				nouveauMot.add(a); // On va créé un nouveau mot
				getMonoideAux(monoide,listeEtat,nouveauMot,alphabet); // Et continuer a chercher les prochaine correspondance
			}
		}
		else { // Sinon
			monoide.ajouteRegle(String.join("", mot),correspond); // On ajout la regle au monoide
		} // on sait aussi que l'on a pas besoin de continuer a chercher plus dans cette direction, car on se rapportera toujours a des regle connue maintenant
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nRegExp : "+ this.regexp);
		
		for (Etat e : this.tableTransition) {
			sb.append("\n"+e.toString());
		}
		
		return sb.toString();
	}
	
}
