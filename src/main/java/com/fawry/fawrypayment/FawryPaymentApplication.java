package com.fawry.fawrypayment;

import com.fawry.fawrypayment.entity.Biller;
import com.fawry.fawrypayment.entity.Roles;
import com.fawry.fawrypayment.entity.User;
import com.fawry.fawrypayment.repo.BillerRepo;
import com.fawry.fawrypayment.repo.RolesRepo;
import com.fawry.fawrypayment.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class FawryPaymentApplication implements CommandLineRunner {
    private final UsersRepo usersRepo;
    private final RolesRepo rolesRepo;
    private final PasswordEncoder passwordEncoder;
    private final BillerRepo billerRepo;

    public static void main(String[] args) {
        SpringApplication.run(FawryPaymentApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Biller biller1 = new Biller();
        biller1.setName("Vodafone");

        Biller biller2 = new Biller();
        biller2.setName("Orange");

        billerRepo.save(biller1);
        billerRepo.save(biller2);

        Roles adminRole = rolesRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> rolesRepo.save(newRole("ROLE_ADMIN")));

        Roles userRole = rolesRepo.findByName("ROLE_USER")
                .orElseGet(() -> rolesRepo.save(newRole("ROLE_USER")));

        if (usersRepo.findByUsername("admin@fawry.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin@fawry.com");
            admin.setPassword(passwordEncoder.encode("admin12345"));
            admin.setRoles(List.of(adminRole));
            usersRepo.save(admin);
        }

        if (usersRepo.findByUsername("user1@fawry.com").isEmpty()) {
            User user1 = new User();
            user1.setUsername("user1@fawry.com");
            user1.setPassword(passwordEncoder.encode("user12345"));
            user1.setRoles(List.of(userRole));
            user1.setBiller(biller1);
            usersRepo.save(user1);
        }

        if (usersRepo.findByUsername("user2@fawry.com").isEmpty()) {
            User user2 = new User();
            user2.setUsername("user2@fawry.com");
            user2.setPassword(passwordEncoder.encode("user12345"));
            user2.setBiller(biller2);
            user2.setRoles(List.of(userRole));
            usersRepo.save(user2);
        }

    }
    private Roles newRole(String name) {
        Roles role = new Roles();
        role.setName(name);
        return role;
    }
}
