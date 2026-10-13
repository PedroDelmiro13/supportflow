
package com.supportflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.supportflow.enums.UserRole;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 2, max = 150)
    @Column(name = "name", nullable = false, length = 150)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail informado é inválido.")
    @Size(max = 150)
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(max = 255)
    @Column(name = "password_hash", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private UserRole role;

    @PrePersist
    protected void aoCriar() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }

        if (email != null) {
            email = email.trim().toLowerCase(java.util.Locale.ROOT);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public @Nullable String getPassword() {
        return this.senhaHash;
    }

    @Override
    public String getUsername() {
        return nome;
    }
}
