package com.toptal.essienntaemmanuel2ndattempt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.essienntaemmanuel2ndattempt.service.api.MailSender;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author bodmas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AccountResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailSender mailSender;

    private static final SessionHolder sessionHolder = new SessionHolder();

    private static final String email = "test1@toptal.com";
    private static int count;

    public AccountResourceTest() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        String password = "secret";

        if (count++ == 0) {
            // Create account
            mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}")
                    .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                    .andExpect(status().isCreated())
                    .andExpect(redirectedUrl("http://localhost/accounts/" + email));

            // Verify account
            mockMvc.perform(get("/accounts/" + email + "/verify/testtoken")
                    .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                    .andExpect(status().isOk());

            // Login
            mockMvc.perform(formLogin().user(email).password(password))
                    .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));
        }
    }

    @Test
    public void getAllShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/accounts")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getMeShouldReturnOk() throws Exception {
        mockMvc.perform(get("/accounts/me")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAll_3args() throws Exception {
        mockMvc.perform(get("/accounts").param("page", "0").param("size", "5")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAccount() throws Exception {
        mockMvc.perform(get("/accounts").param("email", RandomStringUtils.randomAlphabetic(10))
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testSendNewToken() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        mockMvc.perform(post("/accounts/" + email + "/sendtoken")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUnblock() throws Exception {
        String email = RandomStringUtils.randomAlphabetic(10);
        mockMvc.perform(post("/accounts/" + email + "/unblock")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testSetExpectedCalories() throws Exception {
        mockMvc.perform(post("/accounts/" + email + "/settings/calories/expect/25")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) throws Exception {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final class SessionHolder {

        private SessionWrapper session;

        public SessionWrapper getSession() {
            return session;
        }

        public void setSession(SessionWrapper session) {
            this.session = session;
        }
    }

    private static class SessionWrapper extends MockHttpSession {

        private final HttpSession httpSession;

        public SessionWrapper(HttpSession httpSession) {
            this.httpSession = httpSession;
        }

        @Override
        public Object getAttribute(String name) {
            return this.httpSession.getAttribute(name);
        }
    }
}
