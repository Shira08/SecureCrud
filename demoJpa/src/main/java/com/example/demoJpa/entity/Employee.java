    package com.example.demoJpa.entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    @Entity
    @Getter
    @Setter
    @Table(name = "employee")
    public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        public Integer getSalary() {
            return salary;
        }

        @Column(name="salary")
        private Integer salary;
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private User user;
        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSalary(Integer salary) {
            this.salary = salary;
        }


        public String getName() {
            return name;
        }
    }
