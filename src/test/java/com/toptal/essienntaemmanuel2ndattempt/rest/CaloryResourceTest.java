package com.toptal.essienntaemmanuel2ndattempt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.essienntaemmanuel2ndattempt.dto.CaloryDto;
import com.toptal.essienntaemmanuel2ndattempt.service.api.MailSender;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import javax.servlet.http.HttpSession;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author bodmas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CaloryResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailSender mailSender;

    private static final SessionHolder sessionHolder = new SessionHolder();

    private static final String email = "test2@toptal.com";
    private static int count;

    public CaloryResourceTest() {
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

            // Add default foods and verify that they're created.
            String[] defaultFoods = {"sugar", "corn", "rice"};
            Arrays.asList(defaultFoods).stream().forEach(food -> {
                CaloryDto caloryDto = new CaloryDto();
                caloryDto.setDate(LocalDate.now());
                caloryDto.setTime(LocalTime.now());
                caloryDto.setFood(food);
                try {
                    MvcResult result = mockMvc.perform(post("/calories")
                            .content(asJsonString(caloryDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .session(sessionHolder.getSession())
                            .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                            .andExpect(status().isCreated())
                            .andReturn();

                    String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
                    assertThat(location).isNotNull();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            mockMvc.perform(get("/calories")
                    .session(sessionHolder.getSession())
                    .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].food", hasSize(defaultFoods.length)))
                    .andExpect(jsonPath("$[*].food", contains(defaultFoods)));
        }
    }

    @Test
    public void testFindOne() throws Exception {
        CaloryDto caloryDto = new CaloryDto();
        caloryDto.setDate(LocalDate.now());
        caloryDto.setTime(LocalTime.now());
        caloryDto.setFood("milk");

        final MvcResult result = mockMvc.perform(post("/calories")
                .content(asJsonString(caloryDto))
                .contentType(MediaType.APPLICATION_JSON)
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);

        assertThat(location).isNotNull();
        System.out.println("location = " + location);

        final Path path = Paths.get(location);
        String caloryId = path.getName(path.getNameCount() - 1).toString();

        mockMvc.perform(get("/calories/" + caloryId)
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.food", equalTo("milk")));

        mockMvc.perform(get("/calories/" + 1000000)
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindAll_3args_1() throws Exception {
        for (int size = 1; size <= 3; size++) {
            mockMvc.perform(get("/calories")
                    .param("page", "0")
                    .param("size", String.valueOf(size))
                    .session(sessionHolder.getSession())
                    .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].food", hasSize(size)));
        }
    }

    @Test
    public void testFindAll_Principal_String() throws Exception {
        mockMvc.perform(get("/calories")
                .param("q", "(food eq 'corn') or (food eq 'rice')")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].food", hasSize(2)));

        mockMvc.perform(get("/calories")
                .param("q", "food eq 'corn'")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].food", hasSize(1)));

        mockMvc.perform(get("/calories")
                .param("q", "(food eq 'corn') and (((date eq '2017-09-06') or time eq '20:10:15') or num_calories gt 15)")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk());

        mockMvc.perform(get("/calories")
                .param("q", "(food eq 'corn') and (((date eq '2017-09-06) or time eq '20:10:15') or num_calories gt 15)")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Malformed query. Your request was not understood.\",\"status\":\"fail\"}"));
    }

    @Test
    public void testFindAll_4args_1() throws Exception {
        mockMvc.perform(get("/calories")
                .param("q", "(food eq 'corn') or (food eq 'rice')")
                .param("page", "0")
                .param("size", "1")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].food", hasSize(1)));

        mockMvc.perform(get("/calories")
                .param("q", "food eq 'corn' or food eq 'rice'")
                .param("page", "0")
                .param("size", "1")
                .session(sessionHolder.getSession())
                .accept(MediaType.APPLICATION_JSON, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].food", hasSize(1)));
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
