package uz.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.app.entity.Media;
import uz.app.repository.MediaRepository;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    public Media saveMedia(Media media) {
        return mediaRepository.save(media);
    }

}
