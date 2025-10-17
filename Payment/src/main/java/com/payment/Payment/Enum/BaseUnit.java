package com.payment.Payment.Enum;

public enum BaseUnit {
    VND_PER_HOUR("VND/hour"),
    VND_PER_MINUTE("VND/minute");

    private final String label;

    BaseUnit(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
