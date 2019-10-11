package kwan.org.kwanorgmonitorfolder;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


import java.io.IOException;


public class FTPSample {
    private static final String FTP_SERVER_ADDRESS = "192.168.22.27";
    private static final int FTP_SERVER_PORT_NUMBER = 21;
    private static final int FTP_TIMEOUT = 60000;
    private static final int BUFFER_SIZE = 1024 * 1024 * 1;
    private static final String FTP_USERNAME = "ftpuser";
    private static final String FTP_PASSWORD = "12345";
    private FTPClient ftpClient;

    /**
     * main
     *
     * @param args
     */
    public static void main(String[] args) {
        FTPSample ftpSample = new FTPSample();
        ftpSample.connectFTPServer();
    }

    /**
     * connect ftp server
     *
     * @author viettuts.vn
     */
    private void connectFTPServer() {
        ftpClient = new FTPClient();
        try {
            System.out.println("connecting ftp server...");
            // connect to ftp server
            ftpClient.setDefaultTimeout(FTP_TIMEOUT);
            ftpClient.connect(FTP_SERVER_ADDRESS, FTP_SERVER_PORT_NUMBER);
            // run the passive mode command
            ftpClient.enterLocalPassiveMode();
            // check reply code
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                disconnectFTPServer();
                throw new IOException("FTP server not respond!");
            } else {
                ftpClient.setSoTimeout(FTP_TIMEOUT);
                // login ftp server
                if (!ftpClient.login(FTP_USERNAME, FTP_PASSWORD)) {
                    throw new IOException("Username or password is incorrect!");
                }
                ftpClient.setDataTimeout(FTP_TIMEOUT);
                System.out.println("connected");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * disconnect ftp server
     *
     * @author viettuts.vn
     */
    private void disconnectFTPServer() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
