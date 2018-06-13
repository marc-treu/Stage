import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Union extends NAry {

	int direction=-1; // 0 si inverser, 1 si normale, 2 si non orienté et -1 si non défini
	
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
	public String toCypher() {
		StringBuilder sb = new StringBuilder();

		if(this.direction==-1)// Si la direction n'est pas encore defini, normalement, isCypherable() est appeler avant toCypher()
			this.isCypherable();// La direction des atoms est donnée dans isCypherable()
		
		sb.append( this.direction==0 ? "<-[:" : "-[:");

		sb.append(((Atom)this.children.get(0)).getEtiquette());
		
		for(int i=1 ;i<this.children.size();++i) {
			sb.append("|");
			sb.append(((Atom)this.children.get(i)).getEtiquette());
		}

		
		sb.append( this.direction==1 ? "]->" : "]-");
		return sb.toString();
	}

	@Override
	public boolean isCypherable() {
		
		//Iniatialisation
		Map<String, Integer> hm = new HashMap<>();
		String tmp_etiquette = "\0";
		
		for (RegExp e : this.children) {
			if(e.type()!=RegExp.Type.Atom) 
				return false;
			
			tmp_etiquette =((Atom)e).getEtiquette();
			
			if(hm.containsKey(tmp_etiquette)) { // si l'etiquette est deja dans le set
				if (hm.get(tmp_etiquette).equals(((Atom)e).getDirection() ? 1 : 0 )== false 
							&& hm.get(tmp_etiquette)!=2)
					hm.put(tmp_etiquette, 2);
			}
			else 
				hm.put(tmp_etiquette, ((Atom)e).getDirection() ? 1 : 0);			
		}			

		// On teste si tous les Atom sont dans la même direction
		for (Map.Entry<String, Integer> entry : hm.entrySet()) {
			if (entry.getValue() != hm.get(tmp_etiquette)){
				return false;
			}
		}
		
		// On initialisation la direction des etiquettes 
		this.direction = hm.get(tmp_etiquette);
		
		return true;
	}
	
}
