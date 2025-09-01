package com.pandadev.gianghandmade.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ImageResponse {

    private long id;
    private String secureUrl;
    private long productId;
}
