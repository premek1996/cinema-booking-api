package com.example.cinemabooking.showtime.service;

import com.example.cinemabooking.showtime.dto.CreateShowTimeRequest;
import com.example.cinemabooking.showtime.dto.ShowTimeResponse;
import com.example.cinemabooking.showtime.repository.ShowTimeRepository;
import com.example.cinemabooking.showtime.service.exception.ShowTimeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;

    @Transactional(readOnly = true)
    public List<ShowTimeResponse> getAllShowTimes() {
        return showTimeRepository.findAll()
                .stream().map(ShowTimeResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public ShowTimeResponse getShowTimeById(Long id) {
        return showTimeRepository.findById(id)
                .map(ShowTimeResponse::of)
                .orElseThrow(() -> new ShowTimeNotFoundException(id));
    }

    @Transactional
    public ShowTimeResponse createShowTime(CreateShowTimeRequest createShowTimeRequest) {
        return null;
    }

}
