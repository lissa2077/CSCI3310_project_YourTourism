package err.ro.yourtourism3310project;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DialogShow extends Dialog{

    public Context c;
    public Dialog d;
    public Button yes, no;
    int mode = -1;
    int extra = -1;
    ImageButton close;

    TextView msg,msgContent;
    String message,content;

    public DialogShow(Context c, String msg, String content) {
        super(c);
        this.c = c;
        setMsg(msg);
        setContent(content);
        this.mode = -1;
    }

    public DialogShow(Context c, String msg, String content,int mode,int extra) {
        super(c);
        this.c = c;
        setMsg(msg);
        setContent(content);
        this.mode = mode;
        this.extra = extra;
    }

    public void setMsg(String message){
        this.message = message;
    }
    public void setContent(String content){
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(mode > -1){
            setContentView(R.layout.small_dialog);
            yes = findViewById(R.id.yes);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseUtil.deleteAttraction(extra);
                    dismiss();
                    MainActivity.refreshFragment();
                }
            });
            no = findViewById(R.id.no);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else{
            //intro
            setContentView(R.layout.intro_dialog);
            close = findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        msg = findViewById(R.id.msg);
        msg.setText(message);
        msgContent = findViewById(R.id.msgContent);
        msgContent.setText(content);
    }


}
