import rx.Observable;

class HeadTail {

    public static <T> Observable<Cons<T>> headTail(Observable<T> xs) {
        return xs.publish(ys -> ys.take(1).map(y -> new Cons(y, ys)));
    }

    public static void main(String[] args) {
        Integer[] items = {0,1,2,3,4,5};
        Observable<Cons<Integer>> cons = headTail(Observable.from(items));

        cons.subscribe(headTail -> {
            System.out.println(headTail.head);
            headTail.tail.subscribe(x -> {
                System.out.println(x);});
        });
    }

}

class Cons<T> {
    Cons(T head, Observable<T> tail) {
        this.head = head;
        this.tail = tail;
    }
    public T head;
    public Observable<T> tail;
}

