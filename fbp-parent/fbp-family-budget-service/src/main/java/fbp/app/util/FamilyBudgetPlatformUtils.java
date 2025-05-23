package fbp.app.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FamilyBudgetPlatformUtils {

    public String getCurrentUserKkId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
