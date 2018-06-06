import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Star implements RegExp {

	RegExp child;

	@Override
	public Type type() {
		return Type.Star;
	}

	@Override
	public List<RegExp> children() {
		return new ArrayList<>(Arrays.asList(this.child));
	}
	
}