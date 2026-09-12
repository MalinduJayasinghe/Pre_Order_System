package lk.ijse.preordersystem.config;

import lk.ijse.preordersystem.entity.Category;
import lk.ijse.preordersystem.entity.Role;
import lk.ijse.preordersystem.entity.User;
import lk.ijse.preordersystem.repository.CategoryRepository;
import lk.ijse.preordersystem.repository.RoleRepository;
import lk.ijse.preordersystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        log.info("Execute method run (DataSeeder)");

        Role adminRole = seedRole("ADMIN", "Full access - menu, staff, orders, discounts, and reports");
        Role cashierRole =  seedRole("CASHIER", "Manages the live order queue and updates order status");
        Role customerRole = seedRole("CUSTOMER", "Browses the menu and places pre-orders");

        seedCategory("Main Dish", "Hearty full-plate meals");
        seedCategory("Appetizer", "Small starter dishes");
        seedCategory("Dessert", "Sweet dishes served after the meal");
        seedCategory("Beverage", "Drinks, hot and cold");

        if (userRepository.findByUserName("admin").isEmpty()) {

            User admin = new User();
            admin.setUserName("Admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setContact("0779460086");
            admin.setEmail("jayasinghemalindu3@gmail.com");
            admin.setRole(adminRole);
            admin.setEnabled(true);

            userRepository.save(admin);
            log.info("Seeded default admin account (username: Admin / password: admin)");
        }

        if (userRepository.findByUserName("customer").isEmpty()) {

            User customer = new User();
            customer.setUserName("Customer");
            customer.setPassword(passwordEncoder.encode("customer"));
            customer.setContact("0779460086");
            customer.setEmail("jayasinghemalindu3@gmail.com");
            customer.setRole(customerRole);
            customer.setEnabled(true);

            userRepository.save(customer);
            log.info("Seeded default admin account (username: Customer / password: customer)");
        }

        if (userRepository.findByUserName("cashier").isEmpty()) {

            User cashier = new User();
            cashier.setUserName("Cashier");
            cashier.setPassword(passwordEncoder.encode("cashier"));
            cashier.setContact("0779460086");
            cashier.setEmail("jayasinghemalindu3@gmail.com");
            cashier.setRole(cashierRole);
            cashier.setEnabled(true);

            userRepository.save(cashier);
            log.info("Seeded default admin account (username: Cashier / password: cashier)");
        }

        log.info("DataSeeder finished");
    }

    private Role seedRole(String roleName, String description) {

        return roleRepository.findByRoleName(roleName).orElseGet(() -> {

            Role role = new Role();
            role.setRoleName(roleName);
            role.setDescription(description);

            log.info("Seeded role: " + roleName);
            return roleRepository.save(role);
        });
    }

    private void seedCategory(String categoryName, String description) {

        if (categoryRepository.findByCategoryNameIgnoreCase(categoryName).isEmpty()) {

            Category category = new Category();
            category.setCategoryName(categoryName);
            category.setDescription(description);

            categoryRepository.save(category);
            log.info("Seeded category: " + categoryName);
        }
    }
}
