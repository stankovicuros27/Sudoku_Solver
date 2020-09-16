package solver;

import java.util.Arrays;

public class Single implements Group {
	
	private int single[] = new int[1];
	private static final int SIZE = 1;
	
	public Single(int first) {
		super();
		single[0] = first;
	}
	
	@Override
	public int[] getValues() {
		return single;
	}

	@Override
	public boolean contains(int value) {
		return (single[0] == value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Single other = (Single) obj;
		if (!Arrays.equals(single, other.single))
			return false;
		return true;
	}
	
	@Override
	public int size() {
		return SIZE;
	}
	
	@Override
	public String toString() {
		return "Single";
	}
}
