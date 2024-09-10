package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.AvatarException;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private final Path path;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository,
                         @Value("${application.avatars-dir-name}") String avatarDirName) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        path = Paths.get(avatarDirName);
    }
    @Transactional
    public void upload(MultipartFile multipartFile, Long studentId) throws IOException {
        logger.info("Вызвын метод uploadAvatar");
        try {
            byte[] data = multipartFile.getBytes();
            String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path avatarPath = path.resolve(UUID.randomUUID() + "." + extension);
            System.out.println(1);
            //Files.write(avatarPath, data);
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        logger.error("Не найден студент с id " + studentId);
                        return new StudentNotFoundException();
                    });
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setFileSize((long) data.length);
            avatar.setData(data);
            avatar.setMediaType(multipartFile.getContentType());
            avatar.setFilePath(avatarPath.toString());
            avatarRepository.save(avatar);
        } catch (IOException e) {
            throw new AvatarException();
        }
    }

    public Pair<byte[], String> getAvatarFromDb(Long studentId) {
        logger.info("Вызван метод getAvatarFromDb");
        Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                .orElseThrow(() -> {
                        logger.error("Не найден студент с id " + studentId);
                        return new StudentNotFoundException();
                });
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Pair<byte[], String> getAvatarFromFs(Long studentId) throws IOException {
        logger.info("Вызван метод getAvatarFromFs");
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseThrow(() -> {
                        logger.error("Не найден студент с id " + studentId);
                        return new StudentNotFoundException();
                    });
            return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarException();
        }

    }

    public List<Avatar> getAllAvatarsForPage(Integer page, Integer size) {
        logger.info("Вызван метод getAvatarsForPage");
        if(page==0){
            logger.error("Передан неправильный аргумент");
            throw new IllegalArgumentException();
        }
        PageRequest pageRequest = PageRequest.of(page-1,size);
        return avatarRepository.findAll(pageRequest).getContent();
    }

}
