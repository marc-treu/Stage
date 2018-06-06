import java.util.ArrayList;
import java.util.List;

public class Union extends NAry {
	
	
	public Union(RegExp... args) {
		
		this.children = new ArrayList<RegExp>();
		
		for(int i=0;i<args.length;++i){
			this.children.add(args[i]);
		}	
	}
	
	@Override
	public Type type() {
		return Type.Union;
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
			sb.append("+");
			sb.append(this.children.get(i).toString());
		}
		sb.append(")");
		return sb.toString();
	}

}
