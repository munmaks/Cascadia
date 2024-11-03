package fr.uge.environment;

public record WildlifeToken(WildlifeType animal) {

  public WildlifeToken {

  }

  @Override
  public String toString() {
    return " " + animal;
  }

}
