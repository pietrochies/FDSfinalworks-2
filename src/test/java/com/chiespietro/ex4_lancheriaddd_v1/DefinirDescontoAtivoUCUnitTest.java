package com.chiespietro.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chiespietro.ex4_lancheriaddd_v1.Aplicacao.DefinirDescontoAtivoUC;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.DescontoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;

class DefinirDescontoAtivoUCUnitTest {

    @Mock
    private DescontoRepository descontoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private DefinirDescontoAtivoUC definirDescontoAtivoUC;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        definirDescontoAtivoUC = new DefinirDescontoAtivoUC(descontoRepository, usuarioRepository);
    }

    // ==================== TESTES DE PERMISSÃO ====================

    @Test
    void testExecutar_UsuarioMaster_DescontoValido() {
        // Arrange
        long usuarioMasterId = 1L;
        Usuario usuarioMaster = new Usuario();
        usuarioMaster.setId(usuarioMasterId);
        usuarioMaster.setTipoUsuario("MASTER");

        Desconto descontoFrequente = new Desconto("ClienteFrequente", "Desconto frequente", 7.0);
        descontoFrequente.setAtivo(false);

        Desconto descontoGastador = new Desconto("ClienteGastador", "Desconto gastador", 15.0);
        descontoGastador.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioMasterId)).thenReturn(Optional.of(usuarioMaster));
        when(descontoRepository.findByCodigo("ClienteFrequente")).thenReturn(descontoFrequente);
        when(descontoRepository.findAll()).thenReturn(java.util.Arrays.asList(descontoFrequente, descontoGastador));

        // Act
        assertDoesNotThrow(() -> definirDescontoAtivoUC.executar(usuarioMasterId, "ClienteFrequente"));

        // Assert
        verify(descontoRepository, times(1)).findByCodigo("ClienteFrequente");
        verify(descontoRepository, times(1)).findAll();
        verify(descontoRepository, atLeastOnce()).save(any(Desconto.class));
        assertTrue(descontoFrequente.getAtivo());
    }

    @Test
    void testExecutar_UsuarioNaoMaster_LancaExcecao() {
        // Arrange
        long usuarioClienteId = 2L;
        Usuario usuarioCliente = new Usuario();
        usuarioCliente.setId(usuarioClienteId);
        usuarioCliente.setTipoUsuario("CLIENTE");

        when(usuarioRepository.buscarPorId(usuarioClienteId)).thenReturn(Optional.of(usuarioCliente));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            definirDescontoAtivoUC.executar(usuarioClienteId, "ClienteFrequente");
        });

        verify(descontoRepository, never()).findByCodigo(anyString());
    }

    @Test
    void testExecutar_UsuarioNaoEncontrado_LancaExcecao() {
        // Arrange
        long usuarioInexistenteId = 999L;
        when(usuarioRepository.buscarPorId(usuarioInexistenteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            definirDescontoAtivoUC.executar(usuarioInexistenteId, "ClienteFrequente");
        });

        verify(descontoRepository, never()).findByCodigo(anyString());
    }

    // ==================== TESTES DE ATIVAÇÃO/DESATIVAÇÃO ====================

    @Test
    void testExecutar_AtivaDescontoFrequente() {
        // Arrange
        long usuarioMasterId = 1L;
        Usuario usuarioMaster = new Usuario();
        usuarioMaster.setId(usuarioMasterId);
        usuarioMaster.setTipoUsuario("MASTER");

        Desconto descontoFrequente = new Desconto("ClienteFrequente", "Desconto frequente", 7.0);
        descontoFrequente.setAtivo(false);

        Desconto descontoGastador = new Desconto("ClienteGastador", "Desconto gastador", 15.0);
        descontoGastador.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioMasterId)).thenReturn(Optional.of(usuarioMaster));
        when(descontoRepository.findByCodigo("ClienteFrequente")).thenReturn(descontoFrequente);
        when(descontoRepository.findAll()).thenReturn(java.util.Arrays.asList(descontoFrequente, descontoGastador));

        // Act
        definirDescontoAtivoUC.executar(usuarioMasterId, "ClienteFrequente");

        // Assert
        assertTrue(descontoFrequente.getAtivo());
        assertFalse(descontoGastador.getAtivo());
    }

    @Test
    void testExecutar_AtivaDescontoGastador() {
        // Arrange
        long usuarioMasterId = 1L;
        Usuario usuarioMaster = new Usuario();
        usuarioMaster.setId(usuarioMasterId);
        usuarioMaster.setTipoUsuario("MASTER");

        Desconto descontoFrequente = new Desconto("ClienteFrequente", "Desconto frequente", 7.0);
        descontoFrequente.setAtivo(true);

        Desconto descontoGastador = new Desconto("ClienteGastador", "Desconto gastador", 15.0);
        descontoGastador.setAtivo(false);

        when(usuarioRepository.buscarPorId(usuarioMasterId)).thenReturn(Optional.of(usuarioMaster));
        when(descontoRepository.findByCodigo("ClienteGastador")).thenReturn(descontoGastador);
        when(descontoRepository.findAll()).thenReturn(java.util.Arrays.asList(descontoFrequente, descontoGastador));

        // Act
        definirDescontoAtivoUC.executar(usuarioMasterId, "ClienteGastador");

        // Assert
        assertFalse(descontoFrequente.getAtivo());
        assertTrue(descontoGastador.getAtivo());
    }

    @Test
    void testExecutar_DescontoNaoEncontrado_LancaExcecao() {
        // Arrange
        long usuarioMasterId = 1L;
        Usuario usuarioMaster = new Usuario();
        usuarioMaster.setId(usuarioMasterId);
        usuarioMaster.setTipoUsuario("MASTER");

        when(usuarioRepository.buscarPorId(usuarioMasterId)).thenReturn(Optional.of(usuarioMaster));
        when(descontoRepository.findByCodigo("DescontoInexistente")).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            definirDescontoAtivoUC.executar(usuarioMasterId, "DescontoInexistente");
        });

        verify(descontoRepository, never()).findAll();
    }

    // ==================== TESTES DE INATIVAÇÃO ====================

    @Test
    void testExecutar_InativaOutrosDescontos() {
        // Arrange
        long usuarioMasterId = 1L;
        Usuario usuarioMaster = new Usuario();
        usuarioMaster.setId(usuarioMasterId);
        usuarioMaster.setTipoUsuario("MASTER");

        Desconto descontoFrequente = new Desconto("ClienteFrequente", "Desconto frequente", 7.0);
        descontoFrequente.setAtivo(true);

        Desconto descontoGastador = new Desconto("ClienteGastador", "Desconto gastador", 15.0);
        descontoGastador.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioMasterId)).thenReturn(Optional.of(usuarioMaster));
        when(descontoRepository.findByCodigo("ClienteFrequente")).thenReturn(descontoFrequente);
        when(descontoRepository.findAll()).thenReturn(java.util.Arrays.asList(descontoFrequente, descontoGastador));

        // Act
        definirDescontoAtivoUC.executar(usuarioMasterId, "ClienteFrequente");

        // Assert
        assertTrue(descontoFrequente.getAtivo());
        assertFalse(descontoGastador.getAtivo());

        // Verifica que foram salvos (pode ser mais por causa da inativação)
        verify(descontoRepository, atLeastOnce()).save(any(Desconto.class));
    }

    @Test
    void testExecutar_ApenasUmDescontoAtivoSimultaneamente() {
        // Arrange
        long usuarioMasterId = 1L;
        Usuario usuarioMaster = new Usuario();
        usuarioMaster.setId(usuarioMasterId);
        usuarioMaster.setTipoUsuario("MASTER");

        Desconto descontoFrequente = new Desconto("ClienteFrequente", "Desconto frequente", 7.0);
        descontoFrequente.setAtivo(false);

        Desconto descontoGastador = new Desconto("ClienteGastador", "Desconto gastador", 15.0);
        descontoGastador.setAtivo(false);

        when(usuarioRepository.buscarPorId(usuarioMasterId)).thenReturn(Optional.of(usuarioMaster));
        when(descontoRepository.findByCodigo("ClienteGastador")).thenReturn(descontoGastador);
        when(descontoRepository.findAll()).thenReturn(java.util.Arrays.asList(descontoFrequente, descontoGastador));

        // Act
        definirDescontoAtivoUC.executar(usuarioMasterId, "ClienteGastador");

        // Assert
        assertFalse(descontoFrequente.getAtivo());
        assertTrue(descontoGastador.getAtivo());
    }

    @Test
    void testExecutar_AlteraDescontoAtivoParaOutro() {
        // Arrange
        long usuarioMasterId = 1L;
        Usuario usuarioMaster = new Usuario();
        usuarioMaster.setId(usuarioMasterId);
        usuarioMaster.setTipoUsuario("MASTER");

        Desconto descontoFrequente = new Desconto("ClienteFrequente", "Desconto frequente", 7.0);
        descontoFrequente.setAtivo(true);

        Desconto descontoGastador = new Desconto("ClienteGastador", "Desconto gastador", 15.0);
        descontoGastador.setAtivo(false);

        when(usuarioRepository.buscarPorId(usuarioMasterId)).thenReturn(Optional.of(usuarioMaster));
        when(descontoRepository.findByCodigo("ClienteGastador")).thenReturn(descontoGastador);
        when(descontoRepository.findAll()).thenReturn(java.util.Arrays.asList(descontoFrequente, descontoGastador));

        // Act
        definirDescontoAtivoUC.executar(usuarioMasterId, "ClienteGastador");

        // Assert
        // ClienteFrequente deve estar inativo
        assertFalse(descontoFrequente.getAtivo());
        // ClienteGastador deve estar ativo
        assertTrue(descontoGastador.getAtivo());

        // Verifica chamadas ao repository
        verify(descontoRepository, times(1)).findByCodigo("ClienteGastador");
        verify(descontoRepository, times(1)).findAll();
        verify(descontoRepository, atLeastOnce()).save(any(Desconto.class));
    }
}
