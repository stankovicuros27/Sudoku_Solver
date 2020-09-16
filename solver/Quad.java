package solver;

import java.util.Arrays;

public class Quad implements Group {
	
	private int[] quad = new int[4];
	private static final int SIZE = 4;
	
	public Quad(int first, int second, int third, int fourth) {
		super();
		quad[0] = first;
		quad[1] = second;
		quad[2] = third;
		quad[3] = fourth;
		Arrays.sort(quad);
	}
	
	@Override
	public int[] getValues() {
		return quad;
	}

	@Override
	public boolean contains(int value) {
		int index = Arrays.binarySearch(quad, value);
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
		Quad other = (Quad) obj;
		if (!Arrays.equals(quad, other.quad))
			return false;
		return true;
	}
	
	@Override
	public int size() {
		return SIZE;
	}
	
	@Override
	public String toString() {
		return "Quad";
	}
}
