package ch.zhaw.parkship.services;

import java.util.*;

import ch.zhaw.parkship.dtos.ParkingLotSearchDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.parkship.dtos.ParkingLotDto;
import ch.zhaw.parkship.entities.ParkingLotEntity;
import ch.zhaw.parkship.repositories.ParkingLotRepository;

@Service
public class ParkingLotService implements CRUDServiceInterface<ParkingLotDto, Long> {

	@Autowired
	private ParkingLotRepository parkingLotRepository;

	@Override
	public Optional<ParkingLotDto> create(ParkingLotDto data) {
		var parkingLotEntity = new ParkingLotEntity();
		BeanUtils.copyProperties(data, parkingLotEntity);
		var savedEntity = parkingLotRepository.save(parkingLotEntity);
		return Optional.of(new ParkingLotDto(savedEntity));
	}

	@Override
	public Optional<ParkingLotDto> getById(Long id) {
		var parkingLotEntity = parkingLotRepository.findById(id);
		if (parkingLotEntity.isPresent()) {
			return Optional.of(new ParkingLotDto(parkingLotEntity.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<ParkingLotDto> getAll() {
		var parkingLotEntities = parkingLotRepository.findAll();
		List<ParkingLotDto> parkingLotDtos = new ArrayList<>();
		for (ParkingLotEntity entity : parkingLotEntities) {
			parkingLotDtos.add(new ParkingLotDto(entity));
		}
		return parkingLotDtos;
	}

	@Override
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

	@Override
	public Optional<ParkingLotDto> deleteById(Long id) {
		var optionalEntity = parkingLotRepository.findById(id);
		if (optionalEntity.isPresent()) {
			var parkingLotEntity = optionalEntity.get();
			parkingLotRepository.deleteById(id);
			return Optional.of(new ParkingLotDto(parkingLotEntity));
		}
		return Optional.empty();
	}

	public List<ParkingLotDto> getBySearchTerm(ParkingLotSearchDto parkingLotSearchDto){
		Set<ParkingLotEntity> parkingLots = new HashSet<>();
		String[] searchTerms = parkingLotSearchDto.getSearchTerm().toLowerCase().replaceAll("\\W"," ").split("\\s+");
		for(String term : searchTerms){
			parkingLots.addAll(parkingLotRepository.findAllByDescriptionContainsIgnoreCase(term));
		}

		List<ParkingLotDto> parkingLotDtos = new ArrayList<>();
		for (ParkingLotEntity entity : parkingLots) {
			parkingLotDtos.add(new ParkingLotDto(entity));
		}
		return parkingLotDtos;
	}
}
