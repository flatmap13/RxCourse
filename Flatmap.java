import rx.functions.Func1;
import rx.functions.Func2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flatmap {

    public static List<Integer> repeat(int n) {
        List<Integer> xs = new ArrayList<Integer>();
        for (int i = 0; i < n; i++)
            xs.add(n);
        return xs;
    }

    public static <T,S> List<S> flatMap(List<T> xs, Func1<T, List<S>> f) {
        List<S> res = new ArrayList<S>();
        for (T x : xs) {
            List<S> ys = f.call(x);
            for (S y : ys) {
                res.add(y);
            }
        }
        return res;
    }

    public static <T,S> List<S> map(List<T> xs, Func1<T, S> f) {
        return flatMap(xs, new Func1<T, List<S>>() {
            @Override
            public List<S> call(T x) {
                return Collections.singletonList(f.call(x));
            }
        });
    }

    public static <T> List<T> filter(List<T> xs, Func1<T, Boolean> f) {
        return flatMap(xs, new Func1<T, List<T>>() {
            @Override
            public List<T> call(T x) {
                return f.call(x) ? Collections.singletonList(x)
                                 : Collections.emptyList();
            }
        });
    }

    public static <T,S> List<Pair<T,S>> crossProduct(List<T> xs, List<S> ys) {
        return flatMap(xs, new Func1<T, List<Pair<T,S>>> () {
            @Override
            public List<Pair<T, S>> call(T x) {
                return map(ys, new Func1<S, Pair<T, S>>() {
                    @Override
                    public Pair<T, S> call(S y) {
                        return new Pair<T, S>(x, y);
                    }
                });
            }
        });
    }

    public static <T,S,U> List<U> crossProductWith(List<T> xs, List<S> ys, Func2<T, S, U> f) {
        return flatMap(xs, new Func1<T, List<U>> () {
            @Override
            public List<U> call(T x) {
                return map(ys, new Func1<S, U>() {
                    @Override
                    public U call(S y) {
                        return f.call(x, y);
                    }
                });
            }
        });
    }

    public static List<Integer> IncSum(List<Integer> src) {
        List<Integer> res = new ArrayList<Integer>();
        int accumulator = 0;
        for (Integer n : src) {
            accumulator += n;
            res.add(accumulator);
        }
        return res;
    }

}

class Pair<T,S> {
    public T x;
    public S y;

    public Pair(T x, S y) {
        this.x = x;
        this.y = y;
    }
}
