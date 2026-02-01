package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.agents.TravelAssistant;
import com.mohankumar27.langchain4j.agents.TravelPlannerTools;
import com.mohankumar27.langchain4j.agents.WriterAgentTools;
import com.mohankumar27.langchain4j.agents.WritingAssistant;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/typed-agentic")
public class TypedAgenticChatController {

    private final WritingAssistant writingAssistant;
    private final TravelAssistant travelAssistant;

    public TypedAgenticChatController(ChatModel chatModel) {
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

        this.writingAssistant = AgenticServices.sequenceBuilder(WritingAssistant.class)
                .subAgents(writer, editor, publisher)
                .build();

        this.travelAssistant = AgenticServices.parallelBuilder(TravelAssistant.class)
                .subAgents(flightExpert, hotelExpert)
                .output(scope -> {
                    // combines output from hotel and flight agent
                    String flights = (String) scope.readState("flight_options");
                    String hotels = (String) scope.readState("hotel_options");
                    return "Travel Plan:\n" + flights + "\n" + hotels;
                })
                .build();
    }

    @RequestMapping("/write-post")
    public String writePost(String topic) {
        ResultWithAgenticScope<String> result = writingAssistant.writePost(topic);
        return result.result();
    }

    @RequestMapping("/travel-plan")
    public String travelPlan(String destination, String dates) {
        ResultWithAgenticScope<String> result = travelAssistant.travelPlan(destination, dates);
        return result.result();
    }

}
