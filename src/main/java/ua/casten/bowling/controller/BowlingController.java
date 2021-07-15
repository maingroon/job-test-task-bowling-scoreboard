package ua.casten.bowling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bowling")
public class BowlingController {

    @GetMapping()
    public String getBowlingPage() {
        return "bowling-scoreboard";
    }

}
