import java.util.ArrayList;
import java.util.List;

public class Concatenation extends NAry {

	
	public Concatenation(RegExp... args) {
		
		this.children = new ArrayList<RegExp>();
		
		for(int i=0;i<args.length;++i){
			this.children.add(args[i]);
		}	
	}	
	
	@Override
	public Type type() {
		return Type.Concatenation;
	}

	@Override
	public List<RegExp> children() {
		return this.children;
	}

	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.children.get(0).toString());
		
		for(int i=1 ;i<this.children.size();++i) {
			sb.append(".");
			sb.append(this.children.get(i).toString());
		}
		sb.append(")");
		return sb.toString();
	}
	
}
