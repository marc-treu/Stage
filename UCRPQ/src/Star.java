
public class Star implements RegExp {

	RegExp child;

	@Override
	public Type type() {
		return Type.Star;
	}
	
}
