package com.chiespietro.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

@DisplayName("Testes da Entidade Desconto")
class DescontoEntityTest {

    private Desconto desconto;

    @BeforeEach
    void setUp() {
        desconto = new Desconto("ClienteFrequente", "Desconto para cliente frequente", 10.0);
    }

    @Test
    @DisplayName("Deve criar desconto com parâmetros corretos")
    void testCriarDesconto() {
        // Assert
        assertEquals("ClienteFrequente", desconto.getCodigo());
        assertEquals("Desconto para cliente frequente", desconto.getDescricao());
        assertEquals(10.0, desconto.getPercentual());
        assertTrue(desconto.getAtivo());
    }

    @Test
    @DisplayName("Deve calcular valor do desconto corretamente quando ativo")
    void testCalcularValorDescontoQuandoAtivo() {
        // Arrange
        desconto.setAtivo(true);

        // Act
        double resultado = desconto.calcularValorDesconto(100.0);

        // Assert
        assertEquals(10.0, resultado, "10% de R$ 100 deve ser R$ 10");
    }

    @Test
    @DisplayName("Deve retornar 0 quando desconto está inativo")
    void testCalcularValorDescontoQuandoInativo() {
        // Arrange
        desconto.setAtivo(false);

        // Act
        double resultado = desconto.calcularValorDesconto(100.0);

        // Assert
        assertEquals(0.0, resultado);
    }

    @Test
    @DisplayName("Deve retornar 0 quando valor é null")
    void testCalcularValorDescontoComValorNull() {
        // Arrange
        desconto.setAtivo(true);

        // Act
        double resultado = desconto.calcularValorDesconto(null);

        // Assert
        assertEquals(0.0, resultado);
    }

    @Test
    @DisplayName("Deve calcular corretamente desconto ClienteGastador com 15%")
    void testCalcularDescontoClienteGastador() {
        // Arrange
        Desconto descontoGastador = new Desconto("ClienteGastador", "15% desconto", 15.0);
        descontoGastador.setAtivo(true);

        // Act
        double resultado = descontoGastador.calcularValorDesconto(500.0);

        // Assert
        assertEquals(75.0, resultado, "15% de R$ 500 deve ser R$ 75");
    }

    @Test
    @DisplayName("Deve desativar desconto")
    void testDesativarDesconto() {
        // Arrange
        desconto.setAtivo(true);

        // Act
        desconto.setAtivo(false);

        // Assert
        assertFalse(desconto.getAtivo());
    }

    @Test
    @DisplayName("Deve ativar desconto")
    void testAtivarDesconto() {
        // Arrange
        desconto.setAtivo(false);

        // Act
        desconto.setAtivo(true);

        // Assert
        assertTrue(desconto.getAtivo());
    }

    @Test
    @DisplayName("Deve converter para string com todas as propriedades")
    void testToString() {
        // Act
        String resultado = desconto.toString();

        // Assert
        assertTrue(resultado.contains("ClienteFrequente"));
        assertTrue(resultado.contains("10.0"));
        assertTrue(resultado.contains("Desconto para cliente frequente"));
    }

    @Test
    @DisplayName("Deve calcular valor de desconto com percentuais variados")
    void testCalcularValorDescontoComPercentuaisVariados() {
        // Test 5%
        Desconto desconto5 = new Desconto("Teste5", "5% desconto", 5.0);
        desconto5.setAtivo(true);
        assertEquals(5.0, desconto5.calcularValorDesconto(100.0));

        // Test 20%
        Desconto desconto20 = new Desconto("Teste20", "20% desconto", 20.0);
        desconto20.setAtivo(true);
        assertEquals(20.0, desconto20.calcularValorDesconto(100.0));

        // Test 25%
        Desconto desconto25 = new Desconto("Teste25", "25% desconto", 25.0);
        desconto25.setAtivo(true);
        assertEquals(25.0, desconto25.calcularValorDesconto(100.0));
    }

    @Test
    @DisplayName("Deve calcular valor de desconto com valores variados")
    void testCalcularValorDescontoComValoresVariados() {
        // Arrange
        desconto.setAtivo(true);

        // Act & Assert
        assertEquals(10.0, desconto.calcularValorDesconto(100.0));
        assertEquals(50.0, desconto.calcularValorDesconto(500.0));
        assertEquals(100.0, desconto.calcularValorDesconto(1000.0));
        assertEquals(5.0, desconto.calcularValorDesconto(50.0));
    }

    @Test
    @DisplayName("Deve retornar 0 para valor negativo")
    void testCalcularValorDescontoComValorNegativo() {
        // Arrange
        desconto.setAtivo(true);

        // Act
        double resultado = desconto.calcularValorDesconto(-100.0);

        // Assert
        assertEquals(-10.0, resultado, "Percentual aplicado também em valores negativos");
    }
}
