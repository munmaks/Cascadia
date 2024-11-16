package fr.uge.scoring;

import java.util.Map;
import java.util.Objects;


import fr.uge.environment.Environment;
// import fr.uge.environment.;

import fr.uge.util.Constants;


public final class IntermediateScoringCard implements WildlifeScoringCard {

  private final Environment env;
  private final int version;
  private int group = 0;
  private int count = 0;

  private static final Map<Integer, Integer> GROUP_SIZE_TO_POINTS = Map.of(
    2, 5,
    3, 8,
    4, 12
  );


  public IntermediateScoringCard(Environment env, int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    this.env = Objects.requireNonNull(env);
    this.version = version;
  }

  
}
