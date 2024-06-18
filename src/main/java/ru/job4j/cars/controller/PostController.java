package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.OwnerHistory;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.CarBodyService;
import ru.job4j.cars.service.EngineService;
import ru.job4j.cars.service.OwnerHistoryService;
import ru.job4j.cars.service.PostService;

import java.io.IOException;
import java.util.Optional;

@ThreadSafe
@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    private final EngineService engineService;

    private final CarBodyService carBodyService;

    private final OwnerHistoryService ownerHistoryService;

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("posts", postService.findAllOrderById());
        return "posts/list";
    }

    @GetMapping("/new")
    public String getNew(Model model) {
        model.addAttribute("posts", postService.findNew());
        return "posts/list";
    }

    @GetMapping("/photo")
    public String getWithPhoto(Model model) {
        model.addAttribute("posts", postService.findWithPhoto());
        return "posts/list";
    }

    @GetMapping("/post/{id}")
    public String getById(Model model, @PathVariable int id, HttpServletResponse response) {
        Optional<Post> post = postService.findById(id);
        if (post.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("message", "Can't get post with id=" + id
                    + " because this post not found");
            return "errors/error";
        }
        model.addAttribute("post", post.get());
        return "posts/one";
    }

    @GetMapping("/post/add")
    public String getAddPage(Model model) {
        model.addAttribute("engines", engineService.findAll());
        model.addAttribute("carBodies", carBodyService.findAll());
        return "posts/add";
    }

    @PostMapping("/post/add")
    public String create(@ModelAttribute PostDto postDto, @RequestParam MultipartFile file,
                         @SessionAttribute User user) throws IOException {
        postDto.setUser(user);
        postDto.setVin(postDto.getVin().toUpperCase());
        postService.create(postDto, new FileDto(file.getOriginalFilename(), file.getBytes()));
        return "redirect:/posts/all";
    }

    @GetMapping("/post/{id}/edit")
    public String getEditPage(Model model, @PathVariable int id, @SessionAttribute User user) {
        Optional<Post> post = postService.findById(id);
        if (post.isEmpty()) {
            model.addAttribute("message", "Can't edit post with id=" + id
                    + " because this post not found");
            return "errors/error";
        }

        if (user.getId() != post.get().getUser().getId()) {
            model.addAttribute("message", "You can't edit posts added by"
                    + " other users");
            return "errors/error";
        }

        Car car = post.get().getCar();

        OwnerHistory ownerHistory =
                ownerHistoryService.findByCarIdAndOwnerId(car.getId(), car.getOwner().getId()).orElseThrow();

        model.addAttribute("engines", engineService.findAll());
        model.addAttribute("carBodies", carBodyService.findAll());
        model.addAttribute("post", post.get());
        model.addAttribute("ownershipYear", ownerHistory.getHistory().getStartAt().getYear());
        return "posts/edit";
    }

    @PostMapping("/post/{id}/edit")
    public String edit(@ModelAttribute("post") PostDto postDto, @RequestParam MultipartFile file,
                       Model model) throws IOException {
        Optional<Post> post = postService.findById(postDto.getId());
        if (post.isEmpty()) {
            model.addAttribute("message", "Can't edit post with id=" + postDto.getId()
                    + " because this post not found");
            return "errors/error";
        }
        postService.update(postDto, new FileDto(file.getOriginalFilename(), file.getBytes()));
        return "redirect:/posts/post/" + postDto.getId();
    }

    @GetMapping("/post/{id}/sold")
    public String markCarAsSold(@PathVariable int id, Model model) {
        Optional<Post> post = postService.findById(id);
        if (post.isEmpty()) {
            model.addAttribute("message", "Can't mark car as sold in the post with id=" + id
                    + " because this post not found");
            return "errors/error";
        }
        postService.markCarAsSold(post.get());
        return "redirect:/posts/all";
    }

    @GetMapping("/post/{id}/delete")
    public String delete(@PathVariable int id, Model model) {
        Optional<Post> post = postService.findById(id);
        if (post.isEmpty()) {
            model.addAttribute("message", "Can't delete post with id=" + id
                    + " because this post not found");
            return "errors/error";
        }
        postService.delete(post.get());
        return "redirect:/posts/all";
    }

    @GetMapping("/post/{id}/follow")
    public String follow(@PathVariable int id, Model model, @SessionAttribute User user) {
        Optional<Post> post = postService.findById(id);
        if (post.isEmpty()) {
            model.addAttribute("message", "Can't follow post with id=" + id
                    + " because this post not found");
            return "errors/error";
        }
        postService.follow(post.get(), user);
        return "redirect:/posts/all";
    }
}
