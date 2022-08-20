import com.dynamic.search.jpa.DynamicSearchJpaApplication;
import com.dynamic.search.jpa.example.entity.Address;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testGreatThan() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt>2022-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();
        Assertions.assertNotNull(response);

        log.info("great than test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testLessThan() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt<2025-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();
        Assertions.assertNotNull(response);

        log.info("less than test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testGreaterThanEqual() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt>=2022-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();
        Assertions.assertNotNull(response);

        log.info("greater than equal test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testLessThanEqual() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt<=2025-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("less than equal test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    void testNotEquals() {
        List<Address> response = testRestTemplate.exchange("/address?search=name!=Rua 2", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("not equals test response: {}", response.get(0).toString());

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

        Assertions.assertNotNull(response);

        log.info("match test response: {}", response.get(0).toString());

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

        Assertions.assertNotNull(response);

        log.info("match test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    @DisplayName("test match end")
    void testMatchEnd() {
        URI uri = UriComponentsBuilder
                .fromUriString("/address")
                .queryParam("search", "name&~1")
                .encode()
                .build().toUri();
        List<Address> response = testRestTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("match test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    @DisplayName("test equal using composition")
    void testEqualsAccessingComposition() {
        List<Address> response = testRestTemplate.exchange("/address?search=state.name==SP", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    @DisplayName("test is not equal using composition")
    void testNotEqualsAccessingComposition() {
        List<Address> response = testRestTemplate.exchange("/address?search=state.name!=MA", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    @DisplayName("test equal using composition and multiple fields")
    void testEqualsAccessingCompositionAndMultipleFields() {
        List<Address> response = testRestTemplate.exchange("/address?search=state.name==SP;state.country.name!=EUA", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Test
    @DisplayName("test with multiple operations and multiple fields)")
    void testMultipleOperationsAndMultipleFields() {
        List<Address> response = testRestTemplate.exchange("/address?search=state.name==SP;state.country.name!=EUA;name==Rua 1;createdAt<=2025-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

}
