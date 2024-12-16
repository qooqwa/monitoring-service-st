package ru.cc.service;

import org.springframework.context.ApplicationEvent;

public class AgentRegisteredEvent extends ApplicationEvent {
    public AgentRegisteredEvent(Object source) {
        super(source);
    }
}
