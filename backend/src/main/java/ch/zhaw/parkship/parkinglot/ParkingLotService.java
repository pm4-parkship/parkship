package ch.zhaw.parkship.parkinglot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.parkship.user.UserRepository;

@Service
@Transactional
public class ParkingLotService {

	@Autowired
	private ParkingLotRepository parkingLotRepository;

	@Autowired
	private UserRepository userRepository;

	public Optional<ParkingLotDto> create(ParkingLotDto data) {
		var owner = userRepository.findById(data.getOwner().getId());
		if (owner.isPresent()) {
			var parkingLotEntity = new ParkingLotEntity();
			parkingLotEntity.setOwner(owner.get());
			BeanUtils.copyProperties(data, parkingLotEntity);
			parkingLotEntity.setId(null);
			var savedEntity = parkingLotRepository.save(parkingLotEntity);
			return Optional.of(new ParkingLotDto(savedEntity));
		}
		return Optional.empty();
	}

	public Optional<ParkingLotDto> getById(Long id) {
		var parkingLotEntity = parkingLotRepository.findById(id);
		if (parkingLotEntity.isPresent()) {
			return Optional.of(new ParkingLotDto(parkingLotEntity.get()));
		}
		return Optional.empty();
	}

	public List<ParkingLotDto> getAll() {
		var parkingLotEntities = parkingLotRepository.findAll();
		List<ParkingLotDto> parkingLotDtos = new ArrayList<>();
		for (ParkingLotEntity entity : parkingLotEntities) {
			parkingLotDtos.add(new ParkingLotDto(entity));
		}
		return parkingLotDtos;
	}

	public Optional<ParkingLotDto> update(ParkingLotDto data) {
		var optionalEntity = parkingLotRepository.findById(data.getId());
		if (optionalEntity.isPresent()) {
			var parkingLotEntity = optionalEntity.get();
			BeanUtils.copyProperties(data, parkingLotEntity);
			var updatedEntity = parkingLotRepository.save(parkingLotEntity);
			return Optional.of(new ParkingLotDto(updatedEntity));
		}
		return Optional.empty();
	}

	public Optional<ParkingLotDto> deleteById(Long id) {
		var optionalEntity = parkingLotRepository.findById(id);
		if (optionalEntity.isPresent()) {
			var parkingLotEntity = optionalEntity.get();
			var ret = Optional.of(new ParkingLotDto(parkingLotEntity));
			parkingLotRepository.deleteById(id);
			return ret;
		}
		return Optional.empty();
	}
}
