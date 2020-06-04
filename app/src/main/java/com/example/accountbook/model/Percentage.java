package com.example.accountbook.model;

public class Percentage {
    private float percentage;
    private String label;

    public Double getValue() {
        return value;
    }

    private Double value;

    public Percentage(String label, Double value) {
        this.label = label;
        this.value = value;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }
}
