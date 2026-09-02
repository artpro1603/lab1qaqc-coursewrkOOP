package ua.opnu.labwork21.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork21.dto.request.SeatClassCreateRequest;
import ua.opnu.labwork21.dto.request.SeatClassUpdateRequest;
import ua.opnu.labwork21.dto.response.SeatClassResponse;
import ua.opnu.labwork21.exception.ResourceNotFoundException;
import ua.opnu.labwork21.model.SeatClass;
import ua.opnu.labwork21.repository.SeatClassRepository;

import java.util.List;

@Service
public class SeatClassService {

    private final SeatClassRepository seatClassRepository;

    public SeatClassService(SeatClassRepository seatClassRepository) {
        this.seatClassRepository = seatClassRepository;
    }

    public SeatClassResponse create(SeatClassCreateRequest request) {
        SeatClass seatClass = new SeatClass();
        seatClass.setName(request.name());
        seatClass.setDescription(request.description());
        return toResponse(seatClassRepository.save(seatClass));
    }

    public List<SeatClassResponse> getAll() {
        return seatClassRepository.findAll().stream().map(this::toResponse).toList();
    }

    public SeatClassResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public SeatClassResponse update(Long id, SeatClassUpdateRequest request) {
        SeatClass seatClass = findById(id);
        seatClass.setName(request.name());
        seatClass.setDescription(request.description());
        return toResponse(seatClassRepository.save(seatClass));
    }

    public void delete(Long id) {
        findById(id);
        seatClassRepository.deleteById(id);
    }

    private SeatClass findById(Long id) {
        return seatClassRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SeatClass not found: " + id));
    }

    private SeatClassResponse toResponse(SeatClass sc) {
        return new SeatClassResponse(sc.getId(), sc.getName(), sc.getDescription());
    }
}
