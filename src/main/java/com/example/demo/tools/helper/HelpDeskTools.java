package com.example.demo.tools.helper;


import com.example.demo.tools.entity.HelpDeskTicket;
import com.example.demo.tools.model.TicketRequest;
import com.example.demo.tools.service.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpDeskTools.class);

    private final HelpDeskTicketService helpDeskTicketService;

    /**
     * returnDirect=true, this output will not be sent back to LLM, directly returned to user
     *
     */
    @Tool(name = "createTicket", description = "Create the Support Ticket", returnDirect = true)
    public String createTicket(@ToolParam(description = "Details to create a Support ticket")
                        TicketRequest ticketRequest, ToolContext toolContext) {
        String userId = (String) toolContext.getContext().get("userId");
        LOGGER.info("Creating support ticket for user: {} with details: {}", userId, ticketRequest);
        HelpDeskTicket savedTicket = helpDeskTicketService.createTicket(ticketRequest, userId);
        LOGGER.info("Ticket created successfully. Ticket ID: {}, userId: {}", savedTicket.getId(), savedTicket.getUserId());
        return "Ticket #" + savedTicket.getId() + " created successfully for user " + savedTicket.getUserId();
    }

    @Tool(name = "getTicketStatus",
            description = "Fetch or Get the status or ETA of the support tickets for a given userId")
    public List<HelpDeskTicket> getTicketStatus(ToolContext toolContext) {
        String userId = (String) toolContext.getContext().get("userId");
        LOGGER.info("Fetching tickets for user: {}", userId);
        List<HelpDeskTicket> tickets = helpDeskTicketService.getTicketsByUserId(userId);
        LOGGER.info("Found {} tickets for user: {}", tickets.size(), userId);
        return tickets;
    }

}
