package com.pandadev.gianghandmade.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageRequest {
    private String publicId;
    private String secureUrl;
}
