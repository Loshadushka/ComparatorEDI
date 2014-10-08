package Records;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ipopovich on 25.07.2014.
 */
public class Stream {





    public InputStream inputStream(String directPath, String fileName, String[] ftpSettings) {
        InputStream is = null;
        try {
               is = urlConnection(directPath, fileName, ftpSettings).getInputStream(); // To download
        } catch (IOException e) {System.out.print("IOExeption for InputStream"+e); }

        return is;
    }



    public OutputStream outputStream(String directPath, String fileName, String[] ftpSettings)
    {
        OutputStream os = null;


        try {
              os = urlConnection(directPath, fileName, ftpSettings).getOutputStream(); // To upload

        } catch (IOException e) {System.out.print("IOExeption for OutputStream"+e); }

        return os;
    }




    private URLConnection urlConnection(String directPath, String fileName, String[] ftpSettings) {
        URLConnection urlc = null;
        try {
            URL url = new URL("ftp://" + ftpSettings[1] + ":" + ftpSettings[2] + "@" + ftpSettings[0] + "/" + directPath + fileName);
            urlc = url.openConnection();
        } catch (IOException e) {
            System.out.print("IOExeption for InputStream" + e);
        }
        return urlc;
    }




}
