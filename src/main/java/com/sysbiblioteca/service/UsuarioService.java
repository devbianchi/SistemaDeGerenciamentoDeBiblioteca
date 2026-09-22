package com.sysbiblioteca.service;

import com.sysbiblioteca.model.Usuario;
import com.sysbiblioteca.repository.UsuarioRepository;
import com.sysbiblioteca.util.ConsoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConsoleUtils input;

    public void cadastrarUsuario() {
        input.limparTerminal();
        try {
            input.limparScanner();
            String nome = input.lerString("Nome: ");
            String senha = input.lerString("Senha: ");
            String email = input.lerString("Email: ");

            Usuario usuario = new Usuario(nome, senha, email);
            usuarioRepository.save(usuario);
            input.limparTerminal();
            System.out.println("Usuário adicionado com êxito!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
            input.limparScanner();
        }
    }

    public void listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            System.out.println("\nNenhum usuário cadastrado.\n");
            return;
        }


        input.limparTerminal();
        System.out.println("=== LISTA DE USUÁRIOS ===");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
        System.out.println();
    }
}
