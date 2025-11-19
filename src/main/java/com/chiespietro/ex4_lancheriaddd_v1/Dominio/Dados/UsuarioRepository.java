package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    Optional<Usuario> buscarPorId(long id);
}
