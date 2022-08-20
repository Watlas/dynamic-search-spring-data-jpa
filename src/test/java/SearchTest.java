import com.dynamic.search.jpa.DynamicSearchJpaApplication;
import com.dynamic.search.jpa.example.entity.Address;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SearchTest {

    @Autowired
    @Qualifier("testRestTemplate")
    private TestRestTemplate testRestTemplate;

    @Order(1)
    @DisplayName("Test Equals, using only one field and one operation")
    @Test
    void testEquals() {
        List<Address> response = testRestTemplate.exchange("/address?search=name==Rua 1", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(2)
    @Test
    @DisplayName("Test greater than, using only one field and one operation")
    void testGreatThan() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt>2022-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();
        Assertions.assertNotNull(response);

        log.info("great than test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(3)
    @Test
    @DisplayName("Test Less Than than equal, using only one field and one operation")
    void testLessThan() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt<2025-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();
        Assertions.assertNotNull(response);

        log.info("less than test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(4)
    @Test
    @DisplayName("Test Greater Than Equal, using only one field and one operation")
    void testGreaterThanEqual() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt>=2022-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();
        Assertions.assertNotNull(response);

        log.info("greater than equal test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(5)
    @Test
    @DisplayName("Test Less Than Equal, using only one field and one operation")
    void testLessThanEqual() {
        List<Address> response = testRestTemplate.exchange("/address?search=createdAt<=2025-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("less than equal test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(6)
    @Test
    @DisplayName("Test Not Equal, using only one field and one operation")
    void testNotEquals() {
        List<Address> response = testRestTemplate.exchange("/address?search=name!=Rua 2", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("not equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(7)
    @Test
    @DisplayName("Test Match, using only one field and one operation")
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

    @Order(8)
    @Test
    @DisplayName("Test Match Start, using only one field and one operation")
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

    @Order(9)
    @Test
    @DisplayName("Test Match End, using only one field and one operation")
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

    @Order(10)
    @Test
    @DisplayName("Test Equals, using only one field and one operation but using Composition")
    void testEqualsAccessingComposition() {
        List<Address> response = testRestTemplate.exchange("/address?search=state.name==SP", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(11)
    @Test
    @DisplayName("Test Not Equals, using only one field and one operation but using Composition")
    void testNotEqualsAccessingComposition() {
        List<Address> response = testRestTemplate.exchange("/address?search=state.name!=MA", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(12)
    @Test
    @DisplayName("Test using only multiples fields and one multiples operations and Composition 1")
    void testEqualsAccessingCompositionAndMultipleFields() {
        List<Address> response = testRestTemplate.exchange("/address?search=state.name==SP;state.country.name!=EUA", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

    @Order(13)
    @Test
    @DisplayName("Test using only multiples fields and one multiples operations and Composition 2")
    void testMultipleOperationsAndMultipleFields() {
        List<Address> response = testRestTemplate.exchange("/address?search=state.name==SP;state.country.name!=EUA;name==Rua 1;createdAt<=2025-08-01", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Address>>() {
                }).getBody();

        Assertions.assertNotNull(response);

        log.info("equals test response: {}", response.get(0).toString());

        Assertions.assertEquals(response.get(0).getName(), "Rua 1");
    }

}
