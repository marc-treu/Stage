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
	 * 			est developper 
	 */
	public static List<RegExp> extractUnion(RegExp query){
		
		if(query.isCypherable()) {
			return Arrays.asList(query);
		}
		
		List<RegExp> resultat = new ArrayList<>();
		
		switch (query.type()) {
			case Concatenation:
				return extractUnionFromConca(query);
				//break;
			case Union:
				for(RegExp e : query.children()){
					resultat.add(e);
				}
				break;			
			default:
				return Arrays.asList(query);
		}
		
		return resultat;
	}
	
	public static List<RegExp> extractUnionFromConca(RegExp query){

		int i = 0;
		List<RegExp> resultat = new ArrayList<>();
		
		while (i<query.children().size()){
			
			// On ne s'interresse pas au cas de l'étoile pour l'instant
			// Il ne reste que le cas de l'union a traiter donc
			// Car l'Atom n'a pas besoin de transformation, et la Concatenation 
			// Ne pas exister grace a la fonction flatten
			if(query.children().get(i).type()==Type.Union) {// Si on trouve une union d'indice i
				
				RegExp[] temp = new RegExp[query.children().size()];
				
				// On va la séparer grace a getCypherable() dans Union
				for(RegExp e : query.children().get(i).children()) {
					// Et pour chaque nouvelle expression on va recree une nouvelle RegExp
					for (int j = 0;j<query.children().size();++j) {
						if (j!=i) // en faisant bien attention a garder le meme ordre 
							temp[j]=query.children().get(j);
						else
							temp[j]=e;
						
					}
					// créant ainsi une nouvelle Concatenation 
					resultat.add(new Concatenation(temp));
				}
				// On retourne notre liste de Concatenation 
				return resultat;				
			}
			++i;
		}
		return null;
	}
	
	public static List<RPQ> rewriteRPQ(RPQ query){
		List<RPQ> resultat = new ArrayList<>();
		for(RegExp e : extractUnion(query.expression)) {
			if(!e.isCypherable()) 
				resultat.addAll(rewriteRPQ(new RPQ(query.origin,e,query.destination)));
			else
				resultat.add(new RPQ(query.origin,e,query.destination));
		}
		return resultat;
	}
	
}
