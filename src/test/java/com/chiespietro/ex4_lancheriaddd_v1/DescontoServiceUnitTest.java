package com.chiespietro.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.DescontoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoService;

class DescontoServiceUnitTest {

    @Mock
    private DescontoRepository descontoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    private DescontoService descontoService;
    private Cliente clienteTeste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        descontoService = new DescontoService(descontoRepository, pedidoRepository);
        clienteTeste = new Cliente("9001", "Cliente Teste", "51985744566", "Rua Teste", "teste@email.com");
    }

    // ==================== TESTES PARA CLIENTE FREQUENTE ====================

    @Test
    void testClienteFrequente_ComMaisDeTresPedidosEmVinteDias() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime vinteDiasAtras = agora.minusDays(20);
        
        // Cria 4 pedidos pagos nos últimos 20 dias
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Pedido pedido = new Pedido(
                (long) i,
                clienteTeste,
                vinteDiasAtras.plusDays(i),
                new ArrayList<>(),
                Pedido.Status.PAGO,
                100.0,
                0.0,
                0.0,
                100.0
            );
            pedidos.add(pedido);
        }

        Desconto descontoFrequente = new Desconto("ClienteFrequente", "Desconto para cliente frequente", 7.0);
        descontoFrequente.setAtivo(true);

        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(pedidos);
        when(descontoRepository.findByCodigo("ClienteFrequente")).thenReturn(descontoFrequente);

        // Act
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(clienteTeste);

        // Assert
        assertNotNull(descontoAplicavel);
        assertEquals("ClienteFrequente", descontoAplicavel.getCodigo());
        assertEquals(7.0, descontoAplicavel.getPercentual());
    }

    @Test
    void testClienteFrequente_ComApenasTreePedidosEmVinteDias() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime vinteDiasAtras = agora.minusDays(20);
        
        // Cria apenas 3 pedidos pagos (não é frequente)
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Pedido pedido = new Pedido(
                (long) i,
                clienteTeste,
                vinteDiasAtras.plusDays(i),
                new ArrayList<>(),
                Pedido.Status.PAGO,
                100.0,
                0.0,
                0.0,
                100.0
            );
            pedidos.add(pedido);
        }

        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(pedidos);

        // Act
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(clienteTeste);

        // Assert
        assertNull(descontoAplicavel);
    }

    @Test
    void testClienteFrequente_SemPedidosEmVinteDias() {
        // Arrange
        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(new ArrayList<>());

        // Act
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(clienteTeste);

        // Assert
        assertNull(descontoAplicavel);
    }

    // ==================== TESTES PARA CLIENTE GASTADOR ====================

    @Test
    void testClienteGastador_ComGastoMaiorQue500EmTrintaDias() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime trintaDiasAtras = agora.minusDays(30);
        
        // Cria 3 pedidos pagos totalizando R$ 600
        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido1 = new Pedido(1, clienteTeste, trintaDiasAtras.plusDays(5), new ArrayList<>(),
                Pedido.Status.PAGO, 200.0, 0.0, 0.0, 200.0);
        Pedido pedido2 = new Pedido(2, clienteTeste, trintaDiasAtras.plusDays(10), new ArrayList<>(),
                Pedido.Status.PAGO, 200.0, 0.0, 0.0, 200.0);
        Pedido pedido3 = new Pedido(3, clienteTeste, trintaDiasAtras.plusDays(15), new ArrayList<>(),
                Pedido.Status.PAGO, 200.0, 0.0, 0.0, 200.0);
        pedidos.addAll(Arrays.asList(pedido1, pedido2, pedido3));

        Desconto descontoGastador = new Desconto("ClienteGastador", "Desconto para cliente gastador (gasto > R$ 500)", 15.0);
        descontoGastador.setAtivo(true);

        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(pedidos);
        when(descontoRepository.findByCodigo("ClienteGastador")).thenReturn(descontoGastador);

        // Act
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(clienteTeste);

        // Assert
        assertNotNull(descontoAplicavel);
        assertEquals("ClienteGastador", descontoAplicavel.getCodigo());
        assertEquals(15.0, descontoAplicavel.getPercentual());
    }

    @Test
    void testClienteGastador_ComGastoExatamente500EmTrintaDias() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime trintaDiasAtras = agora.minusDays(30);
        
        // Cria pedidos pagos totalizando exatamente R$ 500 (não é gastador)
        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido1 = new Pedido(1, clienteTeste, trintaDiasAtras.plusDays(5), new ArrayList<>(),
                Pedido.Status.PAGO, 250.0, 0.0, 0.0, 250.0);
        Pedido pedido2 = new Pedido(2, clienteTeste, trintaDiasAtras.plusDays(10), new ArrayList<>(),
                Pedido.Status.PAGO, 250.0, 0.0, 0.0, 250.0);
        pedidos.addAll(Arrays.asList(pedido1, pedido2));

        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(pedidos);

        // Act
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(clienteTeste);

        // Assert
        assertNull(descontoAplicavel);
    }

    @Test
    void testClienteGastador_ComGastoMenorQue500EmTrintaDias() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime trintaDiasAtras = agora.minusDays(30);
        
        // Cria pedidos pagos totalizando apenas R$ 300
        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido1 = new Pedido(1, clienteTeste, trintaDiasAtras.plusDays(5), new ArrayList<>(),
                Pedido.Status.PAGO, 150.0, 0.0, 0.0, 150.0);
        Pedido pedido2 = new Pedido(2, clienteTeste, trintaDiasAtras.plusDays(10), new ArrayList<>(),
                Pedido.Status.PAGO, 150.0, 0.0, 0.0, 150.0);
        pedidos.addAll(Arrays.asList(pedido1, pedido2));

        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(pedidos);

        // Act
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(clienteTeste);

        // Assert
        assertNull(descontoAplicavel);
    }

    // ==================== TESTES PARA PEDIDOS NÃO PAGOS ====================

    @Test
    void testPedidosCancelados_NaoContamParaDesconto() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime trintaDiasAtras = agora.minusDays(30);
        
        // Cria pedidos cancelados (não devem contar)
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Pedido pedido = new Pedido(
                (long) i,
                clienteTeste,
                trintaDiasAtras.plusDays(i),
                new ArrayList<>(),
                Pedido.Status.CANCELADO,
                200.0,
                0.0,
                0.0,
                200.0
            );
            pedidos.add(pedido);
        }

        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(pedidos);

        // Act
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(clienteTeste);

        // Assert
        assertNull(descontoAplicavel);
    }

    @Test
    void testPedidosNovos_NaoContamParaDesconto() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime vinteDiasAtras = agora.minusDays(20);
        
        // Cria pedidos com status NOVO (não devem contar)
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Pedido pedido = new Pedido(
                (long) i,
                clienteTeste,
                vinteDiasAtras.plusDays(i),
                new ArrayList<>(),
                Pedido.Status.NOVO,
                100.0,
                0.0,
                0.0,
                100.0
            );
            pedidos.add(pedido);
        }

        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(pedidos);

        // Act
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(clienteTeste);

        // Assert
        assertNull(descontoAplicavel);
    }

    // ==================== TESTES PARA CÁLCULO DE DESCONTO ====================

    @Test
    void testCalcularValorDesconto_ClienteGastador() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime trintaDiasAtras = agora.minusDays(30);
        
        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido1 = new Pedido(1, clienteTeste, trintaDiasAtras.plusDays(5), new ArrayList<>(),
                Pedido.Status.PAGO, 300.0, 0.0, 0.0, 300.0);
        Pedido pedido2 = new Pedido(2, clienteTeste, trintaDiasAtras.plusDays(10), new ArrayList<>(),
                Pedido.Status.PAGO, 300.0, 0.0, 0.0, 300.0);
        pedidos.addAll(Arrays.asList(pedido1, pedido2));

        Desconto descontoGastador = new Desconto("ClienteGastador", "Desconto para cliente gastador", 15.0);
        descontoGastador.setAtivo(true);

        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(pedidos);
        when(descontoRepository.findByCodigo("ClienteGastador")).thenReturn(descontoGastador);

        // Act
        double valorDesconto = descontoService.calcularValorDesconto(clienteTeste, 100.0);

        // Assert
        assertEquals(15.0, valorDesconto); // 15% de 100 = 15
    }

    @Test
    void testCalcularValorDesconto_SemDescontoAplicavel() {
        // Arrange
        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(new ArrayList<>());
        when(pedidoRepository.findByClienteAndDataHoraPagamentoBetween(
            eq(clienteTeste), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(new ArrayList<>());

        // Act
        double valorDesconto = descontoService.calcularValorDesconto(clienteTeste, 100.0);

        // Assert
        assertEquals(0.0, valorDesconto);
    }
}
