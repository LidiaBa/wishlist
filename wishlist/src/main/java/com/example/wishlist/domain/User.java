package com.example.wishlist.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String name;

    private String roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = roles != null ? roles : "USER";
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    @JsonIgnore
    public String getUsername() { return login; } // Spring использует login как username

}