package fr.uge.scoring;

public sealed interface WildlifeScoringCard permits 
    BearScoringCard,
    FoxScoringCard,
    SalmonScoringCard,
    ElkScoringCard,
    HawkScoringCard {
  // to do if need
}

