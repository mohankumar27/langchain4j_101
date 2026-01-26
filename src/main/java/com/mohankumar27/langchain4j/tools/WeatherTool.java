package com.mohankumar27.langchain4j.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class WeatherTool { // Tool is autowired to all services with @AiService annotation

    @Tool("Gets the weather forecast for a specific location")
    public String getWeather(
            @P("The city name, e.g., 'Paris' or 'New York'") String city,
            @P("The unit of temperature: 'C' for Celsius, 'F' for Fahrenheit") String unit
    ) {
        // Call actual weather API here...
        System.out.println("Called getWeather(" + city + ", " + unit + ")");
        return "It is currently 25 degrees " + unit + " in " + city;
    }
}
