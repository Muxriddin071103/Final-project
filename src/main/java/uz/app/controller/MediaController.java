package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.dto.MediaDto;
import uz.app.entity.Media;
import uz.app.service.MediaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @GetMapping
    public List<MediaDto> getAllMedia() {
        return mediaService.getAllMedia().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaDto> getMediaById(@PathVariable Long id) {
        return mediaService.getMediaById(id)
                .map(media -> new ResponseEntity<>(convertToDto(media), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<MediaDto> saveMedia(@RequestBody MediaDto mediaDto) {
        Media media = convertToEntity(mediaDto);
        Media savedMedia = mediaService.saveMedia(media);
        return new ResponseEntity<>(convertToDto(savedMedia), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaById(@PathVariable Long id) {
        mediaService.deleteMediaById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private MediaDto convertToDto(Media media) {
        return new MediaDto(
                media.getId(),
                media.getFileName(),
                media.getFileUrl(),
                media.getUploadedAt()
        );
    }

    private Media convertToEntity(MediaDto mediaDto) {
        return Media.builder()
                .id(mediaDto.getId())
                .fileName(mediaDto.getFileName())
                .fileUrl(mediaDto.getFileUrl())
                .uploadedAt(mediaDto.getUploadedAt())
                .build();
    }
}
