package Tiles;

import java.util.Objects;

public class KeystoneTile implements Tiles {
	String habitat;
	String animal;

	public KeystoneTile(String habitat, String animal) {
		this.habitat = habitat;
		this.animal = animal;
	}

	public String habitat() {
		Objects.requireNonNull(habitat);
		return habitat;
	}

	public String animal() {
		Objects.requireNonNull(animal);
		return animal;
	}
	public String toString() {
		String res="";
		res += "Habitat --> " + habitat() + " AND Animal --> " + animal();
		return res;
	}
}
