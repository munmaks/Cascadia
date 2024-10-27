package fr.uge.Tiles;

import java.util.ArrayList;

public class StarterHabitatTile implements Tiles{
	public ArrayList<TileGeneric> starterhabitattiles = new ArrayList<TileGeneric>();
	public void add(TileGeneric tilegeneric) {
		starterhabitattiles.add(tilegeneric);
	}
	public String toString() {
		String res ="";
		res += starterhabitattiles.toString();
		return res;
	}
}

