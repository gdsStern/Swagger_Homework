package ru.hogwarts.school.service;

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
import java.util.UUID;
@Service
@Transactional
public class AvatarService {
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
    public void upload(MultipartFile multipartFile, Long studentId) {
        try {
            byte[] data = multipartFile.getBytes();
            String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path avatarPath = path.resolve(UUID.randomUUID() + "." + extension);
            Files.write(avatarPath, data);
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new StudentNotFoundException());
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setFileSize((long) data.length);
            avatar.setData(data);
            avatar.setMediaType(multipartFile.getContentType());
            avatar.setFilePath(avatarPath.toString());
        } catch (IOException e) {
            throw new AvatarException();
        }
    }

    public org.springframework.data.util.Pair<byte[], String> getAvatarFromDb(Long studentId) {
        Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                .orElseThrow(() -> new StudentNotFoundException());
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public org.springframework.data.util.Pair<byte[], String> getAvatarFromFs(Long studentId) throws IOException {
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseThrow(() -> new StudentNotFoundException());
            return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarException();
        }

    }


}
