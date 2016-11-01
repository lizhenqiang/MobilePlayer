package atguigu.mobileplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

public class WelcomActivity extends Activity {
    private boolean isstart=false;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
        if(msg.what==0) {
            startMainActivity();
        }
        }
    };

    private void startMainActivity() {
        if(!isstart) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
            isstart=true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        handler.sendEmptyMessageDelayed(0,3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         super.onTouchEvent(event);
        switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN :


                        break;
                    case MotionEvent.ACTION_MOVE:


                        break;
                    case MotionEvent.ACTION_UP :

                      startMainActivity();
                        break;
                }
        return true;

    }
}
