package com.diden.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Deprecated
public class HelloController {

    @RequestMapping("/")
    public String index(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "index";
    }

    @RequestMapping("/list")
    public String list(Model model) {
        return "index";
    }
}
