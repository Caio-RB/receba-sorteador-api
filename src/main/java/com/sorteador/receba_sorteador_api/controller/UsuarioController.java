package com.sorteador.receba_sorteador_api.controller;

import com.sorteador.receba_sorteador_api.model.Usuario;
import com.sorteador.receba_sorteador_api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario usuario) {
        if (usuarioRepository.existsByCelular(usuario.getCelular())) {
            return ResponseEntity.badRequest().body("Celular já cadastrado");
        }
        if (usuarioRepository.existsByNumero(usuario.getNumero())) {
            return ResponseEntity.badRequest().body("Número da sorte já escolhido");
        }
        usuario.setGanhou(false);
        usuario.setData(LocalDate.now());
        Usuario salvo = usuarioRepository.save(usuario);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/numeros-disponiveis")
    public List<Integer> getNumerosDisponiveis() {
        List<Integer> numerosUsados = usuarioRepository.findAll().stream()
                .map(Usuario::getNumero)
                .collect(Collectors.toList());
        return IntStream.rangeClosed(1, 1000)
                .filter(num -> !numerosUsados.contains(num))
                .boxed()
                .collect(Collectors.toList());
    }

    @GetMapping("/ganhador")
    public ResponseEntity<?> getGanhador() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.ok(null);
        }
        Usuario ganhador = usuarios.get(0);
        ganhador.setGanhou(true);
        usuarioRepository.save(ganhador);
        return ResponseEntity.ok(ganhador);
    }
} 
