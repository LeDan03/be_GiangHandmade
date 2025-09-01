package com.pandadev.gianghandmade.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

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
