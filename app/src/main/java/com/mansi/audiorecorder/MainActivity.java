package com.mansi.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    Button btop,btpl,btpa,btst;
    TextView tv;
    SeekBar sb;
    MediaPlayer mp=null;
    int duration=0,current=0;
    boolean pauseFinish=false,finish=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btop = findViewById(R.id.btop);
        btpl = findViewById(R.id.btpl);
        btpa = findViewById(R.id.btpa);
        btst = findViewById(R.id.btst);
        tv = findViewById(R.id.tv);
        sb = findViewById(R.id.sb);

        btpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(mp!=null){
                   mp.pause();
                   pauseFinish=true;
               }
            }
        });

        btpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp!=null){
                    mp.start();
                    pauseFinish = false;
                }
            }
        });

        btst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp!=null){
                    mp.stop();
                    pauseFinish = true;
                    finish = true;
                }
            }
        });

        btop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Intent.ACTION_GET_CONTENT);
                i1.setType("audio/*");
                startActivityForResult(Intent.createChooser(i1,"SELECT YOUR SONG"),151);
            }
        });

    }


    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data){
          if(reqCode == 151 && resCode==RESULT_OK){
              Uri uri = data.getData();
              mp = MediaPlayer.create(getApplicationContext(), uri);
              mp.start();

              duration = mp.getDuration();
              duration = duration/1000;
              tv.setText("0/"+duration);

              sb.setMax(duration);
              finish = false;
              pauseFinish = false;
              new Thread(new Runnable() {
                  @Override
                  public void run() {

                      while(!finish){
                          try{ Thread.sleep(1000);}
                          catch (Exception e){}

                          if(!pauseFinish){
                              current = mp.getCurrentPosition();
                              current = current/1000;
                              sb.setProgress(current);
                              tv.setText(""+current+"/"+duration);

                               if(current >= duration){
                                   pauseFinish = true;
                                   finish = true;
                                   tv.setText("0/0");
                                   sb.setProgress(0);
                               }
                          }

                      }

                      pauseFinish = true;
                      finish = true;
                      tv.setText("0/0");
                      sb.setProgress(0);
                      mp.stop();
                      mp=null;
                  }
              }).start();

          }
    }

}