package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.dto.MediaDto;
import uz.app.entity.Article;
import uz.app.entity.Media;
import uz.app.service.ArticleService;
import uz.app.service.MediaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final ArticleService articleService;

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
    public ResponseEntity<String> deleteMediaById(@PathVariable Long id) {
        // Check if the media exists
        Optional<Media> mediaOpt = mediaService.getMediaById(id);
        if (mediaOpt.isEmpty()) {
            return new ResponseEntity<>("Sorry! But this media has already been deleted.", HttpStatus.BAD_REQUEST);
        }

        Optional<Article> articleOpt = articleService.findArticleByMediaId(id);
        if (articleOpt.isPresent()) {
            String articleTitle = articleOpt.get().getTitle();
            return new ResponseEntity<>("Sorry! This media is used in article: " + articleTitle, HttpStatus.BAD_REQUEST);
        }

        mediaService.deleteMediaById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private MediaDto convertToDto(Media media) {
        return new MediaDto(
                media.getFileName(),
                media.getFileUrl(),
                media.getUploadedAt()
        );
    }

    private Media convertToEntity(MediaDto mediaDto) {
        return Media.builder()
                .fileName(mediaDto.getFileName())
                .fileUrl(mediaDto.getFileUrl())
                .uploadedAt(mediaDto.getUploadedAt())
                .build();
    }
}
