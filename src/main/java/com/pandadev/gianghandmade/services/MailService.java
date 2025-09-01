package com.pandadev.gianghandmade.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    public void sendVerificationEmail(String to, String verifyUrl){
        String subject = "Xác thực email đăng ký tài khoản Giang Handmade";
        String content = """
                Quý khách hàng thân mến, Giang Handmade xin gửi lời cám ơn chân thành đến quý khách vì đã tham quan cửa hàng!
                Quý khách vui lòng nhấn vào link để xác minh địa chỉ email: %s
                
                Link xác thực sẽ mất hiệu lực sau 15 phút.
                Giang Handmade xin chân thành cám ơn!
                """.formatted(verifyUrl);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
            log.info("Da gui mail toi: {}", to);
        }catch (Exception e){
            log.error("Khong the gui mail toi dia chi email: {}", to);
            throw new RuntimeException("Khong the gui mail toi dia chi email: " + to);
        }
    }
}
