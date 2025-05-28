package fbp.app.rest;

import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.family.FamilyDto;
import fbp.app.dto.user.AddParentToFamilyDtoRequest;
import fbp.app.dto.user.UserDto;
import fbp.app.dto.user.UserInfoDto;
import fbp.app.model.User;
import fbp.app.service.FamilyService;
import fbp.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/family")
@RequiredArgsConstructor
public class FamilyController {
    private final FamilyService familyService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PARENT')")
    public ResponseEntity<FamilyDto> createFamily(@RequestBody @Valid CreateFamilyDtoRequest request) {
        return ResponseEntity.ok(familyService.createAndReturnFamilyDto(request));
    }

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @PostMapping("/add-parent/{familyId}")
    public ResponseEntity<UserDto> addParentToFamily(@RequestBody @Valid AddParentToFamilyDtoRequest request,
                                                     @PathVariable("familyId") Long familyId) {
        return ResponseEntity.ok(userService.addParentToFamily(request, familyId));
    }

    @GetMapping("/members/{familyId}")
    public ResponseEntity<List<UserInfoDto>> getAllFamilyMembers(@PathVariable Long familyId){
        return ResponseEntity.ok(userService.getAllFamilyMembers(familyId));
    }
}
