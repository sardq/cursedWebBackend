package demo;

import java.time.LocalDate;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import demo.Model.ExaminationEntity;
import demo.Model.ExaminationTypeEntity;
import demo.Model.ParametresEntity;
import demo.Model.ProtocolParametresEntity;
import demo.Model.TicketEntity;
import demo.Model.UserEntity;
import demo.Model.UserRole;
import demo.Service.ExaminationService;
import demo.Service.ExaminationTypeService;
import demo.Service.MediaService;
import demo.Service.ParametresService;
import demo.Service.ProtocolParametresService;
import demo.Service.TicketService;
import demo.Service.UserService;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    private final UserService userService;
    // private final MediaService mediaService;
    private final ExaminationService examinationService;
    private final ExaminationTypeService examinationTypeService;
    private final ParametresService parametresService;
    private final ProtocolParametresService protocolParametresService;
    private final TicketService ticketService;

    public DemoApplication(UserService userService, MediaService mediaService, TicketService ticketService,
            ExaminationTypeService examinationTypeService, ExaminationService examinationService,
            ParametresService parametresService, ProtocolParametresService protocolParametresService) {
        this.userService = userService;
        // this.mediaService = mediaService;
        this.ticketService = ticketService;
        this.examinationTypeService = examinationTypeService;
        this.examinationService = examinationService;
        this.parametresService = parametresService;
        this.protocolParametresService = protocolParametresService;

    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && Objects.equals("--populate", args[0])) {
            final var admin = new UserEntity("Сергеев Сергей Сергеевич", "dreod@mail.ru", "admin");
            admin.setRole(UserRole.ADMIN);
            userService.create(admin);

            log.info("Create default users values");
            final var user1 = userService.create(new UserEntity("Олег Олегов Олегович", "user@user", "useruser"));
            userService.create(new UserEntity("Иванов Иван Иванович", "jbsdk@asd", "asdsasfasfd"));
            userService.create(new UserEntity("Андреев Андрей Андреевич", "aasdsad@asd", "asfasfasf"));
            userService.create(new UserEntity("Максимов Максим Максимович", "aaxsd@asd", "asfgdasfsaf"));
            userService.create(new UserEntity("Русланов Руслан Русланович", "aacbxcsd@asd", "zxvasfsafb"));
            log.info("Create default examinationTypes values");
            final var examinationtype1 = examinationTypeService.create(
                    new ExaminationTypeEntity("examinationtype1"));
            final var examinationtype2 = examinationTypeService.create(
                    new ExaminationTypeEntity("examinationtype2"));
            final var examinationtype3 = examinationTypeService.create(
                    new ExaminationTypeEntity("examinationtype3"));
            log.info("Create default examination values");
            final var examination1 = examinationService.create(
                    new ExaminationEntity(admin, examinationtype1, LocalDate.now(), "tesasfsafasft1",
                            "testasfasfasfsaf1"));
            examinationService.create(
                    new ExaminationEntity(admin, examinationtype1, LocalDate
                            .now(), "test2safaasfasfasf",
                            "test2asfasfasfsaff"));
            examinationService.create(
                    new ExaminationEntity(admin, examinationtype1, LocalDate
                            .now(), "testasfsafsafsafasf3",
                            "testasfsafasfasasfa3"));

            // log.info("Create default media values");
            // mediaService.create(
            // new MediaEntity(examination1, jsonMediaType));
            // mediaService.create(
            // new MediaEntity(examination1, jsonMediaType));
            // mediaService.create(
            // new MediaEntity(examination1, jsonMediaType));

            log.info("Create default parametres values");
            final var parameter1 = parametresService
                    .create(new ParametresEntity(examinationtype1, "testasfsafasf1"));
            final var parameter2 = parametresService
                    .create(new ParametresEntity(examinationtype3, "testasfsafsaf2"));
            final var parameter3 = parametresService
                    .create(new ParametresEntity(examinationtype2, "testasfasfsaf3"));
            log.info("Create default protocolparametres values");
            protocolParametresService
                    .create(new ProtocolParametresEntity(parameter1, examination1, "tesasfasfsaft1"));
            protocolParametresService
                    .create(new ProtocolParametresEntity(parameter2, examination1, "teasfsafsfst2"));
            protocolParametresService
                    .create(new ProtocolParametresEntity(parameter3, examination1, "tesasfsaft3"));
            log.info("Create default ticket values");
            ticketService
                    .create(new TicketEntity(user1, "tesasfsaft1", "tessafsaft2"));
            ticketService
                    .create(new TicketEntity(user1, "tesasfsaft1", "tesasfsaft2"));
            ticketService
                    .create(new TicketEntity(user1, "tesasfsaft1", "tesasfsaft2"));

        }
    }
}
