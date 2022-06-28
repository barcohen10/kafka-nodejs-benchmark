package application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

@RestController
public class Controller {

	@GetMapping("/")
	public String index() {
		return "Greetings!";
	}

	@GetMapping("/isAlive")
    public Boolean isAlive() {
    	return true;
    }

	@GetMapping("/produce")
	public String produce(@RequestParam String amountOfMessages, @RequestParam String topic) {
		try {
			return amountOfMessages + " Produced to topic " + topic;
		} finally {
			List<ProducerRequest> producerRequests = new ArrayList<ProducerRequest>();
			String[] eventKeys = new String[] { "db653823-eed8-4f49-b104-5939bfa2427c",
					"9675519e-1a6f-4142-a8d3-5ab6b7cd9a66",
					"d7d73f18-2aa9-4f80-bfcc-be0efe23e8a9",
					"f1ba2417-afd7-4193-a794-1a9c797b9baa",
					"24798e62-7d7a-4a16-8637-48f998036e1c",
					"abe209a9-1a3e-492d-b4cd-ede817631307",
					"253b155b-773c-4dad-8267-5efa5bc0cb38",
					"d4bef748-931c-4d67-80cd-81e3d1e7caa7",
					"d4bef748-931c-4d67-80cd-81e3d1e7caa7",
					"3da07737-3588-41f7-b336-fc7f630a852f"
			};

			for (int i = 0; i < parseInt(amountOfMessages); i++) {
				producerRequests.add(new ProducerRequest(topic, Utils.getRandomStringFromArr(eventKeys), "same-value"));
			}

			long produceAllMessagesStart = (new Date()).getTime();

			producerRequests.parallelStream().forEach(req ->
					SyncProducer.getInstance().produce(req)
			);

			Monitor.getInstance().produceAllEnd(topic, parseInt(amountOfMessages), produceAllMessagesStart);
		}
	}
}
