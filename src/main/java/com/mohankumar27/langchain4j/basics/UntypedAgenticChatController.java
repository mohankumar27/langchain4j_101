package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.agents.TravelPlannerTools;
import com.mohankumar27.langchain4j.agents.WriterAgentTools;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/untyped-agentic")
public class UntypedAgenticChatController {

    private final UntypedAgent writingAssistant;
    private final UntypedAgent travelAssistant;

    public UntypedAgenticChatController(ChatModel chatModel) {
        WriterAgentTools.Writer writer = AgenticServices.agentBuilder(WriterAgentTools.Writer.class)
                .chatModel(chatModel)
                .build();

        WriterAgentTools.Editor editor = AgenticServices.agentBuilder(WriterAgentTools.Editor.class)
                .chatModel(chatModel)
                .build();

        WriterAgentTools.Publisher publisher = AgenticServices.agentBuilder(WriterAgentTools.Publisher.class)
                .chatModel(chatModel)
                .build();

        TravelPlannerTools.FlightExpert flightExpert = AgenticServices.agentBuilder(TravelPlannerTools.FlightExpert.class)
                .chatModel(chatModel)
                .build();

        TravelPlannerTools.HotelExpert hotelExpert = AgenticServices.agentBuilder(TravelPlannerTools.HotelExpert.class)
                .chatModel(chatModel)
                .build();

        writingAssistant = AgenticServices.sequenceBuilder()
                .subAgents(writer, editor, publisher)
                .outputKey("final_post")
                .build();

        travelAssistant = AgenticServices.parallelBuilder()
                .subAgents(flightExpert, hotelExpert)
                .output(scope -> {
                    // Typed joiner: must match return type of HolidayPlanner (String)
                    String flights = (String) scope.readState("flight_options");
                    String hotels = (String) scope.readState("hotel_options");
                    return "Travel Plan:\n" + flights + "\n" + hotels;
                })
                .outputKey("travel_options")
                .build();
    }

    @GetMapping("/write-post")
    public String writePost(@RequestParam String topic) {
        ResultWithAgenticScope<String> result = writingAssistant.invokeWithAgenticScope(Map.of("topic", topic));
        return result.result();
    }

    @GetMapping("/travel-plan")
    public String travelPlan(@RequestParam String destination, @RequestParam String dates) {
        ResultWithAgenticScope<String> result = travelAssistant.invokeWithAgenticScope(
                Map.of("destination", destination,
                        "dates", dates));
        return result.result();

    }
}
