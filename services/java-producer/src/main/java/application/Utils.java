package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
      public static Properties loadProperties(String fileName) throws IOException {
              final Properties envProps = new Properties();
              final FileInputStream input = new FileInputStream(fileName);
              envProps.load(input);
              input.close();

              return envProps;
    }

      public static String getRandomStringFromArr(String[] arr) {
            int randIdx = ThreadLocalRandom.current().nextInt(arr.length);
            String randomElem = arr[randIdx];
            return randomElem;
        }
}
