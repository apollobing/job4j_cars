package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cars.service.PostService;

@ThreadSafe
@Controller
@AllArgsConstructor
public class IndexController {

    private final PostService postService;

    @GetMapping({"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("posts", postService.findAllOrderById());
        return "posts/list";

    }
}
