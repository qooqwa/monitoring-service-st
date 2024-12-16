package ru.cc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public class AgentConfig {
    private String agentNick;
    private String agentId;
    private String groupNick;
    private String agentNote;
    private String mainHash;
    private List<String> dependencyTags;
    private String snapshotNum;
    private String updateNum;

    private static final String CONFIG_FILE_PATH = "C:\\AgentJson\\conf.json";

    public void setAgentId(String agentId) {
        this.agentId = agentId;
        saveConfig();
    }

    public void setSnapshotNum(String snapshotNum) {
        this.snapshotNum = snapshotNum;
        saveConfig();
    }

    public void setUpdateNum(String updateNum) {
        this.updateNum = updateNum;
        saveConfig();
    }

    public void setMainHash(String mainHash) {
        this.mainHash = mainHash;
        saveConfig();
    }
    public void setDependencyTags(List<String> dependencyTags) {
        this.dependencyTags = dependencyTags;
        saveConfig();
    }

    public void saveConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(CONFIG_FILE_PATH), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
