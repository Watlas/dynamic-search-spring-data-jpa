# dynamic-search-spring-data-jpa

Example application of a dynamic search using URL parameters and a Predicate builder.

## General Info

This project consists of making dynamic queries according to HTTP GET or POST requests, each one with a different type
of implementation, a great differential of
this project is the possibility to access compositions and searches by dates.

## Technologies

Project is created with: 2022-08-10

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
* [Java 11](https://openjdk.java.net/projects/jdk/11/)
* [H2 database](https://www.h2database.com/)
* [Lombok](https://projectlombok.org/)

## how to use:

Project is using H2 database (memory database), and it already has initial load when starting the application,
so just build the project and test.

```
    @Bean
    public void initDB() {
        Address address = new Address("Rua 1", new State("SP", new Country("Brazil")), new People("John"));
        addressRepository.save(address);
    }
```

As an example we will use an endpoint that is in the directory com.dynamic.search.jpa.example.controller:

```
@RequestMapping("/address")
@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;

    @GetMapping(produces = "application/json")
    public List<Address> listAddressByFilter(String search) {
        return addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, search));
    }

    @PostMapping(produces = "application/json")
    public List<Address> listAddressByFilterPost(@RequestBody JsonNode jsonNode) {
        return addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, jsonNode));
    }
    
}

```

### Examples using GET

Now just access some client to make HTTP requests and make a query:

```
  curl --location --request GET  'http://localhost:8080/address?search=id==1'
```

You can pass more than one attribute using the semicolon (;) as the attribute separator:

```
   curl --location --request GET  'http://localhost:8080/address/page?search=state.id==3;name==Rua 1'
```

You can use dates to query, enter dates, etc.

formats accepted for now:

* yyyy/MM/dd HH:mm:ss.SSSSSS
* yyyy-MM-dd HH:mm:ss
* ddMMMyyyy:HH:mm:ss.SSS
* yyyy-MM-dd HH:mm
* yyyy-MM-dd
* yyyy/MM/dd HH:mm
* yyyy/MM/dd

```
    curl --location --request GET 'http://localhost:8080/address?search=createdAt>=2022-06-06;createdAt<=2030-10-06'
```

But you can go there yourself in the DateCreate Class and add its format.

```
getDynamicFormatLocalDateTime() 
```

These are the operators supported by dynamic search.

| Operator Name        | Operator |
| -------------------- |:--------:|
| `GREATER THAN`       |    >     |
| `LESS THAN`          |    <     |
| `GREATER THAN EQUAL` |    >=    |
| `LESS THAN EQUAL`    |    <=    |
| `NOT EQUAL`          |    !=    |
| `EQUAL`              |    ==    |
| `MATCH`              |    &&    |
| `MATCH START`        |    ~&    |
| `MATCH END`          |    &~    |

### Examples using POST

Simple request with a single field

```
curl --location --request POST 'http://localhost:8080/address' \
--header 'Content-Type: application/json' \
--data-raw '[
  { 
    "fieldName": "name",
    "operationType": "==",
    "value": "Rua 1"
  }
]'

```

Simple request with a single field and accessing composition

```
curl --location --request POST 'http://localhost:8080/address' \
--header 'Content-Type: application/json' \
--data-raw '[
  {
    "fieldName": "state.name",
    "operationType": "==",
    "value": "SP"
  }
]'
```

Complex request with multiple fields and multiple compositions

```
curl --location --request POST 'http://localhost:8080/address' \
--header 'Content-Type: application/json' \
--data-raw '[
  {
    "fieldName": "state.name",
    "operationType": "==",
    "value": "SP"
  },
  {
    "fieldName": "state.country.name",
    "operationType": "!=",
    "value": "EUA"
  },
  {
    "fieldName": "createdAt",
    "operationType": "<=",
    "value": "2030-08-01"
  },
  {
    "fieldName": "name",
    "operationType": "==",
    "value": "Rua 1"
  }
]
'
```

You can see examples of all operations supported in the integration tests found in the SearchGetTest class.

```
mvn clean test
```

## How to use the solution in another Spring project:

just add the directory com.dynamic.search.jpa.search in your project and call the class SpecificationBuilderSearch
passing the entity and the search, and pass the result of the builder to the repository.

## Author:

* [Watlas](https://www.linkedin.com/in/watlas-rick-371392181/)

## More detailed explanation of the solution

* [Medium](https://medium.com/@watlas/9df4ebd80d22)
