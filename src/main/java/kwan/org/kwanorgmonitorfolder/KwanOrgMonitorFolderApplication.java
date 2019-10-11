package kwan.org.kwanorgmonitorfolder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.*;

@SpringBootApplication
public class KwanOrgMonitorFolderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KwanOrgMonitorFolderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Path faxFolder = Paths.get("C:\\Users\\USER\\Documents\\kwan-org-monitor-folder\\Hello");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE , StandardWatchEventKinds.ENTRY_MODIFY);

        boolean valid = true;
        do {
            WatchKey watchKey = watchService.take();

            for (WatchEvent event : watchKey.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
                    String fileName = event.context().toString();
                    System.out.println("File Created:" + fileName);
                }
            }
            valid = watchKey.reset();

        } while (valid);

    }
}
