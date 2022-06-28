package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws InterruptedException {

        try {
            SpringApplication.run(App.class, args);

            Monitor.init();
            System.out.println("monitor init");
            Monitor.started();
            System.out.println("monitor started");
            System.out.println("consumer is waiting for messages");
        } catch (Exception e) {
            System.err.printf("Error in App %s", e);
        }
    }
}
