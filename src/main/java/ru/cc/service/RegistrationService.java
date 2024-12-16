package ru.cc.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.cc.config.AgentConfig;
import ru.cc.model.api.RegisterAgent;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class RegistrationService {
    private static final String CONFIG_FILE_PATH = "C:\\AgentJson\\conf.json";
    private final RestTemplate restTemplate;
    private final AgentConfigService configService;


    public RegistrationService(RestTemplate restTemplate, AgentConfigService configService) {
        this.restTemplate = restTemplate;
        this.configService = configService;
    }

    @Scheduled (initialDelay = 5 * 1000, fixedRate = 30 * 1000)
    public void checkAndRegisterAgent() {
        try {
            AgentConfig agentConfig = configService.loadConfig(CONFIG_FILE_PATH);
            if (agentConfig == null) {
                System.out.println("No config file found");
                throw new IOException("Failed to load config file: " + CONFIG_FILE_PATH);
            }
            if (agentConfig.getAgentId() == null || agentConfig.getAgentId().isEmpty()) {
                agentConfig.setAgentId(UUID.randomUUID().toString());
                String newHash = calculateSHA256Base64(agentConfig);
                agentConfig.setMainHash(newHash);
                sendRegistrationRequest(agentConfig);
            } else {
                String newHash = calculateSHA256Base64(agentConfig);
                if (!newHash.equals(agentConfig.getMainHash())) {
                    System.out.println("Config file changed, updating registration...");
                    agentConfig.setMainHash(newHash);
                    sendRegistrationRequest(agentConfig);
                }
            }
        } catch (IOException e) {
            System.out.println("No config file found");
            e.printStackTrace();
        }
    }

    private void sendRegistrationRequest(AgentConfig agentConfig) {
        String url = "http://localhost:8099/monitor/api/regAgent";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RegisterAgent registerAgent = new RegisterAgent();
        registerAgent.setAgentId(agentConfig.getAgentId());
        registerAgent.setMainHash(agentConfig.getMainHash());
        registerAgent.setAgentNick(agentConfig.getAgentNick());
        registerAgent.setAgentNote(agentConfig.getAgentNote());
        registerAgent.setDependencyTags(agentConfig.getDependencyTags());
        registerAgent.setGroupNick(agentConfig.getGroupNick());
        HttpEntity<RegisterAgent> entity = new HttpEntity<>(registerAgent, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Agent registered successfully.");
        } else {
            System.out.println("Failed to register agent: " + response.getStatusCode());
        }
    }

    private static String calculateSHA256Base64(AgentConfig agentConfig) {
        String dataToHash = agentConfig.getAgentNick() +
                agentConfig.getGroupNick() +
                agentConfig.getAgentNote() +
                agentConfig.getDependencyTags().toString();
        byte[] hash = DigestUtils.sha256(dataToHash);
        return Base64.getEncoder().encodeToString(hash);
    }
}
