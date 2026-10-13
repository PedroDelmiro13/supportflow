package com.supportflow.config;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.supportflow.entity.Usuario;
import com.supportflow.repository.UsuarioRepository;


@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private UsuarioRepository usuarioRepository;
	private BCryptPasswordEncoder passwordEncoder;

	public AdminUserConfig(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional 
	public void run(String... args) throws Exception {
		String emailAdmin = "admin@email.com";

       Optional<Usuario> userAdmin = this.usuarioRepository.findByEmail("emailAdmin");
        
		
		userAdmin.ifPresentOrElse(user -> {
                    System.out.println("admin ja existe");

                },
                () -> {
                    Usuario usuario = new Usuario();

                    usuario.setNome("Admin");
                    usuario.setEmail(emailAdmin);
                    usuario.setSenhaHash(passwordEncoder.encode("123"));
                    usuarioRepository.save(usuario);
                }
        );

	}
}
