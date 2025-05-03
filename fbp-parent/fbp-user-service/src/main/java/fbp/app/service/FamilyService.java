package fbp.app.service;

import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.family.FamilyDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.FamilyMapper;
import fbp.app.model.Family;
import fbp.app.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;

    public FamilyDto createFamily(CreateFamilyDtoRequest request){
        validateFamilyRequest(request);

        Family family = familyRepository.save(FamilyMapper.toFamily(request));
        return FamilyMapper.toFamilyDto(family);
    }

    private void validateFamilyRequest(CreateFamilyDtoRequest request){
        if(request == null){
            throw new UserServiceException("Create family request is null", HttpStatus.BAD_REQUEST);
        }
        if(request.getName() == null || request.getName().isEmpty()){
            throw new UserServiceException("Create family request field 'name' is null or empty", HttpStatus.BAD_REQUEST);
        }
        if(request.getDescription() == null || request.getDescription().isEmpty()){
            throw new UserServiceException("Create family request field 'description' is null or empty", HttpStatus.BAD_REQUEST);
        }
    }
}
