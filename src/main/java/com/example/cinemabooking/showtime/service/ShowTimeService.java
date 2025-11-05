package com.example.cinemabooking.showtime.service;

import com.example.cinemabooking.showtime.repository.ShowTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;

}
