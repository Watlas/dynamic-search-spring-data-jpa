import com.dynamic.search.jpa.DynamicSearchJpaApplication;
import com.dynamic.search.jpa.example.entity.Address;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {DynamicSearchJpaApplication.class, Config.class})
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class SearchTest {

    @Autowired
    @Qualifier("testRestTemplate")
    private TestRestTemplate testRestTemplate;

    @Test
    void testEquals() {
        List<Address> response = testRestTemplate.exchange("/address?search=name==Rua 1", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("equals test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testGreatThan() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt>2022-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("great than test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testLessThan() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt<2025-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("less than test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testGreaterThanEqual() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt>=2022-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("greater than equal test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testLessThanEqual() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt<=2025-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("less than equal test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testNotEquals() {
        List<Address> response = testRestTemplate.exchange("/address?search=name!=Rua 2", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("not equals test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testMatch() {

        URI uri = UriComponentsBuilder
                .fromUriString("/address")
                .queryParam("search", "name&&rua")
                .encode()
                .build().toUri();
        List<Address> response = testRestTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("match test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testMatchStart() {
        URI uri = UriComponentsBuilder
                .fromUriString("/address")
                .queryParam("search", "name~&r")
                .encode()
                .build().toUri();
        List<Address> response = testRestTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("match test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testMatchEnd() {
        URI uri = UriComponentsBuilder
                .fromUriString("/address")
                .queryParam("search", "name&~1")
                .encode()
                .build().toUri();
        List<Address> response = testRestTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        log.info("match test response: {}", response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

}
