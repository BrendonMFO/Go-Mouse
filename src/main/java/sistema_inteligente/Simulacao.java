package sistema_inteligente;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import sistema_inteligente.Configuration.CONFIGS;

public class Simulacao {

  private final Configuration configuration;

  private final Integer quantidadeCruzamento;

  private final Integer melhorFitness;

  public Simulacao(Configuration configuration) {
    this.configuration = configuration;
    this.melhorFitness = calcularMelhorCaso();
    this.quantidadeCruzamento = calcularQuantidadeCruzamento();
  }

  public Individuo iniciar() {
    Integer geracao = 0;
    RandomArrayList<Individuo> populacao = executarSelecao(gerarPopulacao());
    do {
      gerarFilhos(populacao);
      gerarMutacoes(populacao);
      reclassificarIndividuos(populacao);
      populacao = selecionarMelhoresIndividuos(populacao);
      geracao++;
    } while (geracao < configuration.getConfigValue(CONFIGS.QUANT_GERACOES)
        && !Objects.equals(populacao.get(0).getFitness(), melhorFitness));
    return populacao.get(0);
  }

  private RandomArrayList<Individuo> gerarPopulacao() {
    RandomArrayList<Individuo> populacao = new RandomArrayList<>();
    for (Integer i = 0; i < configuration.getConfigValue(CONFIGS.TAXA_POPULACAO); i++) {
      populacao.add(
        new Individuo(
          configuration.getConfigValue(CONFIGS.POSICAO_RATO),
          configuration.getConfigValue(CONFIGS.POSICAO_QUEIJO)
        )
      );
    }
    return populacao;
  }

  private RandomArrayList<Individuo> executarSelecao(RandomArrayList<Individuo> populacao) {
    switch (configuration.getConfigValue(CONFIGS.MECANISMO_SELECAO)) {
      case 1:
        return selecaoRank(populacao);
      case 2:
        return selecaoTorneio(populacao);
      case 3:
        return selecaoRankTorneio(populacao);
    }
    throw new Error("Nenhum metodo de seleçao definido.");
  }

  private RandomArrayList<Individuo> selecaoRank(RandomArrayList<Individuo> individuos) {
    return new RandomArrayList<Individuo>(
      individuos
        .stream()
        .sorted(Comparator.comparing(Individuo::getFitness))
        .collect(Collectors.toCollection(RandomArrayList::new))
        .subList(0, quantidadeCruzamento)
    );
  }

  private RandomArrayList<Individuo> selecaoTorneio(RandomArrayList<Individuo> individuos) {
    return IntStream.range(0, quantidadeCruzamento)
        .mapToObj(i -> individuos.getRandom().compareTo(individuos.getRandom()))
        .collect(Collectors.toCollection(RandomArrayList::new));
  }

  private RandomArrayList<Individuo> selecaoRankTorneio(RandomArrayList<Individuo> individuos) {
    int rankPor = (int) (quantidadeCruzamento * 0.1F);
    var selecaoRank = selecaoRank(individuos);
    var selecaoTorneio = selecaoTorneio(individuos);
    return RandomArrayList.<Individuo>ofCollections(
      selecaoRank.subList(0, rankPor),
      selecaoTorneio.subList(0, quantidadeCruzamento - rankPor)
    );
  }

  private RandomArrayList<Individuo> gerarFilhos(RandomArrayList<Individuo> populacao) {
    int repeticoes = (int) ((float) quantidadeCruzamento / 2);
    while (repeticoes != 0) {
      var filhos = Individuo.gerarFilhos(populacao.getRandom(), populacao.getRandom());
      populacao.add(filhos.getKey());
      populacao.add(filhos.getValue());
      repeticoes--;
    }
    return populacao;
  }

  private RandomArrayList<Individuo> gerarMutacoes(RandomArrayList<Individuo> populacao) {
    for (int i = 0; i < calcularQuantidadeMutaçao(populacao.size()); i++) {
      populacao.getRandom().mutarGene();
    }
    return populacao;
  }

  private RandomArrayList<Individuo> reclassificarIndividuos(RandomArrayList<Individuo> populacao) {
    populacao.forEach(individuo -> individuo.setFitness());
    return populacao;
  }

  private RandomArrayList<Individuo> selecionarMelhoresIndividuos(RandomArrayList<Individuo> populacao) {
    return new RandomArrayList<Individuo>(
      populacao
        .stream()
        .sorted(Comparator.comparing(Individuo::getFitness))
        .collect(Collectors.toCollection(RandomArrayList::new))
        .subList(0, configuration.getConfigValue(CONFIGS.TAXA_POPULACAO))
    );
  }

  private Integer calcularQuantidadeCruzamento() {
    Integer quantidade = configuration.getConfigValue(CONFIGS.TAXA_POPULACAO);
    Integer taxaCruzamento = configuration.getConfigValue(CONFIGS.TAXA_CRUZAMENTO);
    return (int) (quantidade * (taxaCruzamento / 100.00F));
  }

  private Integer calcularQuantidadeMutaçao(int quantidadeIndividuos) {
    return (int) ((quantidadeIndividuos * 8) * ((float) configuration.getConfigValue(CONFIGS.TAXA_MUTACAO) / 100));
  }

  private Integer calcularMelhorCaso() {
    Integer posicaoRato = configuration.getConfigValue(CONFIGS.POSICAO_RATO);
    Integer posicaoQueijo = configuration.getConfigValue(CONFIGS.POSICAO_QUEIJO);
    return Math.abs(posicaoRato - posicaoQueijo) + 9;
  }
}
