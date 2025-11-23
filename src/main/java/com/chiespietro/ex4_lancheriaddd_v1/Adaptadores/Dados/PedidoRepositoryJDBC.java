package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Repository
public interface PedidoRepositoryJDBC extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.status = :status AND p.dataHoraPagamento BETWEEN :inicio AND :fim")
    List<Pedido> findByStatusAndDataEntregaBetween(
            @Param("status") Pedido.Status status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
}
