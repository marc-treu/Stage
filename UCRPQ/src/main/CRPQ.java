package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CRPQ {

	List<RPQ> children;
	
	public CRPQ(List<RPQ> children) {
		this.children = children;
	}
	
	
	
	public String toCypher() {
		StringBuilder sb = new StringBuilder();
		
		if(this.isCypherable()) {
			Set<String> hs = new HashSet<String>();
			sb.append("MATCH (");

			for ( RPQ e : this.children) {
				sb.append(e.origin+")"+e.expression.toCypher()+"("+e.destination+")");
				hs.add(e.origin);
				hs.add(e.destination);
			}

			Iterator<String> it = hs.iterator();
			
			sb.append("\nRETURN "+it.next());
			while (it.hasNext()) {
				sb.append(", "+it.next());
			}
		}else {
			sb.append("La CRPQ doit etre modifier");
		}
		
		return sb.toString();
	}
	
	public boolean isCypherable() {
		for (RPQ e : this.children) {
			if (!e.isCypherable())
				return false;
		}
		return true;
	}
	
}
