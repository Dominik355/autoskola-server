
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ProfilePicture;
import com.example.AutoskolaDemoWithSecurity.repositories.PictureRepository;
import java.io.FileNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class PictureService {
        
    @Autowired
    private PictureRepository pictureRepository;
    
    private final Logger log = LoggerFactory.getLogger(PictureService.class);
    
    public String saveImage(MultipartFile file) {
        if(!file.isEmpty()) {
            String type = FilenameUtils.getExtension(file.getOriginalFilename());
            if(type.equals("png") || type.equals("jpg") || type.equals("jpeg")) {
                String email = SecurityContextHolder.getContext()
                            .getAuthentication().getName();
                ProfilePicture picture;
                try {
                    if(pictureRepository.existsByName(email)) {
                        log.info("User: "+email+" updated his profile picture");
                        picture = pictureRepository.findByName(email).get();
                        picture.setPicture(file.getBytes());
                    } else {
                        log.info("User: "+email+" uploaded profile picture");
                        picture = new ProfilePicture(email, type, file.getBytes());
                    }
                    pictureRepository.save(picture);
                    return "Picture saved";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "exception occued while saving picture";
                }
            }
            return "File you send is not image";
        }
        return "File you send is empty";
    }
    
    
    public ProfilePicture getFile() throws FileNotFoundException {
        String email = SecurityContextHolder.getContext()
                        .getAuthentication().getName();

        if(pictureRepository.existsByName(email)) {
            return pictureRepository.findByName(email).get();
        } else {
            return pictureRepository.findByName(email).orElse(
                pictureRepository.findByName("basicProfilePicture").orElseThrow(
                        () -> new FileNotFoundException("Picture not found")));
        }
    }
    
}
