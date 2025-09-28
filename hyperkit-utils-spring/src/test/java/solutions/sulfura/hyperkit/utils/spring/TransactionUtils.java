package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
public class TransactionUtils {


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T runOnNewTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

}
