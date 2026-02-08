package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.agents.*;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/typed-agentic")
public class TypedAgenticChatController {

    private final WritingAssistant writingAssistant;
    private final TravelAssistant travelAssistant;
    private final CustomerSupportAssistant customerSupportAssistant;
    private final CodingAssistant codingAssistant;
    private final SupervisorAssistant supervisorAssistant;

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

        this.customerSupportAssistant = AgenticServices.sequenceBuilder(CustomerSupportAssistant.class)
                .subAgents(classifierAgent, customerSupportRouter)
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

        this.codingAssistant = AgenticServices.sequenceBuilder(CodingAssistant.class)
                .subAgents(coder, codingRouter)
                .build();

        SupervisorTools.NewsTool news = AgenticServices.agentBuilder(SupervisorTools.NewsTool.class)
                .chatModel(chatModel)
                .build();

        SupervisorTools.StockTool stock = AgenticServices.agentBuilder(SupervisorTools.StockTool.class)
                .chatModel(chatModel)
                .build();

        this.supervisorAssistant = AgenticServices.supervisorBuilder(SupervisorAssistant.class)
                .subAgents(news, stock)
                .chatModel(chatModel)
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

    @RequestMapping("/customer-support")
    public String customerSupport(String query) {
        ResultWithAgenticScope<String> result = customerSupportAssistant.respondToQuery(query);
        return result.result();
    }

    @RequestMapping("/code")
    public String code(String spec, String feedback) {
        ResultWithAgenticScope<String> result = codingAssistant.writeCode(spec, feedback);
        return result.result();
    }

    @RequestMapping("/supervisor")
    public String supervisor(String query) {
        ResultWithAgenticScope<String> result = supervisorAssistant.query(query);
        return result.result();
    }
}
