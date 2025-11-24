package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados.ProdutosRepositoryJDBC;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/dados")
public class DadosAdminController {

    @Autowired
    private ProdutosRepositoryJDBC produtoRepository;

    /**
     * Popula o banco de dados com produtos de teste
     * Endpoint: POST /admin/dados/popular-produtos
     */
    @PostMapping("/popular-produtos")
    public ResponseEntity<?> popularProdutos() {
        try {
            List<Produto> produtos = new ArrayList<>();
            
            // Cria alguns produtos de teste
            produtos.add(new Produto(1L, "Pizza Calabresa", 5500L));
            produtos.add(new Produto(2L, "Pizza Queijo e Presunto", 6000L));
            produtos.add(new Produto(3L, "Pizza Margherita", 4000L));
            produtos.add(new Produto(4L, "Pizza Frango com Catupiry", 6500L));
            produtos.add(new Produto(5L, "Pizza Portuguesa", 7000L));
            produtos.add(new Produto(6L, "Pizza 4 Queijos", 6500L));
            produtos.add(new Produto(7L, "Pizza Vegetariana", 5000L));
            produtos.add(new Produto(8L, "Refrigerante 2L", 800L));
            produtos.add(new Produto(9L, "Suco Natural", 600L));
            produtos.add(new Produto(10L, "Cerveja Premium", 1200L));

            // Salva todos os produtos
            for (Produto p : produtos) {
                produtoRepository.save(p);
            }

            return ResponseEntity.ok(new MensagemResposta(
                "✅ " + produtos.size() + " produtos inseridos com sucesso!",
                true,
                produtos
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensagemResposta(
                "❌ Erro ao popular produtos: " + e.getMessage(),
                false,
                null
            ));
        }
    }

    /**
     * Limpa todos os produtos do banco
     * Endpoint: DELETE /admin/dados/limpar-produtos
     */
    @DeleteMapping("/limpar-produtos")
    public ResponseEntity<?> limparProdutos() {
        try {
            // Busca e deleta todos os produtos
            produtoRepository.deleteAll();
            
            return ResponseEntity.ok(new MensagemResposta(
                "✅ Todos os produtos foram removidos!",
                true,
                null
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensagemResposta(
                "❌ Erro ao limpar produtos: " + e.getMessage(),
                false,
                null
            ));
        }
    }

    /**
     * Retorna a quantidade total de produtos
     * Endpoint: GET /admin/dados/total-produtos
     */
    @GetMapping("/total-produtos")
    public ResponseEntity<?> totalProdutos() {
        try {
            long total = produtoRepository.count();
            
            return ResponseEntity.ok(new MensagemResposta(
                "Total de produtos: " + total,
                true,
                total
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensagemResposta(
                "❌ Erro ao contar produtos: " + e.getMessage(),
                false,
                null
            ));
        }
    }

    /**
     * Classe auxiliar para padronizar respostas
     */
    public static class MensagemResposta {
        public String mensagem;
        public boolean sucesso;
        public Object dados;

        public MensagemResposta(String mensagem, boolean sucesso, Object dados) {
            this.mensagem = mensagem;
            this.sucesso = sucesso;
            this.dados = dados;
        }

        public String getMensagem() { return mensagem; }
        public boolean isSucesso() { return sucesso; }
        public Object getDados() { return dados; }
    }
}
