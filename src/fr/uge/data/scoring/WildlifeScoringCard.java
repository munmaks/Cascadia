package fr.uge.data.scoring;

public sealed interface WildlifeScoringCard permits 
    FamilyAndIntermediateScoringCards,  /* they're similair, so Family and Scoring card are in the same class */
    BearScoringCard,
    FoxScoringCard,
    SalmonScoringCard,
    ElkScoringCard,
    HawkScoringCard {
  // to do if need
}

