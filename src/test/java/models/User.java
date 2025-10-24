package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Модель данных для пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("website")
    private String website;
    
    @JsonProperty("address")
    private Address address;
    
    @JsonProperty("company")
    private Company company;
    
    /**
     * Вложенный класс для адреса
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Address {
        @JsonProperty("street")
        private String street;
        
        @JsonProperty("suite")
        private String suite;
        
        @JsonProperty("city")
        private String city;
        
        @JsonProperty("zipcode")
        private String zipcode;
        
        @JsonProperty("geo")
        private Geo geo;
    }
    
    /**
     * Вложенный класс для геолокации
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Geo {
        @JsonProperty("lat")
        private String lat;
        
        @JsonProperty("lng")
        private String lng;
    }
    
    /**
     * Вложенный класс для компании
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Company {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("catchPhrase")
        private String catchPhrase;
        
        @JsonProperty("bs")
        private String bs;
    }
    
    /**
     * Создать тестового пользователя
     */
    public static User createTestUser() {
        return User.builder()
                .name("Test User")
                .username("testuser")
                .email("test@example.com")
                .phone("123-456-7890")
                .website("test.com")
                .address(Address.builder()
                        .street("Test Street")
                        .suite("Suite 1")
                        .city("Test City")
                        .zipcode("12345")
                        .geo(Geo.builder()
                                .lat("40.7128")
                                .lng("-74.0060")
                                .build())
                        .build())
                .company(Company.builder()
                        .name("Test Company")
                        .catchPhrase("Test Catch Phrase")
                        .bs("Test BS")
                        .build())
                .build();
    }
}
