package fbp.app.mapper;

import fbp.app.dto.mandatory_payment.CreateMandatoryPaymentDtoRequest;
import fbp.app.dto.mandatory_payment.MandatoryPaymentDto;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Family;
import fbp.app.model.MandatoryPayment;

import java.time.Instant;
import java.util.List;

public class MandatoryPaymentMapper {
    public static MandatoryPayment toModel(CreateMandatoryPaymentDtoRequest request,
                                           BudgetPeriod period, Family family){
        return MandatoryPayment.builder()
                .name(request.getName())
                .createdAt(Instant.now())
                .period(period)
                .amount(request.getAmount())
                .isPaid(false)
                .family(family)
                .build();
    }

    public static MandatoryPaymentDto toDto(MandatoryPayment model){
        return MandatoryPaymentDto.builder()
                .id(model.getId())
                .familyId(model.getFamily().getId())
                .periodId(model.getPeriod().getId())
                .isPaid(model.getIsPaid())
                .name(model.getName())
                .amount(model.getAmount())
                .createdAt(model.getCreatedAt())
                .build();
    }

    public static List<MandatoryPaymentDto> toDto(List<MandatoryPayment> models){
        return models.stream().map(MandatoryPaymentMapper::toDto).toList();
    }
}
