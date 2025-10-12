package com.sorteador.receba_sorteador_api.controller;

import com.sorteador.receba_sorteador_api.model.Usuario;
import com.sorteador.receba_sorteador_api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        String[] numerosArr = usuario.getNumeros().split(",");
        for (String numStr : numerosArr) {
            try {
                int num = Integer.parseInt(numStr.trim());
                if (num < 1 || num > 1000) {
                    return ResponseEntity.badRequest().body("Número " + num + " fora do intervalo 1-1000");
                }
                if (usuarioRepository.countByNumero(num) > 0) {
                    return ResponseEntity.badRequest().body("Número " + num + " já escolhido");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Número inválido: " + numStr);
            }
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
        List<String> numerosUsadosStr = usuarioRepository.findAll().stream()
                .map(Usuario::getNumeros)
                .filter(numeros -> numeros != null)
                .collect(Collectors.toList());
        Set<Integer> numerosUsados = new HashSet<>();
        for (String nums : numerosUsadosStr) {
            for (String numStr : nums.split(",")) {
                try {
                    numerosUsados.add(Integer.parseInt(numStr.trim()));
                } catch (NumberFormatException ignored) {}
            }
        }
        return IntStream.rangeClosed(1, 1000)
                .filter(num -> !numerosUsados.contains(num))
                .boxed()
                .collect(Collectors.toList());
    }

    @GetMapping("/ganhador")
    public ResponseEntity<?> getGanhador() {
        Optional<Usuario> ganhador = usuarioRepository.findFirstByGanhouTrue();
        if (ganhador.isPresent()) {
            return ResponseEntity.ok(ganhador.get());
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            String[] numerosArr = usuario.getNumeros().split(",");
            for (String numStr : numerosArr) {
                try {
                    int num = Integer.parseInt(numStr.trim());
                    if (num < 1 || num > 1000) {
                        return ResponseEntity.badRequest().body("Número " + num + " fora do intervalo 1-1000");
                    }
                    if (usuarioRepository.countByNumeroAndIdNot(num, id) > 0) {
                        return ResponseEntity.badRequest().body("Número " + num + " já escolhido por outro usuário");
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("Número inválido: " + numStr);
                }
            }
            usuario.setId(id);
            Usuario salvo = usuarioRepository.save(usuario);
            return ResponseEntity.ok(salvo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ganhador")
    public ResponseEntity<?> definirGanhador(@PathVariable Integer id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            // Resetar outros ganhadores
            usuarioRepository.findAllByGanhouTrue().forEach(u -> {
                u.setGanhou(false);
                usuarioRepository.save(u);
            });
            Usuario usuario = optionalUsuario.get();
            usuario.setGanhou(true);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id) {
    if (!usuarioRepository.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    usuarioRepository.deleteById(id);
    return ResponseEntity.noContent().build();
}
}
