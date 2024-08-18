package ru.hogwarts.school.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/avatars")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(@RequestPart("avatar") MultipartFile multipartFile,
                       @RequestParam Long studentId) throws IOException {
        avatarService.upload(multipartFile, studentId);
    }

    @GetMapping
    public List<Avatar> getAllAvatarsForPage(@RequestParam ("page") Integer page,
                                             @RequestParam ("size") Integer size){
        return avatarService.getAllAvatarsForPage(page,size);
    }
}
