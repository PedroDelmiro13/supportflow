
package com.supportflow.service;


import com.supportflow.dto.CadastroUsuarioRequest;
import com.supportflow.entity.Usuario;
import com.supportflow.exception.EmailJaCadastradoException;
import com.supportflow.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Usuario cadastrar(CadastroUsuarioRequest request) {
        String email = request.email().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException(
                    "Já existe um usuário cadastrado com este e-mail."
            );
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));

        return usuarioRepository.save(usuario);
    }
}
