package models;

import lombok.Builder;
import lombok.Data;

/**
 * Модель для пользователя
 */
@Data
@Builder
public class User {
    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;
    private Company company;

    @Data
    @Builder
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        @Data
        @Builder
        public static class Geo {
            private String lat;
            private String lng;
        }
    }

    @Data
    @Builder
    public static class Company {
        private String name;
        private String catchPhrase;
        private String bs;
    }
}