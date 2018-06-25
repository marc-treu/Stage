package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.RegExp.Type;

public class RewritingRules {

	/**
	 * La fonction va supprimer les Unions en les remontant dans l'expression,
	 * En Cypher, une union pouvant etre seulement exprimer au plus bas niveau -[:A|B]-
	 * Ou au plus haut avec UNION entre deux MATCH, dans les autre cas l'union n'est pas
	 * Expressible   
	 * 
	 * @param query, l'expression a rendre expressible en Cypher
	 * @return Une liste de RegExp ou le premiere Union rencontré non Cypherable
	 * 			est developper, ou sinon la requête est simplement renvoyer dans une liste
	 */
	private static List<RegExp> extractUnion(RegExp query){
		
		if(query.isCypherable()) {// Si on est deja Cypherisable, on retourne tel quel la query
			return Arrays.asList(query);
		}
		
		List<RegExp> resultat = new ArrayList<>();
		
		switch (query.type()) {
			case Concatenation:
				return extractUnionFromConca(query);
			case Union:
				for(RegExp e : query.children()){
					resultat.add(e);
				}
				break;			
			default:// Dans les autre cas Atom, Star on ne peux pas decomposer
				return Arrays.asList(query);// On retourne donc la query
		}
		return resultat;
	}
	
	/**
	 * Fonction auxilere de extractUnion, qui s'occuper du cas ou l'on rencontre une Concatenation
	 * Si on a:
	 * 	(A+B).C  , avec A, B et C des RegExp
	 * La fonction retournera la liste 
	 * [ A.C , B.C ]
	 * 
	 * @param query
	 * @return Une liste de RegExp ou la premier Union trouvé est remonter dans la requête
	 */
	private static List<RegExp> extractUnionFromConca(RegExp query){

		int i = 0;
		List<RegExp> resultat = new ArrayList<>();
		
		while (i<query.children().size()){
			
			// On ne s'interresse pas au cas de l'étoile pour l'instant
			// Il ne reste que le cas de l'union a traiter donc
			// Car l'Atom n'a pas besoin de transformation, et la Concatenation 
			// sou la Concatenation ne pas exister grace a la fonction flatten
			if(query.children().get(i).type()==Type.Union) {// Si on trouve une union d'indice i
				
				RegExp[] temp = new RegExp[query.children().size()];
				
				// On va pour chaque enfant de l'Union
				for(RegExp e : query.children().get(i).children()) {
					// Et pour chaque autre expression presente dans la requête differente de l'indice i
					for (int j = 0;j<query.children().size();++j) {
						if (j!=i) // les ajouter des un tableau
							temp[j]=query.children().get(j);
						else
							temp[j]=e;
						
					}
					// pour créé ainsi une nouvelle Concatenation 
					resultat.add(new Concatenation(temp));
				}
				// On retourne notre liste de Concatenation 
				return resultat;				
			}
			++i;
		}
		// Dans le cas ou il n'y a pas d'Union sous notre Concatenation, on renvoie la query sous forme de liste
		return Arrays.asList(query);
	}
		
	/**
	 * A partir d'une RPQ, essaie de la réécrire de tel sorte que soit Cypherable
	 * 
	 * @param query a traduire
	 * @return Une liste de RPQ traduite, mais pas necessairement Cypherable
	 */
	public static List<RPQ> rewriteRPQ(RPQ query){
		List<RPQ> resultat = new ArrayList<>();
		List<RegExp> temp = extractUnion(query.expression);//On essaie d'extraire des Unions
		
		if(temp.size()==1) // Si il n'y a rien, alors on ajoute simplement la requête au resultat
			resultat.add(new RPQ(query.origin,query.expression,query.destination));
		else {// Sinon, pour chaque sous requête
			for (RegExp e : temp) {// On va faire un appel reccursif de la fonction de réécriture
				resultat.addAll(rewriteRPQ(new RPQ(query.origin,e,query.destination)));// 
			}
		}
		return resultat;
	}

	/**
	 * A partir d'une CRPQ, essaie de la réécrire de tel sorte que soit Cypherable
	 * 
	 * @param query a traduire
	 * @return Une liste de CRPQ traduite, mais pas necessairement Cypherable
	 */
	public static List<CRPQ> rewriteCRPQ(CRPQ query) {
		List<CRPQ> resultat = new ArrayList<>();
		List<List<RPQ>> temp = new ArrayList<>();
		
		
		for(RPQ r : query.children) {// Pour chaque RPQ de notre CRPQ
			temp.add(rewriteRPQ(r));// On tente de la réécrire 
		}
		// On fait appel a la fonction getCombinaison(), pour avoir toute les combinaisons 
		getCombinaison(temp,resultat,0,new ArrayList<RPQ>());// de RPQ dans les CRPQ
		return resultat;
	}
	
	/**
	 * Fonction auxiliere de rewriteCRPQ, qui permet de correctement obtenir les combinaisons
	 * de RPQ pour chaque CRPQ
	 * Par exemple:
	 * (x,a+(b.c),y) & (x,d,y)
	 * va etre réécrie comme 
	 * [[a.b,a.c],[d]] par la boucle de rewriteCRPQ
	 * 
	 * et va donné a la fin du passage de getCombinaison
	 * [a.b.d,a.c.d]
	 * 
	 * @param temp La liste de Liste de RPQ réécrite
	 * @param resultat La liste correctement formé des CRPQ
	 * @param depth la profondeur dans la liste des RPQ réécrite
	 * @param rpqs La liste de RPQ en formation, correctement formé qui sera transformé en CRPQ a la fin
	 */
	private static void getCombinaison(List<List<RPQ>> temp, List<CRPQ> resultat, int depth, List<RPQ> rpqs) {
		if(depth==temp.size()) {
			resultat.add(new CRPQ(rpqs));
			return;
		}
		for(int i=0 ;i<temp.get(depth).size(); ++i) {
			List<RPQ> temps_rpqs = new ArrayList<RPQ>();
			temps_rpqs.addAll(rpqs);
			temps_rpqs.add(temp.get(depth).get(i));
			getCombinaison(temp, resultat, depth+1, temps_rpqs);
	
		}
	}

	/**
	 * A partir d'une UCRPQ, essaie de la réécrire de tel sorte que soit Cypherable
	 * 
	 * @param query a traduire
	 * @return Une UCRPQ traduite, mais pas necessairement Cypherable
	 */
	public static UCRPQ rewriteUCRPQ(UCRPQ query) {
		List<CRPQ> resultat = new ArrayList<>();

		for(CRPQ cr : query.children) {
			resultat.addAll(rewriteCRPQ(cr));
		}
		
		return new UCRPQ(resultat);
	}
	
}
