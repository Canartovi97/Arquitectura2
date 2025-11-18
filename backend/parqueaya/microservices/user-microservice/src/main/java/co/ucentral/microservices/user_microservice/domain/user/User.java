package co.ucentral.microservices.user_microservice.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "APP_USERS")
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String lastname;

    @Column(nullable = false,unique = true)
    private String email;
    private Integer age;
    private String  phone;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
