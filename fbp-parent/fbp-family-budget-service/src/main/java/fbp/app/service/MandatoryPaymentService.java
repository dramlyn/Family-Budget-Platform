package fbp.app.service;

import fbp.app.dto.mandatory_payment.CreateMandatoryPaymentDtoRequest;
import fbp.app.dto.mandatory_payment.MandatoryPaymentDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.MandatoryPaymentMapper;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Family;
import fbp.app.model.MandatoryPayment;
import fbp.app.repository.MandatoryPaymentRepository;
import fbp.app.util.BudgetPeriodUtil;
import fbp.app.util.ModelFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MandatoryPaymentService {
    private final MandatoryPaymentRepository mandatoryPaymentRepository;
    private final ModelFinder modelFinder;
    private final BudgetPeriodUtil budgetPeriodUtil;

    @Transactional
    public MandatoryPaymentDto createMandatoryPayment(CreateMandatoryPaymentDtoRequest mandatoryPaymentDto) {
        Family family = modelFinder.findFamily(mandatoryPaymentDto.getFamilyId());
        BudgetPeriod budgetPeriod = budgetPeriodUtil.getOrCreateCurrentBudgetPeriod();

        MandatoryPayment mandatoryPayment = MandatoryPaymentMapper.toModel(mandatoryPaymentDto,
                budgetPeriod, family);

        try {
            return MandatoryPaymentMapper.toDto(mandatoryPaymentRepository.save(mandatoryPayment));
        } catch (Exception e){
            String message = "Error while create of mandatory payment: %s".formatted(e.getMessage());
            log.error(message, e);
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public MandatoryPaymentDto getMandatoryPaymentById(Long id) {
        MandatoryPayment mandatoryPayment = mandatoryPaymentRepository
                .findById(id).orElseThrow(() -> new UserServiceException("Mandatory payment with id %s not found".formatted(id),
                        HttpStatus.NOT_FOUND));

        return MandatoryPaymentMapper.toDto(mandatoryPayment);
    }

    public List<MandatoryPaymentDto> getAllFamilyMandatoryPayments(Long familyId) {
        return MandatoryPaymentMapper.toDto(mandatoryPaymentRepository.findByFamilyId(familyId));
    }

    @Transactional
    public MandatoryPaymentDto closeMandatoryPayment(Long mandatoryPaymentId){
        MandatoryPayment mandatoryPayment = mandatoryPaymentRepository.findById(mandatoryPaymentId)
                .orElseThrow(() -> new UserServiceException("Mandatory payment with id %s not found".formatted(mandatoryPaymentId),
                        HttpStatus.NOT_FOUND));
        if(mandatoryPayment.getIsPaid()){
            throw new UserServiceException("Mandatory payment with id %s is already paid.".formatted(mandatoryPaymentId), HttpStatus.BAD_REQUEST);
        }

        mandatoryPayment.setIsPaid(true);
        return MandatoryPaymentMapper.toDto(mandatoryPaymentRepository.save(mandatoryPayment));
    }

    @Transactional
    public void deleteMandatoryPayment(Long mandatoryPaymentId){
        try{
            mandatoryPaymentRepository.deleteById(mandatoryPaymentId);
        } catch (Exception e){
            String message = "Error while delete of mandatory payment with id %s: %s".formatted(mandatoryPaymentId, e.getMessage());
            log.error(message, e);
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
