package fbp.app.service;

import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.family.FamilyDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.FamilyMapper;
import fbp.app.model.Family;
import fbp.app.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FamilyService {
    private final FamilyRepository familyRepository;

    @Transactional
    public FamilyDto createAndReturnFamilyDto(CreateFamilyDtoRequest request){
        Family family = familyRepository.save(FamilyMapper.toFamily(request));
        return FamilyMapper.toFamilyDto(family);
    }

    @Transactional
    public Family createFamily(CreateFamilyDtoRequest request){
        return familyRepository.save(FamilyMapper.toFamily(request));
    }

    public Family findFamilyById(Long id){
        Optional<Family> optionalFamily = familyRepository.findById(id);
        if (optionalFamily.isEmpty()) {
            String message = String.format("Family with specified id %s not found.", id);
            log.error(message);
            throw new UserServiceException(message, HttpStatus.BAD_REQUEST);
        }

        return optionalFamily.get();
    }
}
