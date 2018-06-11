import java.util.List;

public class Union extends NAry {
	
	
	public Union(RegExp... args) {
		super("+",args);
	}
	
	@Override
	public Type type() {
		return Type.Union;
	}

	@Override
	public List<RegExp> children() {
		return this.children;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public String toCypher() {
		StringBuilder sb = new StringBuilder();
		sb.append( ((Atom) this.children.get(0)).getDirection() ? "-[:"	: "<-[:");

		sb.append(((Atom)this.children.get(0)).getEtiquette());
		
		for(int i=1 ;i<this.children.size();++i) {
			sb.append("|");
			sb.append(((Atom)this.children.get(i)).getEtiquette());
		}

		
		sb.append(((Atom) this.children.get(0)).getDirection() ? "]->" : "]-");
		return sb.toString();
	}

}
