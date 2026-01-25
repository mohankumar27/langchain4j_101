package com.mohankumar27.langchain4j.entity;

public record Customer(String firstName, String lastName, Sentiment sentiment) {
    public enum Sentiment { HAPPY, ANGRY, NEUTRAL }
}
