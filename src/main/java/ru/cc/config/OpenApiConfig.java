package ru.cc.config;

import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.Objects;

@SpringBootConfiguration
public class OpenApiConfig {

    // Описание OpenAPI
	@Bean
	public OpenAPI openAPI() {

		final String securitySchemeName = "bearerAuth";
		final String apiTitle = "Агент хоста для сбора конфигурации инфраструктуры";
		final String apiDescription = "API сервиса агента";
		final String apiVersion = componentVersion();

		return new OpenAPI()
				.addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList(securitySchemeName))
				.components(
						new Components()
								.addSecuritySchemes(securitySchemeName,
										new SecurityScheme()
												.name(securitySchemeName)
												.type(SecurityScheme.Type.HTTP)
												.scheme("bearer")
												.bearerFormat("JWT")
												.description("Авторизация по JWT")
								)
				)
				.info(new Info().title(apiTitle).version(apiVersion).description(apiDescription));
	}

	@SuppressWarnings("deprecation")
	private String componentVersion() {
		String propertiesFileName = "pom.xml";
		String componentVersion = "Версия не указана";
		File file = new File(propertiesFileName);
		XmlMapper xmlMapper = new XmlMapper();
		try {
			JsonSchema jsonSchema = xmlMapper.generateJsonSchema(String.class);
			JsonSchema json = xmlMapper.readValue(file, jsonSchema.getClass());
			componentVersion = Objects.nonNull(json.getSchemaNode().get("version"))
					? String.valueOf(json.getSchemaNode().get("version")).replaceAll("\"", "")
					: componentVersion;
		} catch (Exception e) {
			return componentVersion;
		}
		return componentVersion;
	}

}
