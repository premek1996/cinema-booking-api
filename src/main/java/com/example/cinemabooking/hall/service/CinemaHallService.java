package com.example.cinemabooking.hall.service;

import com.example.cinemabooking.hall.dto.CinemaHallResponse;
import com.example.cinemabooking.hall.dto.CreateCinemaHallRequest;
import com.example.cinemabooking.hall.dto.UpdateCinemaHallRequest;
import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.hall.entity.Seat;
import com.example.cinemabooking.hall.repository.CinemaHallRepository;
import com.example.cinemabooking.hall.service.exception.CinemaHallAlreadyExistsException;
import com.example.cinemabooking.hall.service.exception.CinemaHallNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaHallService {

    private final CinemaHallRepository cinemaHallRepository;

    @Transactional(readOnly = true)
    public List<CinemaHallResponse> getAllCinemaHalls() {
        return cinemaHallRepository.findAll()
                .stream()
                .map(CinemaHallResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public CinemaHallResponse getCinemaHallById(Long id) {
        CinemaHall cinemaHall = getCinemaHallOrThrow(id);
        return CinemaHallResponse.of(cinemaHall);
    }

    private CinemaHall getCinemaHallOrThrow(Long id) {
        return cinemaHallRepository.findById(id)
                .orElseThrow(() -> new CinemaHallNotFoundException(id));
    }

    @Transactional
    public CinemaHallResponse createCinemaHall(CreateCinemaHallRequest createCinemaHallRequest) {
        CinemaHall cinemaHall = createCinemaHallRequest.toCinemaHall();
        if (cinemaHallRepository.findByName(cinemaHall.getName()).isPresent()) {
            throw new CinemaHallAlreadyExistsException(cinemaHall.getName());
        }
        createSeats(cinemaHall);
        return CinemaHallResponse.of(cinemaHallRepository.save(cinemaHall));
    }

    private void createSeats(CinemaHall cinemaHall) {
        for (int row = 1; row <= cinemaHall.getRows(); row++) {
            for (int seatNumber = 1; seatNumber <= cinemaHall.getSeatsPerRow(); seatNumber++) {
                Seat seat = Seat.builder()
                        .rowNumber(row)
                        .seatNumber(seatNumber)
                        .build();
                cinemaHall.addSeat(seat);
            }
        }
    }

    @Transactional
    public CinemaHallResponse updateCinemaHall(Long id, UpdateCinemaHallRequest updateCinemaHallRequest) {
        CinemaHall cinemaHall = getCinemaHallOrThrow(id);
        validateUniqueName(updateCinemaHallRequest.getName(), id);
        applyUpdates(cinemaHall, updateCinemaHallRequest);
        return CinemaHallResponse.of(cinemaHall);
    }

    private void validateUniqueName(String name, Long id) {
        if (cinemaHallRepository.findByName(name)
                .filter(cinemaHall -> !cinemaHall.getId().equals(id))
                .isPresent()) {
            throw new CinemaHallAlreadyExistsException(name);
        }
    }

    private void applyUpdates(CinemaHall cinemaHall, UpdateCinemaHallRequest request) {
        cinemaHall.setName(request.getName());
        cinemaHall.setRows(request.getRows());
        cinemaHall.setSeatsPerRow(request.getSeatsPerRow());
        cinemaHall.getSeats().clear();
        createSeats(cinemaHall);
    }

    @Transactional
    public void deleteCinemaHall(Long id) {
        CinemaHall cinemaHall = getCinemaHallOrThrow(id);
        cinemaHallRepository.delete(cinemaHall);
    }

}
