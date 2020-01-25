package logger;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class LoggerSystem {

	Map<String, LocalTime> map = new ConcurrentHashMap<>();
	Queue<String> lru = new LinkedBlockingQueue<String>();

	public void processLogger(String msg, LocalTime time) {

		if (!map.containsKey(msg)) {
			System.out.println(msg);
			map.put(msg, time);

			String str = msg + ":entry_time" + time.toString();
			lru.add(str);
		}else {
			System.out.println("duplicate msg > "+msg);
		}
	}

	public void scheduleRun() {
		synchronized (lru) {
			System.out.println("lru size begin:" + lru.size()+ "map size : "+map.size());
			while (lru.size() > 0) {
				String msg = lru.peek();
				if (msg != null) {
					String[] entry = msg.split(":entry_time");
					msg = entry[0];
					String time = entry[1];
					DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
					LocalTime time1 = LocalTime.parse(time, formatter);
					LocalTime now = LocalTime.now();
					long diffSec = SECONDS.between( time1, now);
					System.out.println("diff sec "+diffSec);
					if (diffSec > 10) {
						System.out.println("msg removed >> "+msg + "diff sec "+diffSec);
						map.remove(msg);
						lru.poll();
					}else {
						break;
					}
				}
			}
			System.out.println("lru size end :" + lru.size()+ "map size : "+map.size());
		}
	}

	public static void main(String[] args) {
		LoggerSystem system = new LoggerSystem();
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

		executorService.scheduleAtFixedRate(() -> {
			system.scheduleRun();
		}, 0, 10, TimeUnit.SECONDS);
		Stream<String> str = null;
		try {
			str = Files.lines(Paths.get("D://apache_logs"), StandardCharsets.UTF_8);
			str.forEach(s -> {
				LocalTime localTime = LocalTime.now();
				system.processLogger(s, LocalTime.parse(localTime.toString()));
				try {
					Thread.sleep(1000l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(str != null) str.close();
		}
	}
}
