package fbp.app.rest;

import fbp.app.dto.mandatory_payment.CreateMandatoryPaymentDtoRequest;
import fbp.app.dto.mandatory_payment.MandatoryPaymentDto;
import fbp.app.service.MandatoryPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/mandatory-payment")
@RequiredArgsConstructor
public class MandatoryPaymentController {
    private final MandatoryPaymentService mandatoryPaymentService;

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @PostMapping
    public ResponseEntity<MandatoryPaymentDto> createMandatoryPayment(@RequestBody @Valid CreateMandatoryPaymentDtoRequest paymentRequest) {
        return ResponseEntity.ok(mandatoryPaymentService.createMandatoryPayment(paymentRequest));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<MandatoryPaymentDto> getMandatoryPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(mandatoryPaymentService.getMandatoryPaymentById(paymentId));
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<List<MandatoryPaymentDto>> getAllFamilyMandatoryPayments(@PathVariable Long familyId) {
        return ResponseEntity.ok(mandatoryPaymentService.getAllFamilyMandatoryPayments(familyId));
    }

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @PutMapping("/{paymentId}")
    public ResponseEntity<MandatoryPaymentDto> closeMandatoryPayment(@PathVariable Long paymentId){
        return ResponseEntity.ok(mandatoryPaymentService.closeMandatoryPayment(paymentId));
    }

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deleteMandatoryPayment(@PathVariable Long paymentId) {
        mandatoryPaymentService.deleteMandatoryPayment(paymentId);
        return ResponseEntity.noContent().build();
    }
}
