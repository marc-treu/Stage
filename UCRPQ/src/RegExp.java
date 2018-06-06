
public interface RegExp {

	public enum Type{
		Atom,
		Union,
		Concatenation,
		Star;
	}
	
	public Type type();
	
}
