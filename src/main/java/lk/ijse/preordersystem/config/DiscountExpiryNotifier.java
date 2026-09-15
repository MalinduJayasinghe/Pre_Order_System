package lk.ijse.preordersystem.config;

import lk.ijse.preordersystem.entity.Discount;
import lk.ijse.preordersystem.entity.User;
import lk.ijse.preordersystem.repository.DiscountRepository;
import lk.ijse.preordersystem.repository.UserRepository;
import lk.ijse.preordersystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscountExpiryNotifier {

    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * *")
    public void notifyExpiringDiscounts() {

        log.info("Execute scheduled task notifyExpiringDiscounts");

        try {

            LocalDate tomorrow = LocalDate.now().plusDays(1);
            List<Discount> expiringDiscounts = discountRepository.findByActiveTrueAndDiscountDate(tomorrow);

            if (expiringDiscounts.isEmpty()) {
                return;
            }

            List<User> admins = userRepository.findByRole_RoleName("ADMIN");

            for (Discount discount : expiringDiscounts) {

                String message = "Discount \"" + discount.getCode() + "\" (" + discount.getPercentage()
                        + "%) expires tomorrow (" + tomorrow + ").";

                for (User admin : admins) {
                    notificationService.createNotification(admin.getUserId(), message);
                }
            }

            log.info("notifyExpiringDiscounts finished");

        }catch (Exception e){
            log.error("Error in method notifyExpiringDiscounts", e);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void notifyExpiredDiscounts() {

        log.info("Execute scheduled task notifyExpiredDiscounts");

        try {

            LocalDate yesterday = LocalDate.now().minusDays(1);
            List<Discount> expiredDiscounts = discountRepository.findByActiveTrueAndDiscountDate(yesterday);

            if (expiredDiscounts.isEmpty()) {
                return;
            }

            List<User> admins = userRepository.findByRole_RoleName("ADMIN");

            for (Discount discount : expiredDiscounts) {

                String message = "Discount \"" + discount.getCode() + "\" (" + discount.getPercentage()
                        + "%) has expired (was valid " + yesterday + ").";

                for (User admin : admins) {
                    notificationService.createNotification(admin.getUserId(), message);
                }
            }

            log.info("notifyExpiredDiscounts finished");

        }catch (Exception e){
            log.error("Error in method notifyExpiredDiscounts", e);
        }
    }
}