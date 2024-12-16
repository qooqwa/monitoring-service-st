package ru.cc.service;

import org.springframework.stereotype.Service;
import ru.cc.config.SetOfWorkParameters;

@Service
public class UpdateService {
    private static final String PLUGINS_PATH = "C:\\AgentJson\\Plugins";
    public void handleUpdate(SetOfWorkParameters workParameters) {
        String url = "http://localhost:8099/monitor/api/getPluginList";

    }
}
