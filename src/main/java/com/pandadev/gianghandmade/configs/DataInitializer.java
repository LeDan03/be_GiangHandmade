package com.pandadev.gianghandmade.configs;

import com.pandadev.gianghandmade.entities.Avatar;
import com.pandadev.gianghandmade.entities.Role;
import com.pandadev.gianghandmade.entities.ShippingRule;
import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.entities.enums.Gender;
import com.pandadev.gianghandmade.entities.enums.RegionType;
import com.pandadev.gianghandmade.entities.enums.UserStatus;
import com.pandadev.gianghandmade.repositories.ImageRepository;
import com.pandadev.gianghandmade.repositories.RoleRepository;
import com.pandadev.gianghandmade.repositories.ShippingRuleRepository;
import com.pandadev.gianghandmade.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShippingRuleRepository shippingRuleRepository;

    @Value("${male.user.default.avt}")
    private String defaultMaleAvatarUrl;
    @Value("${male.user.default.publicId}")
    private String defaultMalePublicId;

    @Value("${female.user.default.avt}")
    private String defaultFemaleAvatarUrl;
    @Value("${female.user.default.publicId}")
    private String defaultFemalePublicId;

    @Value("${lgbt.user.default.avt}")
    private String defaultOtherAvatarUrl;
    @Value("${lgbt.user.default.publicId}")
    private String defaultOtherPublicId;

    @Value("${giang.user.default.avt}")
    private String giangAvatarUrl;
    @Value("${giang.user.default.publicId}")
    private String giangAvatarPublicId;

    @Override
    public void run(ApplicationArguments args) {
        initDefaultAvatars();
        createAdmin();
        initShippingRule();
    }

    private void initDefaultAvatars() {
        if (imageRepository.existsByIdNotNull()) return;

        Avatar male = new Avatar();
        male.setPublicId(defaultMalePublicId);
        male.setSecureUrl(defaultMaleAvatarUrl);
        male.setDefaultAvatar(true);

        Avatar female = new Avatar();
        female.setPublicId(defaultFemalePublicId);
        female.setSecureUrl(defaultFemaleAvatarUrl);
        female.setDefaultAvatar(true);

        Avatar other = new Avatar();
        other.setPublicId(defaultOtherPublicId);
        other.setSecureUrl(defaultOtherAvatarUrl);
        other.setDefaultAvatar(true);

        imageRepository.saveAll(List.of(male, female, other));
    }

    private void createAdmin() {
        if (userRepository.existsByIdNotNull()) return;

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        Avatar giangAvatar = new Avatar();
        giangAvatar.setSecureUrl(giangAvatarUrl);
        giangAvatar.setPublicId(giangAvatarPublicId);
        giangAvatar.setDefaultAvatar(true);

        User admin = new User();
        admin.setName("Hương Giang");
        admin.setGender(Gender.FEMALE);
        admin.setPassword(passwordEncoder.encode("CoTienTocDo"));
        admin.setRole(adminRole);
        admin.setAvatar(giangAvatar);
        admin.setStatus(UserStatus.MANAGER);
        admin.setEmail("ng.hgiang1012@gmail.com");

        userRepository.save(admin);
    }

    private void initShippingRule(){
        if(shippingRuleRepository.existsById(1)) return;
        ShippingRule local = new ShippingRule();
        ShippingRule domestic = new ShippingRule();
        ShippingRule international = new ShippingRule();

        local.setActive(true);
        local.setBaseFee(new BigDecimal("30000"));//nội thành 30k
        local.setRegionType(RegionType.LOCAL);

        domestic.setActive(true);
        domestic.setBaseFee(new BigDecimal("35000"));//ngoại thành 35k
        domestic.setRegionType(RegionType.DOMESTIC);

        international.setActive(true);
        international.setBaseFee(new BigDecimal("40000"));//quốc tế 40k
        international.setRegionType(RegionType.INTERNATIONAL);

        shippingRuleRepository.saveAll(List.of(local, domestic, international));
    }
}

