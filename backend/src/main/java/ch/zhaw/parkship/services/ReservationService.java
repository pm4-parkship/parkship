package ch.zhaw.parkship.services;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.parkship.dtos.ReservationDto;
import ch.zhaw.parkship.entities.ReservationEntity;
import ch.zhaw.parkship.repositories.ReservationRepository;

@Service
public class ReservationService implements CRUDServiceInterface<ReservationDto, Long> {

	@Autowired
	private ReservationRepository reservationRepository;

	@Override
	public Optional<ReservationDto> create(ReservationDto data) {
		var reservationEntity = new ReservationEntity();
		BeanUtils.copyProperties(data, reservationEntity);
		var savedEntity = reservationRepository.save(reservationEntity);
		return Optional.of(new ReservationDto(savedEntity));
	}

	@Override
	public Optional<ReservationDto> getById(Long id) {
		var reservationEntity = reservationRepository.findById(id);
		if (reservationEntity.isPresent()) {
			return Optional.of(new ReservationDto(reservationEntity.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<ReservationDto> getAll() {
		var reservationEntities = reservationRepository.findAll();
		List<ReservationDto> reservationDtos = new ArrayList<>();
		for (ReservationEntity entity : reservationEntities) {
			reservationDtos.add(new ReservationDto(entity));
		}
		return reservationDtos;
	}

	@Override
	public Optional<ReservationDto> update(ReservationDto data) {
		var optionalEntity = reservationRepository.findById(data.getId());
		if (optionalEntity.isPresent()) {
			var reservationEntity = optionalEntity.get();
			BeanUtils.copyProperties(data, reservationEntity);
			var updatedEntity = reservationRepository.save(reservationEntity);
			return Optional.of(new ReservationDto(updatedEntity));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ReservationDto> deleteById(Long id) {
		var optionalEntity = reservationRepository.findById(id);
		if (optionalEntity.isPresent()) {
			var reservationEntity = optionalEntity.get();
			reservationRepository.deleteById(id);
			return Optional.of(new ReservationDto(reservationEntity));
		}
		return Optional.empty();
	}


	public boolean isFreeInDateRange(Long id, LocalDate startDate, LocalDate endDate){
		return reservationRepository.findAllWithOverlappingDates(id,startDate,endDate).isEmpty();
	}
}
