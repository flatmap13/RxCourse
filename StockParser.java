import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class StockParser {
    public static void main(String[] args) throws IOException {
        parseFile("IBM.json");
    }

    public static void parseFile(String filename) throws IOException {
        Scanner scanner = new Scanner(new File(filename));

        Scheduler scheduler = Schedulers.newThread();

        Observable<Stock> stocks = Observable.create(new rx.Observable.OnSubscribe<Stock>() {
            @Override
            public void call(Subscriber<? super Stock> subscriber) {
                scheduler.schedule(new Action1<Scheduler.Inner>() {
                    @Override
                    public void call(Scheduler.Inner inner) {
                        inner.schedule(new Action1<Scheduler.Inner>() {
                            @Override
                            public void call(Scheduler.Inner inner) {

                                try {
                                    if (scanner.hasNextLine()) {
                                        Stock stock = parseLine(scanner.nextLine());
                                        subscriber.onNext(stock);
                                    } else {
                                        subscriber.onCompleted();
                                    }
                                } catch(Throwable t) {
                                    subscriber.onError(t);
                                }

                                final Action1<Scheduler.Inner> innerAction = this;
                                inner.schedule(new Action1<Scheduler.Inner>() {
                                    @Override
                                    public void call(Scheduler.Inner inner) {
                                        innerAction.call(inner);
                                    }
                                }, 1, TimeUnit.SECONDS);

                            }
                        });
                    }
                });
            }
        });

        stocks.subscribe(new Action1<Stock>() {
            @Override
            public void call(Stock stock) {
                System.out.println(stock);
            }
        });

        System.in.read();
    }

    public static Stock parseLine(String line) {
        line = line.replace("{", "").replace("}", "").replace("\"", "");
        String[] parts = line.split(",");
        String date = "";
        double open = 0, high = 0, low = 0, close = 0;
        int volume = 0;
        for (String part : parts) {
            String[] keyValue = part.split(":",2);
            String key = keyValue[0];
            String value = keyValue[1];
            if (key.equals("Date")) {
                date = value;
            } else if (key.equals("Open")) {
                open = Double.parseDouble(value);
            } else if (key.equals("High")) {
                high = Double.parseDouble(value);
            } else if (key.equals("Low")) {
                low = Double.parseDouble(value);
            } else if (key.equals("Close")) {
                close = Double.parseDouble(value);
            } else if (key.equals("Volume")) {
                volume = Integer.parseInt(value);
            }
        }
        return new Stock(date, open, high, low, close, volume);
    }
}

class Stock {
    public String date;
    public double open;
    public double high;
    public double low;
    public double close;
    public int volume;

    public Stock(String date, double open, double high, double low, double close, int volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "date: " + date + " open: " + open + " high: " + high + " low: " + low + " close: " + close + " volume: " + volume;
    }
}

