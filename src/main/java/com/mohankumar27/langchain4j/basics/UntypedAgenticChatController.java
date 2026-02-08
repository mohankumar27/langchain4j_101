package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.agents.*;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.agentic.supervisor.SupervisorAgent;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
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
    private final UntypedAgent customerSupportAssistant;
    private final UntypedAgent codingAssistant;
    private final SupervisorAgent supervisorAssistant;

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
                    // combines output from hotel and flight agent
                    String flights = (String) scope.readState("flight_options");
                    String hotels = (String) scope.readState("hotel_options");
                    return "Travel Plan:\n" + flights + "\n" + hotels;
                })
                .outputKey("travel_options")
                .build();

        CustomerSupportTools.ClassifierAgent classifierAgent = AgenticServices.agentBuilder(CustomerSupportTools.ClassifierAgent.class)
                .chatModel(chatModel)
                .build();

        CustomerSupportTools.RefundAgent refundAgent = AgenticServices.agentBuilder(CustomerSupportTools.RefundAgent.class)
                .chatModel(chatModel)
                .build();

        CustomerSupportTools.ProductAgent productAgent = AgenticServices.agentBuilder(CustomerSupportTools.ProductAgent.class)
                .chatModel(chatModel)
                .build();

        UntypedAgent customerSupportRouter = AgenticServices.conditionalBuilder()
                .subAgents(agenticScope -> "REFUND".equals(agenticScope.readState("category")), refundAgent)
                .subAgents(agenticScope -> "PRODUCT".equals(agenticScope.readState("category")), productAgent)
                .build();

        customerSupportAssistant = AgenticServices.sequenceBuilder()
                .subAgents(classifierAgent, customerSupportRouter)
                .outputKey("response")
                .build();

        CodingTools.Coder coder = AgenticServices.agentBuilder(CodingTools.Coder.class)
                .chatModel(chatModel)
                .build();

        CodingTools.Reviewer reviewer = AgenticServices.agentBuilder(CodingTools.Reviewer.class)
                .chatModel(chatModel)
                .build();

        CodingTools.Editor codeEditor = AgenticServices.agentBuilder(CodingTools.Editor.class)
                .chatModel(chatModel)
                .build();

        UntypedAgent codingRouter = AgenticServices.loopBuilder()
                .subAgents(reviewer, codeEditor)
                .outputKey("review_status")
                .exitCondition(agenticScope -> agenticScope.readState("review_status").equals("APPROVED"))
                .maxIterations(1)
                .build();

        this.codingAssistant = AgenticServices.sequenceBuilder()
                .subAgents(coder, codingRouter)
                .outputKey("code")
                .build();

        SupervisorTools.NewsTool news = AgenticServices.agentBuilder(SupervisorTools.NewsTool.class)
                .chatModel(chatModel)
                .build();

        SupervisorTools.StockTool stock = AgenticServices.agentBuilder(SupervisorTools.StockTool.class)
                .chatModel(chatModel)
                .build();

        this.supervisorAssistant = AgenticServices.supervisorBuilder()
                .subAgents(news, stock)
                .chatModel(chatModel)
                .responseStrategy(SupervisorResponseStrategy.SUMMARY)
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

    @GetMapping("/customer-support")
    public String customerSupport(@RequestParam String query) {
        ResultWithAgenticScope<String> result = customerSupportAssistant.invokeWithAgenticScope(
                Map.of("query", query));
        return result.result();
    }

    @GetMapping("/code")
    public String code(@RequestParam String spec, @RequestParam String feedback) {
        ResultWithAgenticScope<String> result = codingAssistant.invokeWithAgenticScope(
                Map.of("spec", spec,
                        "feedback", feedback));
        return result.result();
    }

    @GetMapping("/supervisor")
    public String supervisor(@RequestParam String query) {
        ResultWithAgenticScope<String> result = supervisorAssistant.invokeWithAgenticScope(query);
        return result.result();
    }
}
