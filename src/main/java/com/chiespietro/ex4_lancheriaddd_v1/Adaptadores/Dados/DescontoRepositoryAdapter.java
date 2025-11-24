package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.DescontoRepository;

@Component
public class DescontoRepositoryAdapter implements DescontoRepository {

    private final DescontoRepositoryJDBC descontoRepositoryJDBC;

    @Autowired
    public DescontoRepositoryAdapter(DescontoRepositoryJDBC descontoRepositoryJDBC) {
        this.descontoRepositoryJDBC = descontoRepositoryJDBC;
    }

    @Override
    public Desconto findByCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        return descontoRepositoryJDBC.findByCodigoIgnoreCase(codigo);
    }

    @Override
    public Desconto findById(Long id) {
        if (id == null) {
            return null;
        }
        return descontoRepositoryJDBC.findById(id).orElse(null);
    }

    @Override
    public void save(Desconto desconto) {
        if (desconto != null) {
            descontoRepositoryJDBC.save(desconto);
        }
    }

    @Override
    public List<Desconto> findAll() {
        return descontoRepositoryJDBC.findAll();
    }
}
