package com.toptal.essienntaemmanuel2ndattempt.rest;

import com.toptal.essienntaemmanuel2ndattempt.service.api.MailSender;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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
    private static final String password = "secret";
    private static int count;

    public AccountResourceTest() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
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
        }

        // Login
        mockMvc.perform(formLogin().user(email).password(password))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));
    }

    @After
    public void tearDown() throws Exception {
        // Logout
        mockMvc.perform(post("/logout")
                .session(sessionHolder.getSession()));
    }

    @Test
    public void getAllWhenPrincipalIsUserShouldReturnForbidden() throws Exception {
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
    public void getAllWhenPrincipalIsAdminOrManagerShouldReturnAccountsAppropriateToRole() throws Exception {
        // Logout the user
        tearDown();

        // Login the admin
        mockMvc.perform(formLogin().user("admin").password("admin"))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get("/accounts")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(3)))
                .andExpect(jsonPath("$[*].roles[*]", contains("ADMIN", "USER-MANAGER", "USER")))
                .andExpect(jsonPath("$[*].email", contains("admin", "manager", email)));

        // Logout the admin
        tearDown();

        // Login the manager
        mockMvc.perform(formLogin().user("manager").password("manager"))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get("/accounts")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                // Managers can only CRUD users.
                .andExpect(jsonPath("$.length()").value(equalTo(1)))
                .andExpect(jsonPath("$[*].roles[*]", not(contains("ADMIN", "USER-MANAGER"))))
                .andExpect(jsonPath("$[*].email", not(contains("admin", "manager"))))
                .andExpect(jsonPath("$[*].roles[*]", contains("USER")))
                .andExpect(jsonPath("$[*].email", contains(email)));
    }

    @Test
    public void testGetAll_3args() throws Exception {
        // Logout the user
        tearDown();

        // Login the admin
        mockMvc.perform(formLogin().user("admin").password("admin"))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get("/accounts").param("page", "0").param("size", "1")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(1)));

        mockMvc.perform(get("/accounts").param("page", "0").param("size", "2")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)));

        mockMvc.perform(get("/accounts").param("page", "0").param("size", "3")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)));

        mockMvc.perform(get("/accounts").param("page", "0").param("size", "10")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)));

        mockMvc.perform(get("/accounts").param("page", "5").param("size", "10")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(0)));

        // Logout the admin
        tearDown();

        // Login the manager
        mockMvc.perform(formLogin().user("manager").password("manager"))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get("/accounts").param("page", "0").param("size", "1")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(1)));

        mockMvc.perform(get("/accounts").param("page", "0").param("size", "3")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(1)));

        mockMvc.perform(get("/accounts").param("page", "1").param("size", "3")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(0)));
    }

    @Test
    public void getAccountWhenUserIsPrincipalShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/accounts/" + email)
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAccountWhenUserIsAdminOrManagerShouldReturnOk() throws Exception {
        // Logout the user
        tearDown();

        // Login the admin
        mockMvc.perform(formLogin().user("admin").password("admin"))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get("/accounts/" + email)
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(email)));

        // Logout the admin
        tearDown();

        // Login the manager
        mockMvc.perform(formLogin().user("manager").password("manager"))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get("/accounts/" + email)
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(email)));
    }

    @Test
    public void sendTokenWhenPrincipalIsUserShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/accounts/" + email + "/sendtoken")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUnblock() throws Exception {
        // Logout the user
        tearDown();

        // Attempt login with bad user credentials, 3 times.
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(formLogin().user(email).password(password + "bad"))
                    .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));
        }

        // Now attempt login with the correct credentials, and ascertain that user is blocked.
        mockMvc.perform(formLogin().user(email).password(password))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())))
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("error")));

        // Login the admin
        mockMvc.perform(formLogin().user("admin").password("admin"))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())))
                .andExpect(header().string(HttpHeaders.LOCATION, not(containsString("error"))));

        // Unblock the user.
        mockMvc.perform(post("/accounts/" + email + "/unblock")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk());

        // Logout the admin
        tearDown();

        // Now attempt to re-login the user, with correct credentials, and ascertain that the login attempt was successful.
        mockMvc.perform(formLogin().user(email).password(password))
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())))
                .andExpect(header().string(HttpHeaders.LOCATION, not(containsString("error")))); // Redirect to home.
        mockMvc.perform(get("/")
                .session(sessionHolder.getSession()))
                .andExpect(status().isOk())
                .andExpect(content().string("welcome"));
    }

    @Test
    public void testSetExpectedCalories() throws Exception {
        mockMvc.perform(post("/accounts/" + email + "/settings/calories/expect/25")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void testVerify() throws Exception {
        // Logout the user
        tearDown();

        mockMvc.perform(get("/accounts/" + email + "/verify/" + RandomStringUtils.randomAlphabetic(50))
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Already verified email " + email)));

        mockMvc.perform(get("/accounts/random" + email + "/verify/" + RandomStringUtils.randomAlphabetic(50))
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isNotFound());
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
