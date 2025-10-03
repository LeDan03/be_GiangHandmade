package com.pandadev.gianghandmade.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pandadev.gianghandmade.dtos.CloudinaryUploadDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    public CloudinaryUploadDto upload(String ggPictureLink) {
        try {
            // Upload ảnh từ URL trực tiếp
            Map uploadResult = cloudinary.uploader().upload(
                    ggPictureLink,
                    ObjectUtils.asMap(
                            "folder", "avatars",      // lưu trong folder avatars
                            "overwrite", true,
                            "resource_type", "image"
                    )
            );

            // Trả về secure_url để client có thể load ảnh public
            return new CloudinaryUploadDto(uploadResult.get("secure_url").toString(), uploadResult.get("public_id").toString());
        } catch (Exception e) {
            logger.error("Lỗi upload ảnh từ Google link: {}", e.getMessage(), e);
            throw new RuntimeException("Upload ảnh thất bại", e);
        }
    }

    public boolean delete(String publicId){
        try{
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return true;
        }catch (Exception ex){
            logger.error("Lỗi khi xóa ảnh", ex);
            return false;
        }
    }
}
