package main;
import java.util.ArrayList;
import java.util.List;

public abstract class NAry implements RegExp {
	
	List<RegExp> children;
	String separator;
	
	
	public NAry(String separator,RegExp... args) {
		this.separator = separator;
		this.children = new ArrayList<RegExp>();
		
		for(int i=0;i<args.length;++i){
			this.children.add(args[i]);
		}	
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.children.get(0).toString());
		
		for(int i=1 ;i<this.children.size();++i) {
			sb.append(this.separator);
			sb.append(this.children.get(i).toString());
		}
		sb.append(")");
		return sb.toString();
	}
	
	public RegExp flatten() {
		
		if(this.children.size()==1) 
			return this.children.get(0).flatten();
		
		List<RegExp> le = new ArrayList<RegExp>();
		
		for (RegExp e : this.children) {
			if(e.type()==(separator=="." ? RegExp.Type.Concatenation : RegExp.Type.Union)) {
				RegExp t = e.flatten();
				try {
					for (RegExp ee : t.children()) {
						le.add(ee.flatten());
					}
				}catch(UnsupportedOperationException exception) {
					le.add(t);
				}
			}
			else
				le.add(e.flatten());
		}
		return separator=="." ? new Concatenation(le.toArray(new RegExp [le.size()])) : new Union(le.toArray(new RegExp [le.size()])) ;	
	}
	
	
	public List<String> getEtiquette(){
		List<String> resultat = new ArrayList<>();
		
		for(RegExp e : this.children) {
			resultat.addAll(e.getEtiquette());
		}
		
		return resultat;
	}
	
	@Override
	public int getLength() {
		int taille = 0;
		
		for(RegExp e : this.children) {
			taille += e.getLength();
		}
		
		return taille;
	}
	
	@Override
	public RegExp getRename(int i) {
		
		List<RegExp> le = new ArrayList<RegExp>();

		for(RegExp e : this.children) {
			le.add(e.getRename(i));
			i += e.getLength();
		}
		
		return separator=="." ? new Concatenation(le.toArray(new RegExp [le.size()])) :  new Union(le.toArray(new RegExp [le.size()]));
	}

	

}
