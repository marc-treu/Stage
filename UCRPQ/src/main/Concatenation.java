package main;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Concatenation extends NAry {

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
	
	@Override
	public List<RegExp> getCypherable() {
		int i = 0;
		while (i<this.children.size()){
			
			if(this.children.get(i).type()==Type.Union) {
				List<RegExp> resultat = new ArrayList<>();
				RegExp[] temp = new RegExp[this.children.size()];
				for(RegExp e : this.children.get(i).getCypherable()) {
					for (int j = 0;j<this.children.size();++j) {
						if (j!=i)
							temp[j]=this.children.get(j);
						else
							temp[j]=e;
						
					}
					resultat.add(new Concatenation(temp));
				}
				return resultat;				
			}
			++i;
		}
		return null;
	}

}
