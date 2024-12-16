package ru.cc.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Данные регистрации агента в мониторе
 */
@Data
@Schema(name = "RegisterAgent", description = "Данные регистрации агента в мониторе")
public class RegisterAgent {
    /**
     * Уникальный ник агента
     */
    @Schema(description = "Уникальный ник агента", example = "AGENT_TEST_NGINX_1")
    @JsonProperty("agentNick")
    private String agentNick;

    /**
     * Уникальный идентификатор агента
     */
    @Schema(description = "Уникальный идентификатор агента", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    @JsonProperty("agentId")
    private String agentId;

    /**
     * Ник группы инфраструктуры
     */
    @Schema(description = "Ник группы инфраструктуры", example = "TEST_INFA")
    @JsonProperty("groupNick")
    private String groupNick;

    /**
     * Описание хоста развертывания агента
     */
    @Schema(description = "Описание агента", example = "Хост проксирования NGINX")
    @JsonProperty("agentNote")
    private String agentNote;

    /**
     * Хэш конфигурации агента
     */
    @Schema(description = "Хэш конфигурации агента", example = "fsaf12iooiaw")
    @JsonProperty("mainHash")
    private String mainHash;

    /**
     * Теги зависимостей
     */
    @Schema(description = "Теги зависимостей", example = "[\"UBUNTU\"")
    @JsonProperty("dependencyTags")
    private List<String> dependencyTags;

    /**
     * Номер последнего снэпшота
     */
    @Schema(description = "Номер последнего оснэпшота", example = "1")
    @JsonProperty("snapshotNum")
    private String snapshotNum;

    /**
     * Номер последнего обновления
     */
    @Schema(description = "Номер последнего обновления", example = "1")
    @JsonProperty("updateNum")
    private String updateNum;
}
