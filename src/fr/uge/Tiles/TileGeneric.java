package fr.uge.Tiles;

import java.util.Objects;

public class TileGeneric implements Tiles {
	String habitat;
	String animal;

	public TileGeneric(String habitat, String animal) {
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

	@Override
	public String toString() {
		String res="";
		res += "Habitat --> " + habitat() + " AND Animal --> " + animal();
		return res;
	}
}

