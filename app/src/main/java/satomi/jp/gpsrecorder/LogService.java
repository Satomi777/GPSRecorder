package satomi.jp.gpsrecorder;

/**
 * Created by satomi on 15/11/02.
 */
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LogService extends Service {

    //for LogService loop
    private Timer mTimer = null;
    Handler mHandler = new Handler();

    // for Log record
    public int arraylength;
    public int writelength;
    public ArrayList<String> recordlist =  new ArrayList<String>();
    String name;

    @Override
    public void onCreate(){
        Log.d("LogService","onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"GPSRecord Start",Toast.LENGTH_SHORT).show();

        Log.d("LogService", "onStartCommand");

        //Name
        name = intent.getStringExtra("name");

        //Timer
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        Log.d("LogService", "Timer run");

                        //Datetime
                        Date date = new Date();
                        String datetime = date.toString();
                        //GPSGet class
                        GPSGet gpsg = new GPSGet(LogService.this);
                        double latitude = gpsg.location.getLatitude();
                        String latitudes = String.valueOf(latitude);
                        double longitude = gpsg.location.getLongitude();
                        String longitudes = String.valueOf(longitude);
                        //add arraylist
                        String inarray = name+","+datetime+","+latitudes+","+longitudes;
                        recordlist.add(inarray);
                        arraylength++;

                        //write file   if more than 20 line
                        if(arraylength-writelength>20){

                            Log.d("LogService","arraylength>20");
                            Log.d("LogService_arraylength",Integer.toString(arraylength));
                            Log.d("LogService_writelength",Integer.toString(writelength));

                            ArrayList<String> recordlistc = new ArrayList(recordlist);
                            OutputStream fio;
                            String filename = "GPS_"+name+".txt";
                            int i=0;
                            int arraylengthc = arraylength;
                            try{
                                fio = openFileOutput(filename,MODE_APPEND);
                                PrintWriter writer = new PrintWriter(new OutputStreamWriter(fio,"UTF-8"));
                                Log.d("LogService","openfile");
                                while(arraylengthc-writelength>0){
                                    String srecord = recordlistc.get(i);
                                    writer.append(srecord);
                                    Log.d("LogService write",srecord);
                                    i++;
                                    writelength++;
                                }
                                writer.close();
                                //arraylist clear
                                recordlist.clear();
                                recordlistc.clear();
                                arraylength=0;
                                writelength=0;

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }


                    }

                });
            }
        }, 1000, 1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.d("LogService","onDestroy");


        ArrayList<String> recordlistc = new ArrayList(recordlist);
        OutputStream fio;
        String filename = "GPS_"+name+".txt";

        int i=0;
        int arraylengthc = arraylength;

        try{
            fio = openFileOutput(filename,MODE_APPEND);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(fio,"UTF-8"));
            Log.d("LogService","stop_openfile");
            while(arraylengthc-writelength>0){
                String srecord = recordlistc.get(i);
                writer.append(srecord);
                Log.d("LogService stop write",srecord);
                i++;
                writelength++;
            }
            writer.close();
            //arraylist clear
            recordlist.clear();
            recordlistc.clear();
            arraylength=0;
            writelength=0;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //stop timer
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        Toast.makeText(this,"LogService onDestroy",Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent arg0){
        Log.d("LogService","onBind");
        return null;
    }

}
