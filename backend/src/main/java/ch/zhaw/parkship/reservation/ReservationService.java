package ch.zhaw.parkship.reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.user.UserRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ParkingLotRepository parkingLotRepository;

	@Autowired
	private UserRepository userRepository;

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

	public Optional<ReservationDto> getById(Long id) {
		var reservationEntity = reservationRepository.findById(id);
		if (reservationEntity.isPresent()) {
			return Optional.of(new ReservationDto(reservationEntity.get()));
		}
		return Optional.empty();
	}

	public List<ReservationDto> getAll() {
		var reservationEntities = reservationRepository.findAll();
		List<ReservationDto> reservationDtos = new ArrayList<>();
		for (ReservationEntity entity : reservationEntities) {
			reservationDtos.add(new ReservationDto(entity));
		}
		return reservationDtos;
	}

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
