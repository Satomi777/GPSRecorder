package satomi.jp.gpsrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;

public class MainActivity extends Activity {

    Intent intent;
    int startservice = 0;
    String name;


    ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //List
        listView1 = (ListView)findViewById(R.id.listView1);
        String[] savedList = fileList();
        for(int i = 0; i < savedList.length; i++){
            Log.d("filename",savedList[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedList);
        listView1.setAdapter(adapter);


        //Button
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startLogService();
            }

        });

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopLogService();
            }

        });
    }

    private void startLogService(){

        startservice = 1;

        //Create log name
        Date startdatename = new Date();
        name = startdatename.toString();

        //LogService → GetGps
        intent = new Intent(MainActivity.this, LogService.class);

        intent.putExtra("state","start");
        intent.putExtra("name",name);
        startService(intent);
        Log.d("Mainactivity","startLogService");
    }

    private void stopLogService(){
        if(startservice ==1){
            intent = new Intent(MainActivity.this, LogService.class);
            intent.putExtra("state","stop");
            intent.putExtra("name",name);
            stopService(intent);

            startservice = 0;

        }

        String[] savedList = fileList();
        for(int i = 0; i < savedList.length; i++){
            Log.d("filename",savedList[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedList);
        listView1.setAdapter(adapter);

    }

}
