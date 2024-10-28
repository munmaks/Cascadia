package fr.uge.scoring;

public sealed interface WildlifeScoringCard permits 
    FamilyScoringCard,
    IntermediateScoringCard,
    BearScoringCard,
    FoxScoringCard,
    SalmonScoringCard,
    ElkScoringCard,
    HawkScoringCard {
  // to do if need
}

