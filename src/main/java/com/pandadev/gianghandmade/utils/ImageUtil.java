package com.pandadev.gianghandmade.utils;

import com.pandadev.gianghandmade.entities.Avatar;
import com.pandadev.gianghandmade.entities.Image;
import com.pandadev.gianghandmade.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUtil {
    @Value("{male.user.default.publicId}")
    private String maleAvatarPublicId;

    @Value("{female.user.default.publicId}")
    private String femaleAvatarPublicId;

    @Value("{other.user.default.publicId}")
    private String otherAvatarPublicId;

    private final ImageRepository imageRepository;

    public Avatar getAvatarByGender(String gender) {
        return (Avatar) switch (gender) {
            case "OTHER" -> imageRepository.findByPublicId(maleAvatarPublicId).orElse(null);
            case "FEMALE" -> imageRepository.findByPublicId(femaleAvatarPublicId).orElse(null);
            default -> imageRepository.findByPublicId(otherAvatarPublicId).orElse(null);
        };
    }
}
