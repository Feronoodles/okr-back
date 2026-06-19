package com.example.okr;

import com.example.okr.entities.KeyResult;
import com.example.okr.entities.User;
import com.example.okr.persistence.KeyResultRepository;
import com.example.okr.persistence.RefreshTokenRepository;
import com.example.okr.persistence.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthAndSecurityIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeyResultRepository keyResultRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
        keyResultRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void loginReturnsAccessAndRefreshTokens() throws Exception {
        createUser("alice", "secret123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"alice\",\"password\":\"secret123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void refreshReturnsNewSessionTokens() throws Exception {
        createUser("alice", "secret123");
        JsonNode loginResponse = login("alice", "secret123");

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshPayload(loginResponse.get("refreshToken").asText())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").value(org.hamcrest.Matchers.not(loginResponse.get("refreshToken").asText())));
    }

    @Test
    void logoutRevokesCurrentRefreshToken() throws Exception {
        createUser("alice", "secret123");
        JsonNode loginResponse = login("alice", "secret123");

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + loginResponse.get("accessToken").asText())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshPayload(loginResponse.get("refreshToken").asText())))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshPayload(loginResponse.get("refreshToken").asText())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void publicKeyResultsEndpointReturnsOnlyActiveRecords() throws Exception {
        User user = createUser("alice", "secret123");
        keyResultRepository.save(buildKeyResult(user, "Visible KR", false));
        keyResultRepository.save(buildKeyResult(user, "Archived KR", true));

        mockMvc.perform(get("/key_result/view_key_results"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].description").value("Visible KR"));
    }

    @Test
    void createKeyResultRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/key_result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"description\":\"KR\",\"percent\":50,\"quarter\":\"Q1\",\"owner\":\"Team\",\"iniciativa\":\"Init\",\"pilar\":\"Growth\",\"area\":\"Tech\",\"objective\":\"Ship\",\"objective_percent\":70}]") )
                .andExpect(status().isForbidden());
    }

    private User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    private KeyResult buildKeyResult(User user, String description, boolean archived) {
        KeyResult keyResult = new KeyResult();
        keyResult.setDescription(description);
        keyResult.setPercent(80.0);
        keyResult.setQuarter("Q1");
        keyResult.setOwner("Alice");
        keyResult.setIniciativa("Initiative");
        keyResult.setPilar("Growth");
        keyResult.setArea("Engineering");
        keyResult.setObjective("Improve delivery");
        keyResult.setObjective_percent(90.0);
        keyResult.setArchived(archived);
        keyResult.setArchivedAt(archived ? new Date() : null);
        keyResult.setUser(user);
        return keyResult;
    }

    private JsonNode login(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private String refreshPayload(String refreshToken) throws Exception {
        return objectMapper.writeValueAsString(objectMapper.createObjectNode().put("refreshToken", refreshToken));
    }
}
