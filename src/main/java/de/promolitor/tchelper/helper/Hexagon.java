package de.promolitor.tchelper.helper;

public class Hexagon {

	public int q = 0;
	public int r = 0;
	public String aspect;
	public boolean visited = false;

	public Hexagon(int q, int r, String aspect) {
		this.q = q;
		this.r = r;
		this.aspect = aspect;
	}

	public Hexagon getNeighbour(int direction) {
		int[] d = AspectCalculation.NEIGHBOURS[direction];
		return new Hexagon(this.q + d[0], this.r + d[1], "");
	}

	public String toString() {
		return aspect;

	}

}
