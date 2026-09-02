package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deveRetornarNome() {
        Usuario user1 = new Usuario(23412, "guilherme", "2039482", "giudslhfisd");

        String nome = user1.getNome();

        assertEquals("guilherme", nome, "O nome deve ser guilherme");
    }
}