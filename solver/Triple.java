package solver;

import java.util.Arrays;

public class Triple implements Group {

	private int[] triple = new int[3];
    private static final int SIZE = 3;
	
	public Triple(int first, int second, int third) {
		super();
		triple[0] = first;
		triple[1] = second;
		triple[2] = third;
		Arrays.sort(triple);
	}
	
	@Override
	public int[] getValues() {
		return triple;
	}

	@Override
	public boolean contains(int value) {
		int index = Arrays.binarySearch(triple, value);
		return index >= 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triple other = (Triple) obj;
		if (!Arrays.equals(triple, other.triple))
			return false;
		return true;
	}
	
	@Override
	public int size() {
		return SIZE;
	}
	
	@Override
	public String toString() {
		return "Triple";
	}
}
