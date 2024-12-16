package ru.cc.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetOfWorkParameters {
    @JacksonXmlProperty(localName = "paramName")
    private String paramName;

    @JacksonXmlProperty(localName = "paramVal")
    private String paramVal;
}
