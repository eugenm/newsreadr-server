package de.patrickgotthard.newsreadr.server.test;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Abstract transactional unit test.
 * 
 * @author Patrick Gotthard
 *
 */
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public abstract class AbstractTransactionalUT {

}