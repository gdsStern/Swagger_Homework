package ru.hogwarts.school.controller;

import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarService;

import java.awt.*;
import java.io.IOException;

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
}
