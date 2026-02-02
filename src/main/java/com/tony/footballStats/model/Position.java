package com.tony.footballStats.model;

import lombok.Getter;

@Getter
public enum Position {
    GOALKEEPER("Goalkeeper", "Gardien", "bg-warning text-dark"), // Jaune

    DEFENCE("Defence", "Défenseur", "bg-info text-dark"),        // Bleu clair
    CENTRE_BACK("Centre-Back", "Défenseur Central", "bg-info text-dark"),
    LEFT_BACK("Left-Back", "Latéral Gauche", "bg-info text-dark"),
    RIGHT_BACK("Right-Back", "Latéral Droit", "bg-info text-dark"),

    MIDFIELD("Midfield", "Milieu", "bg-success"),                // Vert
    DEFENSIVE_MIDFIELD("Defensive Midfield", "Milieu Défensif", "bg-success"),
    CENTRAL_MIDFIELD("Central Midfield", "Milieu Central", "bg-success"),
    ATTACKING_MIDFIELD("Attacking Midfield", "Milieu Offensif", "bg-success"),
    LEFT_MIDFIELD("Left Midfield", "Milieu Gauche", "bg-success"),
    RIGHT_MIDFIELD("Right Midfield", "Milieu Droit", "bg-success"),

    OFFENCE("Offence", "Attaquant", "bg-danger"),                // Rouge
    CENTRE_FORWARD("Centre-Forward", "Avant-Centre", "bg-danger"),
    LEFT_WINGER("Left Winger", "Ailier Gauche", "bg-danger"),
    RIGHT_WINGER("Right Winger", "Ailier Droit", "bg-danger");

    private final String apiLabel;
    private final String frenchLabel;
    private final String badgeColor;

    Position (String apiLabel, String frenchLabel, String badgeColor) {
        this.apiLabel = apiLabel;
        this.frenchLabel = frenchLabel;
        this.badgeColor = badgeColor;
    }

    // Méthode pour retrouver l'Enum à partir du texte de l'API
    public static Position fromApiLabel(String label) {
        if (label == null) return null;
        for (Position p : values()) {
            if (p.apiLabel.equalsIgnoreCase(label)) {
                return p;
            }
        }
        return null; // ou une valeur par défaut
    }
}
