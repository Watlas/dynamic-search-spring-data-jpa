import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.MethodDescriptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class Config {


    @Bean(name = "testRestTemplate")
    @Primary
    public TestRestTemplate testRestTemplateRoleUserCreator() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + 8080 + "").defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json");
        return new TestRestTemplate(restTemplateBuilder);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }


    static class MethodOrderCustom implements MethodOrderer {
        private static int compare(MethodDescriptor m1, MethodDescriptor m2) {
            Integer integer1 = Integer.decode(m2.getDisplayName().split("-")[0].trim());
            Integer integer2 = Integer.decode(m1.getDisplayName().split("-")[0].trim());
            int i = integer1.compareTo(integer2);

            if (i != 0) return -i;  // reverse sort

            return integer1.compareTo(integer2);
        }

        @Override
        public void orderMethods(MethodOrdererContext context) {
            context.getMethodDescriptors().sort(MethodOrderCustom::
                    compare);
        }
    }
}
