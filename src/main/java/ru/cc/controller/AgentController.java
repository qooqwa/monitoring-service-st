package ru.cc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cc.model.api.RegisterAgent;
import ru.cc.model.api.RespMain;

@RestController
public class AgentController {

    @PostMapping(value = "/monitor/api/сheckAgent")
    @Operation(summary = "Получение информации об агенте")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные об агенте успешно получены",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterAgent.class)) }),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RespMain.class)) }),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка обработки запроса",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RespMain.class)) })
    })
    public ResponseEntity<RespMain> GetAgentInfo() {
        return new ResponseEntity<>(new RespMain("SUCCESS", null, null), HttpStatus.OK);
    }

}
