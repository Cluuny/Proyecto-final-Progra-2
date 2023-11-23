package dev.uptc.loginfx.model;

public class Person {

        private String id;
        private String firstName;
        private String lastName;
        private String country;

        public Person(String id, String firstName, String lastName, String country) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.country = country;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getFullName() {
            return firstName + " " + lastName;
        }
    }
