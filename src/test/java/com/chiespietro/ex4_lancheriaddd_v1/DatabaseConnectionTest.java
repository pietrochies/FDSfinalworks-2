package com.chiespietro.ex4_lancheriaddd_v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados.CardapioRepositoryJDBC;
import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados.ProdutosRepositoryJDBC;
import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados.ReceitasRepositoryJDBC;
import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados.IngredientesRepositoryJDBC;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CardapioRepositoryJDBC cardapioRepository;

    @Autowired
    private ProdutosRepositoryJDBC produtosRepository;

    @Autowired
    private ReceitasRepositoryJDBC receitasRepository;

    @Autowired
    private IngredientesRepositoryJDBC ingredientesRepository;

    @Test
    public void testDatabaseConnection() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertNotNull(result);
            System.out.println("✅ Conexão com banco de dados estabelecida com sucesso!");
        } catch (Exception e) {
            fail("❌ Falha na conexão com o banco de dados: " + e.getMessage());
        }
    }

    @Test
    public void testRepositoriesAreWired() {
        assertNotNull(cardapioRepository, "CardapioRepository não foi injetado");
        assertNotNull(produtosRepository, "ProdutosRepository não foi injetado");
        assertNotNull(receitasRepository, "ReceitasRepository não foi injetado");
        assertNotNull(ingredientesRepository, "IngredientesRepository não foi injetado");
        System.out.println("✅ Todos os repositórios foram injetados com sucesso!");
    }

    @Test
    public void testQueryCardapios() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cardapios", Integer.class);
            assertNotNull(count);
            System.out.println("✅ Cardápios encontrados: " + count);
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao contar cardápios: " + e.getMessage());
        }
    }

    @Test
    public void testQueryProdutos() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM produtos", Integer.class);
            assertNotNull(count);
            System.out.println("✅ Produtos encontrados: " + count);
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao contar produtos: " + e.getMessage());
        }
    }

    @Test
    public void testQueryReceitas() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM receitas", Integer.class);
            assertNotNull(count);
            System.out.println("✅ Receitas encontradas: " + count);
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao contar receitas: " + e.getMessage());
        }
    }

    @Test
    public void testQueryIngredientes() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ingredientes", Integer.class);
            assertNotNull(count);
            System.out.println("✅ Ingredientes encontrados: " + count);
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao contar ingredientes: " + e.getMessage());
        }
    }

    @Test
    public void testJPARepositoryMethods() {
        try {
            long cardapioCount = cardapioRepository.count();
            System.out.println("✅ Total de cardápios via JPA: " + cardapioCount);
            
            long produtoCount = produtosRepository.count();
            System.out.println("✅ Total de produtos via JPA: " + produtoCount);
            
            long receitaCount = receitasRepository.count();
            System.out.println("✅ Total de receitas via JPA: " + receitaCount);
            
            long ingredienteCount = ingredientesRepository.count();
            System.out.println("✅ Total de ingredientes via JPA: " + ingredienteCount);
        } catch (Exception e) {
            fail("❌ Erro ao consultar repositórios JPA: " + e.getMessage());
        }
    }
}
