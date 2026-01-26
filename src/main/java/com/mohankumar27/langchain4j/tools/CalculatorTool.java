package com.mohankumar27.langchain4j.tools;

import dev.langchain4j.agent.tool.Tool;

public class CalculatorTool {
    @Tool("Calculates the sum of two numbers")
    public double add(double a, double b) {
        System.out.println("Called add(" + a + ", " + b + ")");
        return a + b;
    }
    @Tool("Calculates the square root of a number")
    public double squareRoot(double number) {
        System.out.println("Called squareRoot(" + number + ")");
        return Math.sqrt(number);
    }
}
