package fr.uge.data.scoring;

public sealed interface WildlifeScoringCard permits FamilyAndIntermediateScoringCards,
    BearScoringCard, FoxScoringCard, SalmonScoringCard, ElkScoringCard, HawkScoringCard {}
