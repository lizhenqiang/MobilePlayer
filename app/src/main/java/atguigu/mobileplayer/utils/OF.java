package atguigu.mobileplayer.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import atguigu.mobileplayer.domain.MediaItem;

/**
 * Created by lzq on 2016/10/6.
 */
public class OF {


    public File[] read(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

           File externalStorageDirectory= Environment.getExternalStorageDirectory();
            File file1 = new File(externalStorageDirectory,"wodezuiai2");
            try {
                file1.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("TAG", "读入的"+file1.getAbsolutePath());
            File[] files = file1.listFiles();
            return  files;

        }
        return null;


    }
    public void write(MediaItem mediaItem)  {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {





                File externalStorageDirectory =Environment.getExternalStorageDirectory();

                    File file1 = new File(externalStorageDirectory,"wodezuiai2");
                    try {
                        file1.mkdirs();
                        Log.e("TAG", "路径"+file1.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    File file = new File(file1, mediaItem.getName());

            try {
                      
                        Log.e("TAG", "写出的文件"+file.getAbsolutePath());
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

                        oos.writeObject(mediaItem);
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


    public void delete(MediaItem mediaItem) {
        File[] files = read();
        for (File file  : files) {
            if(mediaItem.getName().equals(file.getName())) {
                file.delete();
            }
        }
    }
}


