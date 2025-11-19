package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.DTOs.LoginRequest;
import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.DTOs.RegistroRequest;
import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.DTOs.UsuarioResponse;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    
    private AutenticacaoService autenticacaoService;

    @Autowired
    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    /**
     * POST /auth/registro - Registrar novo usuário cliente
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {
        try {
            Usuario usuario = autenticacaoService.registrarCliente(
                request.getEmail(), 
                request.getSenha(), 
                request.getNome()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioResponse(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * POST /auth/login - Autenticar usuário
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            Usuario usuario = autenticacaoService.autenticar(request.getEmail(), request.getSenha());
            session.setAttribute("usuarioId", usuario.getId());
            return ResponseEntity.ok(new UsuarioResponse(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * POST /auth/logout - Desautenticar usuário
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        autenticacaoService.desautenticar();
        session.invalidate();
        return ResponseEntity.ok("{\"mensagem\": \"Logout realizado com sucesso\"}");
    }

    /**
     * GET /auth/usuario - Retorna o usuário autenticado
     */
    @GetMapping("/usuario")
    public ResponseEntity<?> getUsuarioAutenticado() {
        try {
            Usuario usuario = autenticacaoService.getUsuarioAutenticado();
            return ResponseEntity.ok(new UsuarioResponse(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * GET /auth/verificar - Verifica se há usuário autenticado
     */
    @GetMapping("/verificar")
    public ResponseEntity<?> verificarAutenticacao() {
        boolean autenticado = autenticacaoService.estaAutenticado();
        return ResponseEntity.ok("{\"autenticado\": " + autenticado + "}");
    }
}
