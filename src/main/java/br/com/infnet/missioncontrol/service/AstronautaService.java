
package br.com.infnet.missioncontrol.service;

import br.com.infnet.missioncontrol.model.Astronauta;
import br.com.infnet.missioncontrol.model.StatusAstronauta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AstronautaService {

    private final List<Astronauta> astronautas = new ArrayList<>();
    private Long proximoId = 4L;

    public AstronautaService() {
        astronautas.add(new Astronauta(
                1L, "Ana Silva", "Brasil", 34, 8,
                StatusAstronauta.DISPONIVEL));

        astronautas.add(new Astronauta(
                2L, "John Carter", "Estados Unidos", 41, 15,
                StatusAstronauta.EM_MISSAO));

        astronautas.add(new Astronauta(
                3L, "Mei Lin", "China", 29, 5,
                StatusAstronauta.AFASTADO));
    }

    public List<Astronauta> listarTodos() {
        return new ArrayList<>(astronautas);
    }

    public List<Astronauta> filtrarPorStatus(StatusAstronauta status) {
        return astronautas.stream()
                .filter(a -> a.getStatus() == status)
                .toList();
    }

    public Optional<Astronauta> buscarPorId(Long id) {
        return astronautas.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public Astronauta criar(Astronauta astronauta) {
        validar(astronauta, null);

        Astronauta novoAstronauta = new Astronauta(
                proximoId++,
                astronauta.getNome().trim(),
                astronauta.getPais().trim(),
                astronauta.getIdade(),
                astronauta.getAnosExperiencia(),
                astronauta.getStatus()
        );

        astronautas.add(novoAstronauta);

        return novoAstronauta;
    }

    public Astronauta atualizar(Long id, Astronauta dados) {
        Astronauta existente = buscarPorId(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Astronauta não encontrado."));

        validar(dados, id);

        existente.setNome(dados.getNome().trim());
        existente.setPais(dados.getPais().trim());
        existente.setIdade(dados.getIdade());
        existente.setAnosExperiencia(dados.getAnosExperiencia());
        existente.setStatus(dados.getStatus());

        return existente;
    }

    public void excluir(Long id) {
        Astronauta existente = buscarPorId(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Astronauta não encontrado."));

        astronautas.remove(existente);
    }

    private void validar(Astronauta astronauta, Long idAtual) {

        if (astronauta.getNome() == null ||
                astronauta.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        if (astronauta.getPais() == null ||
                astronauta.getPais().isBlank()) {
            throw new IllegalArgumentException("País é obrigatório.");
        }

        if (astronauta.getIdade() == null ||
                astronauta.getIdade() < 18) {
            throw new IllegalArgumentException(
                    "Idade deve ser igual ou superior a 18 anos.");
        }

        if (astronauta.getAnosExperiencia() == null ||
                astronauta.getAnosExperiencia() < 0) {
            throw new IllegalArgumentException(
                    "Anos de experiência não pode ser negativo.");
        }

        if (astronauta.getStatus() == null) {
            throw new IllegalArgumentException("Status é obrigatório.");
        }

        boolean nomeDuplicado = astronautas.stream()
                .anyMatch(a ->
                        !a.getId().equals(idAtual) &&
                                a.getNome().trim().equalsIgnoreCase(
                                        astronauta.getNome().trim()));

        if (nomeDuplicado) {
            throw new IllegalArgumentException(
                    "Já existe um astronauta com esse nome.");
        }
    }
}
