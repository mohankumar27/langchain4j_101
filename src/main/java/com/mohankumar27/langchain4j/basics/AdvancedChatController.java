package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.entity.Customer;
import com.mohankumar27.langchain4j.service.CustomerExtractor;
import com.mohankumar27.langchain4j.service.EmailDrafter;
import com.mohankumar27.langchain4j.service.PirateAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/advanced-chat")
public class AdvancedChatController {

    private final PirateAssistant pirateAssistant;
    private final EmailDrafter emailDrafter;
    private final CustomerExtractor customerExtractor;

    public AdvancedChatController(PirateAssistant pirateAssistant, EmailDrafter emailDrafter,
                                  CustomerExtractor customerExtractor) {
        this.pirateAssistant = pirateAssistant;
        this.emailDrafter = emailDrafter;
        this.customerExtractor = customerExtractor;
    }

    @GetMapping("/pirate")
    public String pirateChat(@RequestParam String query) {
        return pirateAssistant.chat(query);
    }

    @GetMapping("/email")
    public String emailChat(@RequestParam String name, @RequestParam String topic) {
        return emailDrafter.draftEmail(name, topic);
    }

    @GetMapping("/customer")
    public Customer customerChat(@RequestParam String text) {
        return customerExtractor.extract(text);
    }
}
