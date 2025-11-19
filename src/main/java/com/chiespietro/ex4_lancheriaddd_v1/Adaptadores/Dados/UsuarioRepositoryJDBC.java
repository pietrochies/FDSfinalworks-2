package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepositoryJDBC extends JpaRepository<Usuario, Long>, UsuarioRepository {
    
    @Override
    default Usuario salvar(Usuario usuario) {
        return save(usuario);
    }
    
    @Override
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(?1)")
    Optional<Usuario> buscarPorEmail(String email);
    
    @Override
    default Optional<Usuario> buscarPorId(long id) {
        return findById(id);
    }
}
