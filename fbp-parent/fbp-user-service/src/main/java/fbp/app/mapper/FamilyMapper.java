package fbp.app.mapper;

import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.family.FamilyDto;
import fbp.app.model.Family;

public class FamilyMapper {
    public static Family toFamily(CreateFamilyDtoRequest request){
        return Family.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static FamilyDto toFamilyDto(Family family) {
        return FamilyDto.builder()
                .id(family.getId())
                .name(family.getName())
                .description(family.getDescription())
                .createdAt(family.getCreatedAt())
                .build();
    }
}
