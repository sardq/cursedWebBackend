package demo.Controllers;

import demo.Service.EmailService;
import demo.Service.OtpService;
import demo.Service.SmsService;
import demo.core.configuration.Constants;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(OtpController.URL)
public class OtpController {
    public static final String URL = Constants.API_URL + "/otp";

    private OtpService otpService;

    private EmailService emailService;

    private SmsService smsService;

    public OtpController(SmsService smsService, OtpService otpService, EmailService emailService) {
        this.smsService = smsService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("/send/sms")
    public String sendOtpSms(@RequestParam String phone) {
        String otp = otpService.generateOtp(phone);
        smsService.sendOtp(phone, otp);
        return "Код отправлен на ваш номер. Пожалуйста проверьте номер телефона." + otp;
    }

    @PostMapping("/verify")
    public boolean verifyOtp(@RequestParam String key, @RequestParam String otp) {
        return otpService.validateOtp(key, otp);
    }

    @PostMapping("/send/email")
    public Map<String, String> sendOtpEmail(@RequestParam String email) {
        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Код отправлен на вашу почту. Пожалуйста проверьте почту.");
        response.put("otp", otp.toString());
        return response;
    }

}