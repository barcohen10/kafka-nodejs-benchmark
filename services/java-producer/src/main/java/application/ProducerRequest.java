package application;

public class ProducerRequest {
    String topic;
    String key;
    String value;

    ProducerRequest(String topic, String key, String value) {
        this.topic = topic;
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return (
            "application.ProducerRequest{" +
            "topic='" +
            topic +
            '\'' +
            ", key='" +
            key +
            '\'' +
            ", value='" +
            value +
            '}'
        );
    }
}
