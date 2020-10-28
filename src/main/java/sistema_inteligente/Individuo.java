package sistema_inteligente;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.util.Pair;

public class Individuo {

  private final Integer[] positions = new Integer[10];

  private Integer fitness;

  public static Pair<Individuo, Individuo> gerarFilhos(Individuo pai1, Individuo pai2) {
    Random random = new Random();
    int ponto = random.nextInt(6) + 2;
    List<Integer> genesPai1 = pai1.getPositionsList();
    List<Integer> genesPai2 = pai2.getPositionsList();
    Individuo filho1 = new Individuo(pai1.positions[0], pai1.positions[9],
        Stream.of(genesPai1.subList(0, ponto + 1), genesPai2.subList(ponto + 1, 10)).flatMap(Collection::stream)
            .collect(Collectors.toList()));
    Individuo filho2 = new Individuo(pai2.positions[0], pai2.positions[9],
        Stream.of(genesPai2.subList(0, ponto + 1), genesPai1.subList(ponto + 1, 10)).flatMap(Collection::stream)
            .collect(Collectors.toList()));
    return new Pair<>(filho1, filho2);
  }

  Individuo(int firstPosition, int lastPosition) {
    setFirstLastPosition(firstPosition, lastPosition);
    setIntervalPositions();
    setFitness();
  }

  Individuo(int firstPosition, int lastPosition, List<Integer> genes) {
    setFirstLastPosition(firstPosition, lastPosition);
    setGenes(genes);
    setFitness();
  }

  public final void setFitness() {
    fitness = 0;
    for (int i = 0; i < positions.length - 1; i++) {
      fitness += Math.abs(positions[i] - positions[i + 1]);
    }
    fitness += 9;
  }

  public void mutarGene() {
    Random random = new Random();
    int gene = random.nextInt(8) + 1;
    positions[gene] = random.nextInt(10) + 1;
  }

  public Integer[] getPositions() {
    return positions;
  }

  public Integer getFitness() {
    return fitness;
  }

  @Override
  public String toString() {
    return Arrays.asList(positions).toString();
  }

  public Individuo compareTo(Individuo individuo) {
    int result = fitness.compareTo(individuo.getFitness());
    return result == -1 || result == 0 ? this : individuo;
  }

  public List<Integer> getPositionsList() {
    return Arrays.asList(positions);
  }

  private void setFirstLastPosition(int firstPosition, int lastPosition) {
    positions[0] = firstPosition;
    positions[9] = lastPosition;
  }

  private void setIntervalPositions() {
    Random randomGenerator = new Random();
    for (int i = 1; i < 9; i++) {
      positions[i] = randomGenerator.nextInt(10) + 1;
    }
  }

  private void setGenes(List<Integer> genes) {
    for (int i = 1; i < 9; i++) {
      positions[i] = genes.get(i);
    }
  }
}
