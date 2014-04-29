import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Game {
    public static void main(String[] args) throws Throwable {
        Observable<Long> timer = Observable.timer(1, 1, TimeUnit.SECONDS);

        Subscription subscription =
                timer.map(new Func1<Long, Date>() {
                    @Override
                    public Date call(Long t) {
                        return new Date();
                    }
                }).filter(new Func1<Date, Boolean>() {
                    @Override
                    public Boolean call(Date date) {
                        return date.getSeconds() % 2 == 0;
                    }
                }).take(100).subscribe(new Action1 <Date>() {
                    @Override
                    public void call(Date date) {
                        System.out.println(date);
                    }
                });

        Observable<Integer> timer_ = Observable.from(0, 1, 2, 3, 4, 5);
        timer_.map(new Func1<Integer, String>() {
            @Override
            public String call(Integer i) {
                return "Number " + i;
            }
        }).subscribe(new Action1 <String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

        System.out.println("hit any key to stop stream ...");
        System.in.read();
        subscription.unsubscribe();

        System.out.println("hit any key to exit ...");
        System.in.read();
    }
}
