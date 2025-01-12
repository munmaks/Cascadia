package fr.uge.data.scoring;

// Hawks score by spreading out across the landscape and can earn points for
// each hawk, pairs of hawks, or lines of sight between hawks.
// Lines of sight are straight lines connecting flat sides of hexagons, and they
// are interrupted only by the presence of another hawk.
// There are card clarifications:
// - (A) Score an increasing number of points for each hawk not adjacent to any
// other hawk.
// - (B) Score an increasing number of points for each hawk not adjacent to any
// other hawk and having a direct line of sight to another hawk.
// - (C) Score 3 points for each line of sight between two hawks, with the
// possibility of multiple lines involving the same hawk.
// - (D) Score for each pair of hawks, with points increasing based on the
// number of unique animal types between them,
// excluding other hawks.Each hawk can only be part of one pair.

public final class HawkScoringCard implements WildlifeScoringCard {

  public HawkScoringCard() {

  }

}
