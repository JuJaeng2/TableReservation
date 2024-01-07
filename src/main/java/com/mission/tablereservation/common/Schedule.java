package com.mission.tablereservation.common;

import com.mission.tablereservation.customer.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Schedule {

    private final ReservationService reservationService;

    @Scheduled(cron = "0 0 0/6 * * ?")
    public void deleteReservation(){


        log.info("=============================");
        log.info("<<<<<<<<<< 스케쥴러 >>>>>>>>>>>>");

        reservationService.deleteReservation();

        log.info("<<< 기간이 지난 예약 삭제 완료 >>>");
        log.info("=============================");

    }

}
