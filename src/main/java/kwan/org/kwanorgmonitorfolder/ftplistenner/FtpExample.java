package kwan.org.kwanorgmonitorfolder.ftplistenner;

import java.util.concurrent.TimeUnit;
import com.github.drapostolos.rdp4j.DirectoryPoller;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;

public class FtpExample {

    public static void main(String[] args) throws Exception {
        String host = "192.168.22.27";
        String workingDirectory = "/test";
        String username = "ftpuser";
        String password = "12345";
        PolledDirectory polledDirectory = new FtpDirectory(host, workingDirectory, username, password);

        DirectoryPoller dp = DirectoryPoller.newBuilder()
                .addPolledDirectory(polledDirectory)
                .addListener(new MyListener())
                .setPollingInterval(50, TimeUnit.MILLISECONDS)
                .start();

        TimeUnit.HOURS.sleep(2);

        dp.stop();
    }
}