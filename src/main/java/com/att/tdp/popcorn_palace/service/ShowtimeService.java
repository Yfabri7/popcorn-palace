package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Showtime;

import java.util.List;

public interface ShowtimeService {
    Showtime createShowtime(Showtime showtime);
    Showtime getShowtime(Long id);
    Showtime updateShowtime(Long id, Showtime updated);
    void deleteShowtime(Long id);
    List<Showtime> getAllShowtimes();
}