package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.StatsOverviewDTO;
import lk.ijse.preordersystem.dto.TopSellersDTO;
import lk.ijse.preordersystem.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "v1/stats")
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @GetMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStatsOverview() {

        log.info("getStatsOverview API was called");
        StatsOverviewDTO statsOverviewDTO = statsService.getStatsOverview();

        log.info("getStatsOverview API successful");
        return new CommonResponse(0, statsOverviewDTO, "Stats overview called");
    }

    @GetMapping(value = "/top-sellers", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getTopSellers() {

        log.info("getTopSellers API was called");
        List<TopSellersDTO> topSellers = statsService.getTopSellers();

        log.info("getTopSellers API successful");
        return new CommonResponse(0, topSellers, "Top sellers called");
    }
}
