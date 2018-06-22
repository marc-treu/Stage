package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Star implements RegExp {

	RegExp child;
	int borne_sup;
	
	/**
	 * Construteur qui permet de rajouter une borne superieur a notre étoile
	 * 
	 * @param child l'éxpression 
	 * @param borne_superieur La borne superieur de l'étoile
	 */
	public Star(RegExp child) {
		this.child=child;
	}
			

<<<<<<< HEAD
	/**
	 * Le constructeur pour l'étoile classique, non borné.
	 * Par convention on va mettre -1 comme borne supérieur
	 * 
	 * @param child l'éxpression sous l'étoile
	 */
=======
	
>>>>>>> Cyphermorphim2
	public Star(RegExp child) {
		this(child);
	}
	
	@Override
	public Type type() {
		return Type.Star;
	}

	@Override
	public List<RegExp> children() {
		return new ArrayList<>(Arrays.asList(this.child));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.child.type()==RegExp.Type.Atom ? "("+this.child.toString()+")" : this.child.toString());
		sb.append(this.borne_sup > 0 ? "*.."+this.borne_sup : "*");
		return sb.toString();
	}

	@Override
	public String toCypher() {
		StringBuilder sb = new StringBuilder();
		if(this.child.type()==RegExp.Type.Atom) {// si on a seulement un Atom
			sb.append( ((Atom)this.child).getDirection()==false ? "<-[:" : "-[:");
			sb.append( ((Atom)this.child).getEtiquette());	
			sb.append(this.borne_sup > 0 ? "*.."+this.borne_sup : "*");
			sb.append( ((Atom)this.child).getDirection()==true ? "]->" : "]-");
		}
		else // si on a une Union d'Atom	
			sb.append( ((Union)this.child).toCypher(true,this.borne_sup));
		return sb.toString();
	}

	@Override
	public boolean isCypherable() {
		// Si l'étoile est au desous de la Concatenation,
		// alors la requête est n'est pas exprimable
		if(this.child.isCypherable() && this.child.type()!=RegExp.Type.Concatenation)
			return true;
		return false;
	}

	@Override
	public Star flatten() {
		if(this.child.type()==RegExp.Type.Star)// Si on a Star(Star(...))
			return new Star( ((Star)this.child).getChild().flatten());
		
		return this.borne_sup > 0 ? new Star(this.child.flatten(),this.borne_sup) : new Star(this.child.flatten());
	}
	
	public RegExp getChild(){
		return this.child;
	}

}