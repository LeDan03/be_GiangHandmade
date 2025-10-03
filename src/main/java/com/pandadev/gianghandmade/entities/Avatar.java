package com.pandadev.gianghandmade.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true) //nếu không có sẽ không gọi equals và hashcode các field của lớp cha
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class Avatar extends Image{
    @Column(nullable = false)
    private boolean defaultAvatar;

    @OneToOne(mappedBy = "avatar")
    private User user;
}
