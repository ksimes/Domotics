package com.stronans.domotics.services.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapperService {
    private final ObjectMapper mapper = new ObjectMapper();

    public ObjectMapper getMapper() {
        return mapper;
    }
}
