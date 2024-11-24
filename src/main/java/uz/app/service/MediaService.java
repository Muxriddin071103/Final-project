package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.app.entity.Media;
import uz.app.repository.MediaRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;

    public Media saveMedia(Media media) {
        return mediaRepository.save(media);
    }

    public Optional<Media> getMediaById(long id) {
        return mediaRepository.findById(id);
    }

    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public void deleteMediaById(long id) {
        mediaRepository.deleteById(id);
    }

}
