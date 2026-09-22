import com.sysbiblioteca.exception.EmprestimoNaoEncontradoException;
import com.sysbiblioteca.service.EmprestimoService;
import com.sysbiblioteca.exception.LivroIndisponivelException;
import com.sysbiblioteca.exception.LivroNaoEncontradoException;
import com.sysbiblioteca.exception.UsuarioNaoEncontradoException;
import com.sysbiblioteca.model.Emprestimo;
import com.sysbiblioteca.model.Livro;
import com.sysbiblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import com.sysbiblioteca.repository.EmprestimoRepository;
import com.sysbiblioteca.repository.LivroRepository;
import com.sysbiblioteca.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoServiceTest {

    @Test
    void deveEmprestar() {
    }

    @Test
    void emprestimoUsuarioNaoExiste() {
    }

    @Test
    void emprestimoLivroNaoExiste() {
    }

    @Test
    void emprestimoLivroIndisponivel() {
    }

    @Test
    void emprestimoDevolucao() {
    }

    @Test
    void devolucaoEmprestimoNaoExiste() {
    }
}