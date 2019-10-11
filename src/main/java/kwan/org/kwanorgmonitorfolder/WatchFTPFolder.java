package kwan.org.kwanorgmonitorfolder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.nio.file.*;

public class WatchFTPFolder {

    public static void main(String[] args) {
        String server = "192.168.22.27";
        int port = 21;
        String user = "ftpuser";
        String pass = "12345";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            Path vchFolder = Paths.get("/test");
            OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(vchFolder.toFile()));
            ftpClient.retrieveFile(vchFolder.toString(), outputStream);

            WatchService watchService = FileSystems.getDefault().newWatchService();
            vchFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE , StandardWatchEventKinds.ENTRY_MODIFY , StandardWatchEventKinds.ENTRY_DELETE);

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

        } catch (IOException | InterruptedException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
