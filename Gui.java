import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui {

    private static int numClicks = 0;

    private static void createAndShowGUI() {
        final JFrame frame = new JFrame("RxSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        frame.getContentPane().add(panel);

        final JLabel label = new JLabel("Number of clicks: " + numClicks);

        final JButton button = new JButton("click me");

        final Observable<ActionEvent> events = Observable.create(
                new Observable.OnSubscribe<ActionEvent>() {
            @Override
            public void call(Subscriber<? super ActionEvent> subscriber) {
                ActionListener listener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            subscriber.onNext(e);
                        } catch(Throwable t) {
                            subscriber.onError(t);
                        }
                    }
                };

                button.addActionListener(listener);

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        button.removeActionListener(listener);
                    }
                }));
            }
        });

        events.subscribe(x -> label.setText("Number of clicks: " + ++numClicks));

        panel.add(label);
        panel.add(button);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

