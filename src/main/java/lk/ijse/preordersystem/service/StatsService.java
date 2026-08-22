package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.StatsOverviewDTO;
import lk.ijse.preordersystem.dto.TopSellersDTO;

import java.util.List;

public interface StatsService {

    StatsOverviewDTO getStatsOverview();
    List<TopSellersDTO> getTopSellers();
}