import rx.Observable;
import rx.functions.Func1;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Game {
    public static void main(String[] args) throws Throwable {
        Observable<Long> timer = Observable.timer(1, 1, TimeUnit.SECONDS);

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
        }).take(100).subscribe(System.out::println);

        System.in.read();
    }
}

