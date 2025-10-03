package com.pandadev.gianghandmade.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CloudinaryUploadDto {

    private String secureUrl;
    private String publicId;
}
