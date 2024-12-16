package ru.cc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cc.config.AgentConfig;
import ru.cc.config.SetOfWorkParameters;


import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ReadyService {
    private static final long HEALTH_CHECK_INTERVAL = 30 * 1000;
    private final RestTemplate restTemplate;
    private AgentConfig agentConfig;
    private final SnapshotService snapshotService;
    private final ObjectMapper objectMapper;
    private final AgentConfigService agentConfigService;
    private final UpdateService updateService;

    public ReadyService(RestTemplate restTemplate, SnapshotService snapshotService, ObjectMapper objectMapper, AgentConfigService agentConfigService, UpdateService updateService) {
        this.restTemplate = restTemplate;
        this.agentConfigService = agentConfigService;
        this.snapshotService = snapshotService;
        this.objectMapper = objectMapper;
        this.updateService = updateService;
    }


    @PostConstruct
    public void init(){
        startReadySignal();
    }
    public void startReadySignal() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                agentConfig = new AgentConfigService().loadConfig("C:\\AgentJson\\conf.json");
                if(agentConfigService.isAgentRegistered(agentConfig)){
                    sendReadyRequest(agentConfig.getAgentNick(), agentConfig.getAgentId());
                }
            }
        }, 0, HEALTH_CHECK_INTERVAL);
    }

    public void sendReadyRequest(String agentNick, String agentId) {
        if(agentConfig == null){
            System.out.println("No agent config loaded");
            return;
        }
        String url = "http://localhost:8099/monitor/api/ready";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("agentNick", agentNick)
                .queryParam("agentId", agentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);
        try {
            SetOfWorkParameters workParameters = objectMapper.readValue(response.getBody(), SetOfWorkParameters.class);
            handleWorkParameters(workParameters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Ready request was sent successfully");
            System.out.println(response.getBody());
        } else {
            System.out.println("Failed to send ready request");
        }
    }
    private void handleWorkParameters(SetOfWorkParameters workParameters) {
        String paramName = workParameters.getParamName();
        switch (paramName) {
            case "SNAPSHOT":
                if(!Objects.equals(agentConfig.getSnapshotNum(), workParameters.getParamVal())){
                    snapshotService.handleSnapshot(workParameters);
                }
                break;
            case "UPDATE":
                if(!Objects.equals(agentConfig.getUpdateNum(), workParameters.getParamVal())){
                    updateService.handleUpdate(workParameters);
                }
            default:
                System.out.println("Unknown work parameter: " + paramName);
                break;
        }
    }
}
