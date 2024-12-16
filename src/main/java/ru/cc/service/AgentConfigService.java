package ru.cc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.cc.config.AgentConfig;

import java.io.File;
import java.io.IOException;

@Service
public class AgentConfigService {
    public AgentConfig loadConfig(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), AgentConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isAgentRegistered(AgentConfig  agent) {
        return agent.getAgentId() != null && !agent.getAgentId().isEmpty();
    }
}
