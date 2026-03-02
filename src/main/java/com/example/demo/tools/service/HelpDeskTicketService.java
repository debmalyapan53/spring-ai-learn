package com.example.demo.tools.service;

import com.example.demo.tools.entity.HelpDeskTicket;
import com.example.demo.tools.model.TicketRequest;
import com.example.demo.tools.repository.HelpDeskTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {

    private final HelpDeskTicketRepository helpDeskTicketRepository;

    public HelpDeskTicket createTicket(TicketRequest ticketInput, String userId) {
        HelpDeskTicket ticket = HelpDeskTicket.builder()
                .issue(ticketInput.issue())
                .userId(userId)
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();
        return helpDeskTicketRepository.save(ticket);
    }

    public List<HelpDeskTicket> getTicketsByUserId(String username) {
        return helpDeskTicketRepository.findByUserId(username);
    }

}
