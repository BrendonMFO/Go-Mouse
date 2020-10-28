package sistema_inteligente;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomArrayList<Type> extends ArrayList<Type> {

  private static final long serialVersionUID = 1L;

  public static <Type> RandomArrayList<Type> ofCollections(Collection<? extends Type>... c) {
    RandomArrayList<Type> temp = new RandomArrayList<>();
    for (Collection<? extends Type> collection : c) {
      temp.addAll(collection);
    }
    return temp;
  }

  public RandomArrayList() {
    super();
  }

  public RandomArrayList(Integer initialCapacity) {
    super(initialCapacity);
  }

  public RandomArrayList(Collection<? extends Type> c) {
    super(c);
  }

  public Type getRandom() {
    Random random = new Random();
    return this.get(random.nextInt(this.size()));
  }
}
