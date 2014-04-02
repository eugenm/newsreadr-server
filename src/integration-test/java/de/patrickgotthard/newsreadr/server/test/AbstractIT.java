package de.patrickgotthard.newsreadr.server.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import de.patrickgotthard.newsreadr.server.config.MvcConfig;

/**
 * Abstract integration test.
 *
 * @author Patrick Gotthard
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfig.class, MvcConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@Transactional
@DatabaseSetup("classpath:testdata.xml")
public abstract class AbstractIT {

    protected static final long USER_USER_ID = 1;
    protected static final long ADMIN_USER_ID = 2;

    protected static final String USER_USERNAME = "user";
    protected static final String ADMIN_USERNAME = "admin";
    protected static final String UNKNOWN_USERNAME = "unknown";

    protected static final String USER_PASSWORD = "$2a$10$nt0ls1ZI48ncyNi44jwp7.ZX2lkbZR.h9rOF2DSZq.gqYvXlPq0U.";
    protected static final String ADMIN_PASSWORD = "$2a$10$Oy7eCE8k68EOMXpSbWYNiufkQmTCgpglzF.cSVFFUe2Na6RNEx4M.";

    private static final String ADMIN_BASE_AUTH = "Basic YWRtaW46YWRtaW4=";
    private static final String USER_BASE_AUTH = "Basic dXNlcjp1c2Vy";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilter;

    private ObjectMapper jsonMapper;
    protected MockMvc mvc;

    @Before
    public void setup() {
        jsonMapper = new ObjectMapper();
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilter).alwaysExpect(status().isOk()).build();
    }

    protected MockHttpServletRequestBuilder getAsAdmin(final String url) {
        return get(url).header("Authorization", ADMIN_BASE_AUTH);
    }

    protected MockHttpServletRequestBuilder getAsUser(final String url) {
        return get(url).header("Authorization", USER_BASE_AUTH);
    }

    private MockHttpServletRequestBuilder get(final String url) {
        return MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
    }

    protected void assertResponse(final ResultActions result, final Object value) {
        try {
            final String expected = jsonMapper.writeValueAsString(value);
            final String actual = result.andReturn().getResponse().getContentAsString();
            assertThat(actual, is(expected));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void cleanup() {
        SecurityContextHolder.clearContext();
    }

}
