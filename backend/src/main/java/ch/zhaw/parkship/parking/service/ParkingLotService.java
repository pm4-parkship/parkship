package ch.zhaw.parkship.parking.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import ch.zhaw.parkship.parking.dto.ParkingLotSearchDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ch.zhaw.parkship.parking.dto.ParkingLotDto;
import ch.zhaw.parkship.parking.entity.ParkingLotEntity;
import ch.zhaw.parkship.parking.repository.ParkingLotRepository;
import jakarta.persistence.EntityNotFoundException;

@Component
public class ParkingLotService implements CRUDServiceInterface<ParkingLotDto, Long> {
	private final ParkingLotRepository parkingLotRepository;

	public ParkingLotService(ParkingLotRepository parkingLotRepository) {
		this.parkingLotRepository = parkingLotRepository;
	}

	@Override
	public List<ParkingLotDto> readAll() {
		return parkingLotRepository.findAll().stream().map(ParkingLotDto::new).toList();
	}

	@Override
	public ParkingLotDto create(ParkingLotDto parkingLot) {
		var parkingLotEntity = new ParkingLotEntity();
		BeanUtils.copyProperties(parkingLot, parkingLotEntity);
		return new ParkingLotDto(parkingLotRepository.save(parkingLotEntity));
	}

	@Override
	public Optional<ParkingLotDto> getById(Long id) {
		return parkingLotRepository.findById(id).map(ParkingLotDto::new);
	}

	@Override
	public ParkingLotDto update(Long id, ParkingLotDto data) {
		var parkingLotEntity = parkingLotRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		BeanUtils.copyProperties(data, parkingLotEntity);

		return new ParkingLotDto(parkingLotRepository.save(parkingLotEntity));
	}

	@Override
	public void delete(Long id) {
		var parkingLotEntity = parkingLotRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		parkingLotRepository.delete(parkingLotEntity);
	}

	public List<ParkingLotDto> getBySearchTerm(ParkingLotSearchDto parkingLotSearchDto){
		return parkingLotRepository.findAll().stream().map(ParkingLotDto::new).toList();
	}


}
