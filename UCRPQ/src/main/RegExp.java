package main;
import java.util.List;

public interface RegExp {

	public enum Type{
                Epsilon,
		Atom,
		Union,
		Concatenation,
		Star;
	}

	public Type type();

	public List<RegExp> children();

	public String toCypher();

	public boolean isCypherable();

	public RegExp flatten();

	public List<String> getEtiquette();

	/**
	 * Donne la taille d'une RegExp, ie, le nombre de lettre du langage
	 * 
	 * @return la taille de l'expression
	 */
	public int getLength();
}
