package ru.cc.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Стандартный набор полей ответа. Есть в любом ответе.
 */
@Data
@Schema(name = "respMain", description = "Стандартный набор полей ответа. Есть в любом ответе")
@AllArgsConstructor
public class RespMain {
    /**
     * Код ошибки. См. SedoErrorEnum
     */
    @Schema(description = "Код ошибки.", example = "SUCCESS")
    protected String errCode;

    /**
     * Сообщение об ошибке. Формируется в месте возникновения ошибки и описывает ее.
     */
    @Schema(description = "Сообщение об ошибке. Формируется в месте возникновения ошибки и описывает ее", example = "Описание ошибки тут.")
    protected String errMsg;

    /**
     * Источник сообщения об ошибке.
     */
    @Schema(description = "Источник сообщения об ошибке", example = "INFRA_MOONITOR")
    protected String errSrc;
}
