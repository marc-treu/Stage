import java.util.ArrayList;

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

}
