package Records;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FTPforEDI  {

    String server, username, password;
    FTPClient ftp;
    PrintWriter printWriter;
    PrintCommandListener printCommandLister;


    public FTPforEDI(String[] arraySettings) {
        server = arraySettings[0];
        username = arraySettings[1];
        password = arraySettings[2];
        ftp = new FTPClient();

        printWriter = new PrintWriter(System.out);
        printCommandLister = new PrintCommandListener(printWriter);
        ftp.addProtocolCommandListener(printCommandLister);

        try {
            int reply;
            ftp.connect(server);
            System.out.println("Connected to " + server + ".");

            // After connection attempt, you should check the reply code to verify success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            System.exit(1);
        }


        __main:
        try {
            if (!ftp.login(username, password)) {
                ftp.logout();
                break __main;
            }
        }catch (FTPConnectionClosedException e) {

            System.err.println("Server closed connection.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public void downloadEDIfile(String[] arraySettings) {
        int base = 0;
        boolean error = false;
        String server, username, password, remote, local, mode;

        FTPClient ftp;
        ftp = new FTPClient();

        server = arraySettings[0];
        username = arraySettings[1];
        password = arraySettings[2];
        remote = arraySettings[3];
        local = arraySettings[4];


        PrintWriter printWriter = new PrintWriter(System.out);
        PrintCommandListener printCommandLister = new PrintCommandListener(printWriter);
        ftp.addProtocolCommandListener(printCommandLister);

        try {
            int reply;
            ftp.connect(server);
            System.out.println("Connected to " + server + ".");

            // After connection attempt, you should check the reply code to verify success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            System.exit(1);
        }

        __main:
        try {
            if (!ftp.login(username, password)) {
                ftp.logout();
                error = true;
                break __main;
            }

            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            OutputStream output;
            output = new FileOutputStream(local);
            ftp.retrieveFile(remote, output);
            output.close();
            ftp.logout();
        } catch (FTPConnectionClosedException e) {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }

        //  System.exit(error ? 1 : 0);
    }


    public ArrayList<String> ftpStream(Date dateofFile, String path) {

        ArrayList<String> fileList = new ArrayList<String>();

        try {

            FTPFile[] filesInDict = ftp.listFiles("/" + path);

            fileList.clear();

            for (FTPFile cur : filesInDict) {
                Date fileDate = cur.getTimestamp().getTime();
                String string = new SimpleDateFormat("MM/dd/yyyy").format(fileDate);


                try {
                    fileDate = new SimpleDateFormat("MM/dd/yyyy").parse(string);
                } catch (ParseException e) {
                    System.err.print("Date parse error" + e);
                }



                if (fileDate.compareTo(dateofFile) == 0) {
                    fileList.add(cur.getName());
                }

            }





        } catch (FTPConnectionClosedException e) {

            System.err.println("Server closed connection.");
            e.printStackTrace();
        } catch (IOException e) {
              e.printStackTrace();
        }

        return fileList;
    }





    public void close () {
        try {
            ftp.logout();
        } catch (FTPConnectionClosedException e) {

            System.err.println("Server closed connection.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }
    }



    }


