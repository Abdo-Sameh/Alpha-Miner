import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Pair {

	private Set<String> left;
	private Set<String> right;

	Pair() {
		setLeft(new HashSet<String>());
		setRight(new HashSet<String>());
	}

	Pair(Set<String> left, Set<String> right) {
		this.left = left;
		this.right = right;
	}

	public Set<String> getLeft() {
		return left;
	}

	public void setLeft(Set<String> left) {
		this.left = left;
	}

	public Set<String> getRight() {
		return right;
	}

	public void setRight(Set<String> right) {
		this.right = right;
	}

	public boolean isIdenticalHashSet(Set<String> h1, Set<String> h2) {
		if (h1.size() != h2.size()) {
			return false;
		}
		Set<String> clone = new HashSet<String>(h2); // just use h2 if you don't
														// need to save the
														// original h2
		Iterator<String> it = h1.iterator();
		while (it.hasNext()) {
			String s = it.next();
			if (clone.contains(s)) { // replace clone with h2 if not concerned
										// with saving data from h2
				clone.remove(s);
			} else {
				return false;
			}
		}
		return true; // will only return true if sets are equal
	}

	public boolean equal(Pair p) {
		if (isIdenticalHashSet(this.getLeft(), p.left) && isIdenticalHashSet(this.getRight(), p.right)) {
			return true;
		} else
			return false;

	}

	public String getPair() {
		String out1 = "{ ", out2 = "{ ";
		for (String s : left) {
			out1 += s + ",";
		}
		out1 += " }";
		out1 = out1.replace(", }", " }");
		
		for (String s : right) {
			out2 += s + ",";
		}
		out2 += " }";
		out2 = out2.replace(", }", " }");
		return "(" + out1 + ", " + out2 + ")";
	}
}
