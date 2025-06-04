package demo.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProtocolConfig {

    private String hospitalName = "Клиники имени а";

    private String chiefDoctorName = "Иванов Иван";

    public Map<String, Object> getProtocolProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hospitalName", hospitalName);
        properties.put("chiefDoctorName", chiefDoctorName);
        return properties;
    }
}