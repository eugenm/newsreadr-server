package de.patrickgotthard.newsreadr.server.test;

import static de.patrickgotthard.newsreadr.server.test.Tests.ADMIN_BASE_AUTH;
import static de.patrickgotthard.newsreadr.server.test.Tests.USER_BASE_AUTH;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import de.patrickgotthard.newsreadr.server.config.MvcConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfig.class, MvcConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@Transactional
@DatabaseSetup("classpath:testdata.xml")
public abstract class AbstractIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilter;

    protected MockMvc mvc;

    @Before
    public void setup() {
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

    @After
    public void cleanup() {
        SecurityContextHolder.clearContext();
    }

}
