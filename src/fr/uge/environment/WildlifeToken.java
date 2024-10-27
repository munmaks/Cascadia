package fr.uge.environment;

//import java.io.IO;

public record WildlifeToken(WildlifeType animal) {

  public WildlifeToken {
    if (animal == null) {
      throw new IllegalArgumentException("Animal cannot be null");
    }
  }

  @Override
  public String toString() {
    return "WildlifeToken: animal is " + animal;
  }
   
//  var bearToken = new WildlifeToken(WildlifeType.BEAR);
//  
//  IO.println(bearToken);
//  IO.println("\n\n" + bearToken.animal());

}
