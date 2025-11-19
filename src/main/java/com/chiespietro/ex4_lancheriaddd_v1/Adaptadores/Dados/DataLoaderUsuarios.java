package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoaderUsuarios implements CommandLineRunner {
    
    private UsuarioRepositoryJDBC usuarioRepository;

    @Autowired
    public DataLoaderUsuarios(UsuarioRepositoryJDBC usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar se o usuário MASTER já existe
        if (usuarioRepository.buscarPorEmail("master@pizzaria.com").isEmpty()) {
            Usuario usuarioMaster = new Usuario(
                "master@pizzaria.com",
                "master123",
                "Administrador",
                Usuario.TipoUsuario.MASTER
            );
            usuarioRepository.save(usuarioMaster);
            System.out.println("✅ Usuário MASTER criado: master@pizzaria.com / master123");
        }
    }
}
