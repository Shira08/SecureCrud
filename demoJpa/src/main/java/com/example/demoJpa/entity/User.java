    package com.example.demoJpa.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "app_user")
    public class User implements UserDetails {


            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Integer id;
            private String username;
            private String password;




     /*   @OneToOne(mappedBy = "user")
        private Employee employee;*/

        @ManyToOne
        @JoinColumn(name = "role_id")
        private Role role;


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return role.getAuthorities();
        }

            @Override
            public String getPassword() {
                return password;
            }

        public Integer getId() {
            return id;
        }

        @Override
            public String getUsername() {
                return username;
            }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }


        public Role getRole() {
            return this.role = role;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setRole(Role role) {
            this.role = role;
        }


    }
