package fr.uge.Tiles;

import java.util.ArrayList;

public class AllStarterHabitatTile implements Tiles{
	public ArrayList<StarterHabitatTile> allstarterhabitattiles = new ArrayList<StarterHabitatTile>();
	public void add(StarterHabitatTile starterhabitattile1) {
		allstarterhabitattiles.add(starterhabitattile1);
	}
	public String toString() {
		String res ="";
		res += allstarterhabitattiles.toString();
		return res;
	}
	public static void main(String[] args) {
		var allstarterhabitattiles = new AllStarterHabitatTile();

		var starterhabitattile1 = new StarterHabitatTile();
		var starterhabitattile2 = new StarterHabitatTile();
		var starterhabitattile3 = new StarterHabitatTile();
		var starterhabitattile4 = new StarterHabitatTile();
		var starterhabitattile5 = new StarterHabitatTile();

		starterhabitattile1.add(new TileGeneric("River", "Salmon"));
		starterhabitattile1.add(new TileGeneric("Wetland Mountain", "Hawk Fox"));
		starterhabitattile1.add(new TileGeneric("Forest Prarie", "Bear Elk Salmon"));

		allstarterhabitattiles.add(starterhabitattile1);

		starterhabitattile2.add(new TileGeneric("Wetland", "Elk"));
		starterhabitattile2.add(new TileGeneric("Prarie Mountain", "Bear Fox"));
		starterhabitattile2.add(new TileGeneric("Forest River", "Elk Salmon Hawk"));

		allstarterhabitattiles.add(starterhabitattile2);

		starterhabitattile3.add(new TileGeneric("Forest", "Elk"));
		starterhabitattile3.add(new TileGeneric("Wetland Prarie", "Salmon Fox"));
		starterhabitattile3.add(new TileGeneric("River Mountain", "Bear Elk Hawk"));

		allstarterhabitattiles.add(starterhabitattile3);

		starterhabitattile4.add(new TileGeneric("Prarie", "Fox"));
		starterhabitattile4.add(new TileGeneric("Forest Mountain", "Bear Elk"));
		starterhabitattile4.add(new TileGeneric("River Wetland", "Salmon Hawk Fox"));

		allstarterhabitattiles.add(starterhabitattile4);

		starterhabitattile5.add(new TileGeneric("Mountain", "Bear"));
		starterhabitattile5.add(new TileGeneric("River Prarie", "Bear Salmon"));
		starterhabitattile5.add(new TileGeneric("Wetland Forest", "Elk Hawk Fox"));

		allstarterhabitattiles.add(starterhabitattile5);

		System.out.println(allstarterhabitattiles);
	}
}
