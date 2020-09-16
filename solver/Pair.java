package solver;

import java.util.Arrays;

public class Pair implements Group {

	private int[] pair = new int[2];
	private static final int SIZE = 2;

	public Pair(int first, int second) {
		super();
		pair[0] = first;
		pair[1] = second;
		Arrays.sort(pair);
	}

	@Override
	public int[] getValues() {
		return pair;
	}

	@Override
	public boolean contains(int value) {
		int index = Arrays.binarySearch(pair, value);
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
		Pair other = (Pair) obj;
		if (!Arrays.equals(pair, other.pair))
			return false;
		return true;
	}

	@Override
	public int size() {
		return SIZE;
	}

	@Override
	public String toString() {
		return "Pair";
	}
}
