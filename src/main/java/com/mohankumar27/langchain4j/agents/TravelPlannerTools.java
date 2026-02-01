package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TravelPlannerTools {

    // Flight Agent that fetches flight details for provided destination and dates and writes output to 'flight_options'
    public interface FlightExpert {
        @UserMessage("Find 3 flight options to {{destination}} for dates {{dates}}.")
        @Agent(outputKey = "flight_options")
        String findFlights(@V("destination") String dest, @V("dates") String dates);
    }

    // Hotel Agent that fetches hotel details for provided destination and dates and writes output to 'hotel_options'
    public interface HotelExpert {
        @UserMessage("Find 3 hotel options in {{destination}} for dates {{dates}}.")
        @Agent(outputKey = "hotel_options")
        String findHotels(@V("destination") String dest, @V("dates") String dates);
    }
}
