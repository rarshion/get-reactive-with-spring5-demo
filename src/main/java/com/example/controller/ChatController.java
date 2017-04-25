package com.example.controller;

import com.example.controller.vm.MessageVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.service.MessageService;
import com.example.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping
public class ChatController {

    private final MessageService messageService;
    private final StatisticService statisticService;

    @Autowired
    public ChatController(MessageService messageService, StatisticService statisticService) {
        this.messageService = messageService;
        this.statisticService = statisticService;
    }

    @GetMapping
    public String index() {
        return "chat";
    }

    @GetMapping(value = "/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String messages(Model model) {
        Flux<MessageVM> messages = messageService.latest();

        model.addAttribute("messages", new ReactiveDataDriverContextVariable(messages, 1));

        return "message";
    }

    @GetMapping(value = "/statistic", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String statistic(Model model) {
        Flux<UsersStatisticVM> statistic = statisticService.usersStatisticStream();

        model.addAttribute("statistic", new ReactiveDataDriverContextVariable(statistic, 1));

        return "users-statistic";
    }
}