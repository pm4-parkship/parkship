package ch.zhaw.parkship.parking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;

import ch.zhaw.parkship.parking.dto.ReservationDto;
import ch.zhaw.parkship.parking.entity.ParkingLotEntity;
import ch.zhaw.parkship.parking.entity.ReservationEntity;
import ch.zhaw.parkship.parking.repository.ParkingLotRepository;
import ch.zhaw.parkship.parking.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ReservationService implements CRUDServiceInterface<ReservationDto, Long>{
	private final ReservationRepository reservationRepository;
	private final ParkingLotRepository parkingLotRepository;

	public ReservationService(ReservationRepository reservationRepository, ParkingLotRepository parkingLotRepository) {
		this.reservationRepository = reservationRepository;
		this.parkingLotRepository = parkingLotRepository;
	}

	@Override
	public List<ReservationDto> readAll() {
		return reservationRepository.findAll().stream().map(ReservationDto::new).toList();
	}

	@Override
	public ReservationDto create(ReservationDto reservation) {
		var parkingLotEntity = parkingLotRepository.findById(reservation.getParkingLot().getId())
				.orElseThrow(EntityNotFoundException::new);
		var reservationEntity = new ReservationEntity();
		BeanUtils.copyProperties(reservation, reservationEntity);
		reservationEntity.setParkingLot(parkingLotEntity);
		return new ReservationDto(reservationRepository.save(reservationEntity));
	}

	@Override
	public void delete(Long id) {
		var reservationEntity = reservationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		reservationRepository.delete(reservationEntity);
	}

	@Override
	public Optional<ReservationDto> getById(Long id) {
		return reservationRepository.findById(id).map(ReservationDto::new);
	}

	@Override
	public ReservationDto update(Long id, ReservationDto data) {
		var reservationEntity = reservationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		BeanUtils.copyProperties(data, reservationEntity);

		return new ReservationDto(reservationRepository.save(reservationEntity));
	}
}
