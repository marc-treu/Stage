import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Star implements RegExp {

	RegExp child;

	public Star(RegExp child) {
		this.child=child;
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
		return "("+this.child.toString()+")*";
	}

	@Override
	public String toCypher() {
		StringBuilder sb = new StringBuilder();

		sb.append( ((Atom)this.child).getDirection()==false ? "<-[:" : "-[:");

		sb.append(((Atom)this.child).getEtiquette());
	
		sb.append( ((Atom)this.child).getDirection()==true ? "*]->" : "*]-");
		return sb.toString();
	}

	@Override
	public boolean isCypherable() {
		// Si l'étoile est au desous de la Concatenation,
		// alors la requêtte est n'est pas exprimable
		return this.child.type()==RegExp.Type.Concatenation ? false : true;
	}

	public Star flatten() {
		return new Star(this.child.flatten());
	}
	
}