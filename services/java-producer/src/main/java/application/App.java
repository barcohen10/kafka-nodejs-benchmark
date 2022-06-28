package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        try {
            SpringApplication.run(App.class, args);

            Monitor.getInstance().init();

            System.out.println("monitor init");

            SyncProducer.getInstance().initializeProducer();

            System.out.println("producer initialized");

            Monitor.getInstance().started();

            System.out.println("monitor started");

            System.out.println("spring application started");

            final ExecutorService pool = Executors.newCachedThreadPool();
            final CompletionService<String> service = new ExecutorCompletionService<>(pool);
            List<ProduceCallable> callables = new ArrayList<>();

            for (int i = 0; i < 10000; i++) {
                callables.add(new ProduceCallable());
            }

            long produceAllMessagesStart = (new Date()).getTime();
            for (int i = 0; i < 10000; i++) {
                service.submit(callables.get(i));
            }

            pool.shutdown();
            pool.awaitTermination(1000, TimeUnit.SECONDS);

            Monitor.getInstance().produceAllEnd("java-multiple-partitions", 10000, produceAllMessagesStart);

        } catch (Exception e) {
            System.err.printf("Error %s", e);
        }
    }
}
