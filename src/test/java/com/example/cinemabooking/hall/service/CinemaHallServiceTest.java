package com.example.cinemabooking.hall.service;

import com.example.cinemabooking.TestFixtures;
import com.example.cinemabooking.hall.dto.CinemaHallResponse;
import com.example.cinemabooking.hall.dto.CreateCinemaHallRequest;
import com.example.cinemabooking.hall.dto.UpdateCinemaHallRequest;
import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.hall.repository.CinemaHallRepository;
import com.example.cinemabooking.hall.service.exception.CinemaHallAlreadyExistsException;
import com.example.cinemabooking.hall.service.exception.CinemaHallNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CinemaHallServiceTest {

    @Mock
    private CinemaHallRepository cinemaHallRepository;

    @InjectMocks
    private CinemaHallService cinemaHallService;

    private CinemaHall cinemaHall;

    @BeforeEach
    void setUp() {
        cinemaHall = TestFixtures.cinemaHallWithId();
    }

    @Test
    @DisplayName("should return all cinema halls")
    void shouldReturnAllCinemaHalls() {
        // given
        given(cinemaHallRepository.findAll()).willReturn(List.of(cinemaHall));
        // when
        List<CinemaHallResponse> cinemaHalls = cinemaHallService.getAllCinemaHalls();
        // then
        assertThat(cinemaHalls).hasSize(1);
        assertThat(cinemaHalls.getFirst().getName()).isEqualTo("Hall A");
        assertThat(cinemaHalls.getFirst().getRows()).isEqualTo(5);
        assertThat(cinemaHalls.getFirst().getSeatsPerRow()).isEqualTo(10);
        verify(cinemaHallRepository).findAll();
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should return cinema hall by id when exists")
    void shouldReturnCinemaHallByIdWhenExists() {
        // given
        given(cinemaHallRepository.findById(1L)).willReturn(Optional.of(cinemaHall));
        // when
        CinemaHallResponse cinemaHallResponse = cinemaHallService.getCinemaHallById(1L);
        // then
        assertThat(cinemaHallResponse)
                .extracting(CinemaHallResponse::getName, CinemaHallResponse::getRows, CinemaHallResponse::getSeatsPerRow)
                .containsExactly("Hall A", 5, 10);
        verify(cinemaHallRepository).findById(1L);
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should throw exception when cinema hall not found by id")
    void shouldThrowExceptionWhenCinemaHallNotFoundById() {
        // given
        given(cinemaHallRepository.findById(1L)).willReturn(Optional.empty());
        // when + then
        assertThatThrownBy(() -> cinemaHallService.getCinemaHallById(1L))
                .isInstanceOf(CinemaHallNotFoundException.class);
        verify(cinemaHallRepository).findById(1L);
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should create cinema hall when name not exists")
    void shouldCreateCinemaHallWhenNameNotExists() {
        // given
        CreateCinemaHallRequest createCinemaHallRequest = mock(CreateCinemaHallRequest.class);
        given(createCinemaHallRequest.toCinemaHall()).willReturn(cinemaHall);
        given(cinemaHallRepository.findByName(cinemaHall.getName())).willReturn(Optional.empty());
        given(cinemaHallRepository.save(cinemaHall)).willReturn(cinemaHall);
        // when
        CinemaHallResponse cinemaHallResponse = cinemaHallService.createCinemaHall(createCinemaHallRequest);
        // then
        assertThat(cinemaHallResponse)
                .extracting(CinemaHallResponse::getName, CinemaHallResponse::getRows, CinemaHallResponse::getSeatsPerRow)
                .containsExactly("Hall A", 5, 10);
        assertThat(cinemaHallResponse.getSeats()).hasSize(cinemaHall.getRows() * cinemaHall.getSeatsPerRow());
        verify(cinemaHallRepository).findByName(cinemaHall.getName());
        verify(cinemaHallRepository).save(cinemaHall);
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should throw exception when creating cinema hall with duplicate name")
    void shouldThrowExceptionWhenCreatingCinemaHallWithDuplicateName() {
        // given
        CreateCinemaHallRequest createCinemaHallRequest = mock(CreateCinemaHallRequest.class);
        given(createCinemaHallRequest.toCinemaHall()).willReturn(cinemaHall);
        given(cinemaHallRepository.findByName(cinemaHall.getName())).willReturn(Optional.of(cinemaHall));
        // when + then
        assertThatThrownBy(() -> cinemaHallService.createCinemaHall(createCinemaHallRequest))
                .isInstanceOf(CinemaHallAlreadyExistsException.class);
        verify(cinemaHallRepository).findByName(cinemaHall.getName());
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should update cinema hall when exists and name is unique")
    void shouldUpdateCinemaHallWhenExistsAndNameIsUnique() {
        // given
        UpdateCinemaHallRequest updateCinemaHallRequest = UpdateCinemaHallRequest.builder()
                .name("Hall B")
                .rows(15)
                .seatsPerRow(8)
                .build();
        given(cinemaHallRepository.findById(1L)).willReturn(Optional.of(cinemaHall));
        given(cinemaHallRepository.findByName(updateCinemaHallRequest.getName())).willReturn(Optional.empty());
        // when
        CinemaHallResponse cinemaHallResponse = cinemaHallService.updateCinemaHall(1L, updateCinemaHallRequest);
        // then
        assertThat(cinemaHallResponse)
                .extracting(CinemaHallResponse::getName, CinemaHallResponse::getRows, CinemaHallResponse::getSeatsPerRow)
                .containsExactly("Hall B", 15, 8);
        assertThat(cinemaHallResponse.getSeats()).hasSize(updateCinemaHallRequest.getRows() * updateCinemaHallRequest.getSeatsPerRow());
        verify(cinemaHallRepository).findById(1L);
        verify(cinemaHallRepository).findByName("Hall B");
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should throw exception when updating cinema hall not found")
    void shouldThrowExceptionWhenUpdatingCinemaHallNotFound() {
        // given
        UpdateCinemaHallRequest updateCinemaHallRequest = mock(UpdateCinemaHallRequest.class);
        given(cinemaHallRepository.findById(1L)).willReturn(Optional.empty());
        // when + then
        assertThatThrownBy(() -> cinemaHallService.updateCinemaHall(1L, updateCinemaHallRequest))
                .isInstanceOf(CinemaHallNotFoundException.class);
        verify(cinemaHallRepository).findById(1L);
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should throw exception when updating cinema hall with duplicate name")
    void shouldThrowExceptionWhenUpdatingCinemaHallWithDuplicateName() {
        // given
        UpdateCinemaHallRequest updateCinemaHallRequest = mock(UpdateCinemaHallRequest.class);
        given(cinemaHallRepository.findById(1L)).willReturn(Optional.of(cinemaHall));
        given(updateCinemaHallRequest.getName()).willReturn("Duplicate name");
        CinemaHall cinemaHallWithDuplicateName = new CinemaHall();
        cinemaHallWithDuplicateName.setId(3L);
        cinemaHallWithDuplicateName.setName("Duplicate name");
        cinemaHallWithDuplicateName.setRows(2);
        cinemaHallWithDuplicateName.setSeatsPerRow(8);

        given(cinemaHallRepository.findByName("Duplicate name")).willReturn(Optional.of(cinemaHallWithDuplicateName));
        // when + then
        assertThatThrownBy(() -> cinemaHallService.updateCinemaHall(1L, updateCinemaHallRequest))
                .isInstanceOf(CinemaHallAlreadyExistsException.class);
        verify(cinemaHallRepository).findById(1L);
        verify(cinemaHallRepository).findByName("Duplicate name");
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should delete cinema hall when exists")
    void shouldDeleteCinemaHallWhenExists() {
        // given
        given(cinemaHallRepository.findById(1L)).willReturn(Optional.of(cinemaHall));
        // when
        cinemaHallService.deleteCinemaHall(1L);
        // then
        verify(cinemaHallRepository).findById(1L);
        verify(cinemaHallRepository).delete(cinemaHall);
        verifyNoMoreInteractions(cinemaHallRepository);
    }

    @Test
    @DisplayName("should throw exception when deleting non-existent cinema hall")
    void shouldThrowExceptionWhenDeletingNonExistentCinemaHall() {
        // given
        given(cinemaHallRepository.findById(1L)).willReturn(Optional.empty());
        // when + then
        assertThatThrownBy(() -> cinemaHallService.deleteCinemaHall(1L))
                .isInstanceOf(CinemaHallNotFoundException.class);
        verify(cinemaHallRepository).findById(1L);
        verifyNoMoreInteractions(cinemaHallRepository);
    }

}