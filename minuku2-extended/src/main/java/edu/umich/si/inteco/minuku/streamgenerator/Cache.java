package edu.umich.si.inteco.minuku.streamgenerator;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;

/**
 * Created by Joe on 2017/10/29.
 */

public class Cache {
    public static void Upload(Context mApplicationContext, String fileName, DAO<LocationDataRecord> mDAO) {
        FileInputStream inputStream;
        File file = new File(mApplicationContext.getFilesDir(), fileName);
        if (file.exists()) {
            try {
                inputStream = mApplicationContext.openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    LocationDataRecord locationDataRecord = new LocationDataRecord();
                    locationDataRecord.creationTime = Long.parseLong(line.split(":")[0]);
                    locationDataRecord.latitude = Float.parseFloat(line.split(":")[2]);
                    locationDataRecord.longitude = Float.parseFloat(line.split(":")[3]);
                    try {
                        mDAO.add(locationDataRecord);
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                }
                inputStream.close();
                file.delete();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void WriteCache(Context mApplicationContext, String fileName, LocationDataRecord locationDataRecord){
        File file = new File(mApplicationContext.getFilesDir(),fileName);
        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        String str =  Long.toString((new Date().getTime())) + ":" + locationDataRecord.toString() + '\n';
        try{
            FileOutputStream outputStream = mApplicationContext.openFileOutput(fileName, Activity.MODE_APPEND);
            outputStream.write(str.getBytes());
            outputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
