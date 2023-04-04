package ch.zhaw.parkship.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.parkship.dtos.ReservationDto;
import ch.zhaw.parkship.entities.ReservationEntity;
import ch.zhaw.parkship.repositories.ParkingLotRepository;
import ch.zhaw.parkship.repositories.ReservationRepository;
import ch.zhaw.parkship.repositories.UserRepository;

@Service
public class ReservationService implements CRUDServiceInterface<ReservationDto, Long> {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ParkingLotRepository parkingLotRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<ReservationDto> create(ReservationDto data) {
		var parkingLot = parkingLotRepository.findById(data.getParkingLot().getId());
		var tenant = userRepository.findById(data.getTenant().getId());
		if (parkingLot.isPresent() && tenant.isPresent()) {
			var reservationEntity = new ReservationEntity();
			BeanUtils.copyProperties(data, reservationEntity);
			reservationEntity.setParkingLot(parkingLot.get());
			reservationEntity.setTenant(tenant.get());
			var savedEntity = reservationRepository.save(reservationEntity);
			return Optional.of(new ReservationDto(savedEntity));
		}
		return Optional.empty();
	}

	public Optional<ReservationDto> create(Long parkingLotId, ReservationDto data) {
		data.getParkingLot().setId(parkingLotId);
		return create(data);
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
	@Transactional
	public Optional<ReservationDto> deleteById(Long id) {
		var optionalEntity = reservationRepository.findById(id);
		if (optionalEntity.isPresent()) {
			var reservationEntity = optionalEntity.get();
			var ret = new ReservationDto(reservationEntity);
			reservationRepository.delete(reservationEntity);
			return Optional.of(ret);
		}
		return Optional.empty();
	}

}
