    package com.example.demoJpa.entity;

    import com.example.demoJpa.entity.Permission;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;

    import java.util.List;
    import java.util.Set;
    import java.util.stream.Collectors;

    public enum Role {
      /*  USER(
                Set.of(
                        Permission.USER_READ
                )
        ),
        ADMIN(
                Set.of(
                        Permission.ADMIN_READ,
                        Permission.ADMIN_UPDATE,
                        Permission.ADMIN_CREATE,
                        Permission.MANAGER_READ,
                        Permission.MANAGER_DELETE)

        ),
          USER(
              Set.of(
                      Permission.ADMIN_READ,
                      Permission.ADMIN_UPDATE,
                      Permission.ADMIN_CREATE,
                      Permission.MANAGER_READ,
                      Permission.MANAGER_DELETE)
      ),*/

        MANAGER(
                Set.of(
                        //Permission.MANAGER_READ,
                        //Permission.MANAGER_DELETE,
                        Permission.EMPLOYEE_CREATE,
                        Permission.EMPLOYEE_READ,
                        Permission.EMPLOYEE_DELETE,
                        Permission.MATERIAL_DELETE
                )),
        EMPLOYEE(
                Set.of(
                        Permission.MATERIAL_READ,
                        Permission.MATERIAL_CREATE,
                        Permission.MATERIAL_UPDATE,
                        Permission.EMPLOYEE_UPDATE
                ));

        private final Set<Permission> permissions;

        Role(Set<Permission> permissions) {
            this.permissions = permissions;
        }


        public Set<Permission> getPermissions() {
            return permissions;
        }

       /* public List<SimpleGrantedAuthority> getAuthorities() {
            var authorities = getPermissions()
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.toString()))
                    .collect(Collectors.toList());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + this.toString()));
            return authorities;

        }*/
       public List<SimpleGrantedAuthority> getAuthorities() {
           return getPermissions()
                   .stream()
                   .map(permission -> new SimpleGrantedAuthority(permission.toString()))
                   .collect(Collectors.toList());
       }

    }
