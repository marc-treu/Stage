import java.util.ArrayList;

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

}
