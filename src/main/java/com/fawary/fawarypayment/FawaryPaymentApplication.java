package com.fawary.fawarypayment;

import com.fawary.fawarypayment.entity.Roles;
import com.fawary.fawarypayment.entity.Users;
import com.fawary.fawarypayment.repo.RolesRepo;
import com.fawary.fawarypayment.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class FawaryPaymentApplication implements CommandLineRunner {
    private final UsersRepo usersRepo;
    private final RolesRepo rolesRepo;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(FawaryPaymentApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Roles adminRole = rolesRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> rolesRepo.save(newRole("ROLE_ADMIN")));

        Roles userRole = rolesRepo.findByName("ROLE_BILLER")
                .orElseGet(() -> rolesRepo.save(newRole("ROLE_BILLER")));

        // ===== إنشاء Admin =====
        if (usersRepo.findByUsername("admin@fawary.com").isEmpty()) {
            Users admin = new Users();
            admin.setUsername("admin@fawary.com");
            admin.setPassword(passwordEncoder.encode("admin12345"));
            admin.setRoles(List.of(adminRole)); // admin مش USER
            usersRepo.save(admin);
        }

        // ===== إنشاء user1 =====
        if (usersRepo.findByUsername("user1@fawary.com").isEmpty()) {
            Users user1 = new Users();
            user1.setUsername("user1@fawary.com");
            user1.setPassword(passwordEncoder.encode("user112345"));
            user1.setRoles(List.of(userRole));
            usersRepo.save(user1);
        }

        // ===== إنشاء user2 =====
        if (usersRepo.findByUsername("user2@fawary.com").isEmpty()) {
            Users user2 = new Users();
            user2.setUsername("user2@fawary.com");
            user2.setPassword(passwordEncoder.encode("user212345"));
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
