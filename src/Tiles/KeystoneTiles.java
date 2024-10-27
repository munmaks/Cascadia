package Tiles;

import java.util.ArrayList;

public class KeystoneTiles implements Tiles {
	public ArrayList<KeystoneTile> keystonetile = new ArrayList<KeystoneTile>();
	public void add(KeystoneTile tilegeneric) {
		keystonetile.add(tilegeneric);
	}
	public String toString() {
		String res ="";
		res += keystonetile.toString();
		return res;
	}
	public static void main(String[] args) {
		var keystones = new KeystoneTiles();
		keystones.add(new KeystoneTile("River", "Bear"));
		keystones.add(new KeystoneTile("River", "Bear"));
		keystones.add(new KeystoneTile("River", "Salmon"));
		keystones.add(new KeystoneTile("River", "Hawk"));
		keystones.add(new KeystoneTile("River", "Hawk"));
		keystones.add(new KeystoneTile("Wetland", "Salmon"));
		keystones.add(new KeystoneTile("Wetland", "Salmon"));
		keystones.add(new KeystoneTile("Wetland", "Hawk"));
		keystones.add(new KeystoneTile("Wetland", "Fox"));
		keystones.add(new KeystoneTile("Wetland", "Fox"));
		keystones.add(new KeystoneTile("Forest", "Bear"));
		keystones.add(new KeystoneTile("Forest", "Bear"));
		keystones.add(new KeystoneTile("Forest", "Fox"));
		keystones.add(new KeystoneTile("Forest", "Fox"));
		keystones.add(new KeystoneTile("Forest", "Elk"));
		keystones.add(new KeystoneTile("Prarie", "Elk"));
		keystones.add(new KeystoneTile("Prarie", "Elk"));
		keystones.add(new KeystoneTile("Prarie", "Salmon"));
		keystones.add(new KeystoneTile("Prarie", "Salmon"));
		keystones.add(new KeystoneTile("Prarie", "Fox"));
		keystones.add(new KeystoneTile("Mountain", "Bear"));
		keystones.add(new KeystoneTile("Mountain", "Elk"));
		keystones.add(new KeystoneTile("Mountain", "Elk"));
		keystones.add(new KeystoneTile("Mountain", "Hawk"));
		keystones.add(new KeystoneTile("Mountain", "Hawk"));
		System.out.println(keystones);
	}
}
