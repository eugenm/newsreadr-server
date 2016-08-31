package de.patrickgotthard.newsreadr.server.common.tx;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionHelper {

    /**
     * Calls the given callable in a new transaction.
     * 
     * @param callable The callable to call
     * @return Result of the callable
     */
    @Transactional(propagation = REQUIRES_NEW)
    public <T> T executeInNewTransaction(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
