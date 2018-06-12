import java.util.ArrayList;
import java.util.List;

public class Concatenation extends NAry {

	/*List<RegExp> children;
	String separator;*/

	public Concatenation(RegExp... args) {
		super(".",args);
	}
	
	@Override
	public Type type() {
		return Type.Concatenation;
	}

	@Override
	public List<RegExp> children() {
		return this.children;
	}

// 	@Override
// 	public String toString() {
// 		return super.toString();
// 	}
//
	public String toCypher() {

		StringBuilder sb = new StringBuilder();
		sb.append(this.children.get(0).toCypher());

		for(int i=1 ;i<this.children.size();++i) {
			sb.append("()");
			sb.append(this.children.get(i).toCypher());
		}

		return sb.toString();
	}

	@Override
	public boolean isCypherable() {

		for (RegExp e : this.children) {
			if(!(e.isCypherable())) {
				return false;
			}
		}
		return true;
	}

	
	public Concatenation flatten() {
		
		List<RegExp> le = new ArrayList<RegExp>();
		
		for (RegExp e : this.children) {
			if(e.type()==RegExp.Type.Concatenation) {
				RegExp t = e.flatten();
				for (RegExp ee : t.children()) {
					le.add(ee.flatten());
				}
			}
			else
				le.add(e);
		}
		return new Concatenation(le.toArray(new RegExp [le.size()]));
	}

}
