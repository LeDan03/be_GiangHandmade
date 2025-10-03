package com.pandadev.gianghandmade.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AvatarResponse extends ImageResponse{

    private boolean defaultAvatar;
    private int userId;
}
