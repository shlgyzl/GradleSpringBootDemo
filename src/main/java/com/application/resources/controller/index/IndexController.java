package com.application.resources.controller.index;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.resources.exception.BusinessErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("main")
public class IndexController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "模板引擎");
        return "index/index";
    }

    @GetMapping(value = "/miss")
    public String list(Model model, @RequestParam(name = "name") String name) {
        model.addAttribute("name", name);
        return "index/index";
    }

    @GetMapping(value = "/business")
    public String list(Model model) {
        try {
            int i = 1;
            System.out.println(i / 0);
        } catch (Exception e) {
            throw new BusinessErrorException(BusinessErrorType.UNEXPECTED_EXCEPTION);
        }
        return "index/index";
    }
}