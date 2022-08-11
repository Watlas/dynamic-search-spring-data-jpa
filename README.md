# dynamic-search-spring-data-jpa

Example application of a dynamic search using URL parameters and a Specification builder.

## General Info

This project is about making dynamic queries according to some parameters passed by the URL, a great differential of
this project is the possibility of accessing compositions and searches by dates.

## Technologies

Project is created with: 2022-08-10

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
* [Java 11](https://openjdk.java.net/projects/jdk/11/)
* [H2 database](https://www.h2database.com/)
* [Lombok](https://projectlombok.org/)

## how to use:

As an example we will use an endpoint that is in the directory com.dynamic.search.jpa.example.controller:

```
     @GetMapping(produces = "application/json")
    public List<Address> listAddressByFilter(@RequestParam String search) {
        return addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, search));
    }
    
    @GetMapping(produces = "application/json")
    public Page<Address> listAddressByFilterAndPageable(@RequestParam String search, Pageable pageable) {
        return addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, search), pageable);
    }
    
```

Now just access some client to make HTTP requests and make a query:

```
     http://localhost:8080/address?search=id==1
     http://localhost:8080/address/page?search=state.id==3&page=0&size=10
```

You can pass more than one attribute using the semicolon (;) as the attribute separator:

```
    http://localhost:8080/address/page?search=state.id==3&page=0&size=10
    http://localhost:8080/address?search=state.id==1;&page=0&size=10
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
    http://localhost:8080/address?search=createdAt>=2022-06-06;createdAt<=2022-10-06
    http://localhost:8080/address/page?search=createdAt>=2022-06-06;createdAt<=2022-10-06&page=0&size=10
```

But you can go there yourself in the DateUtil Class and add its format.



```
getDynamicFormatLocalDateTime() 
```
## How to use:

Project is using H2 database (memory database), and it already has initial load when starting the application,
so just build the project and test.

```
    @Bean
    public void initDB() {
        Address address = new Address("Rua 1", new State("SP", new Country("Brazil")), new People("John"));
        addressRepository.save(address);
    }
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
  


## How to use the solution in another Spring project:

just add the directory com.dynamic.search.jpa.search in your project and call the class SpecificationBuilderSearch
passing the entity and the search, and pass the result of the builder to the repository.

```
addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, search));

Address - Entity
search - Search parameter
SpecificationBuilderSearch - Class responsible for creating the Specification to send the repository layer

```
