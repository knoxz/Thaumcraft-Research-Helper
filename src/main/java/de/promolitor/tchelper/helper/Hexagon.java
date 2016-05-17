package de.promolitor.tchelper.helper;

public class Hexagon {

	public int q = 0;
	public int r = 0;
	public String aspect;
	public boolean visited = false;
	private boolean notDeletable = false;

	public Hexagon(int q, int r, String aspect) {
		this(q, r, aspect, false);
	}

	public Hexagon(int q, int r, String aspect, boolean notDeletable) {
		this.q = q;
		this.r = r;
		this.aspect = aspect;
		this.notDeletable = notDeletable;
	}

	public Hexagon getNeighbour(int direction) {
		int[] d = AspectCalculation.NEIGHBOURS[direction];
		return new Hexagon(this.q + d[0], this.r + d[1], "");
	}

	public boolean isNotDeleteable() {
		return notDeletable;
	}

	public String toString() {
		return aspect;

	}

}
