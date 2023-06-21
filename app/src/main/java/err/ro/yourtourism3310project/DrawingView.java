package err.ro.yourtourism3310project;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View {

    int panelHeight;
    int panelWidth;
    Canvas c;
    ArrayList<StepLog> sList;

    boolean stepTop = true;

    public DrawingView(Context context) {
        super(context);
    }

    public DrawingView(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
    }

    public DrawingView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
    }

    public void setter(ArrayList<StepLog> sList,boolean stepTop) {
        this.sList = sList;
        this.stepTop = stepTop;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        panelHeight = h;
        panelWidth = w;
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        c.drawPaint(paint);
        paint.setColor(Color.BLACK);
        drawForm(c);
    }

    public void drawForm(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        int srcWidth  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int srcHeight = Resources.getSystem().getDisplayMetrics().heightPixels -200;

        int height = (int)(srcHeight / 1.5) ;
        int width = srcWidth /3 * 2;
        int y = srcHeight / 12 ;
        int x = srcWidth / 8;
        int barWidth = width/10 ;

        c.drawLine(x, y, x, y+height, paint); //x
        c.drawLine(x, y+height, x+width, y+height, paint); //y
        paint.setColor(Color.GRAY);
        for(int i = 9; i> 0;i--){
            c.drawLine(x, y+(height*i/10), x+width, y+(height*i/10), paint);
        }
        Log.d("meD","sList.size(): "+sList.size());
        if(sList != null && sList.size() > 0) {
            int total = -1;
            if(stepTop){
                //step
                total = sList.get(0).step;
            }else{
                //attraction
                total = sList.get(0).attractionCount;
            }

            int[] color = {Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY};
            for (int i = 0; i < sList.size(); i++) {
                paint.setColor(color[i]);
                if(stepTop){
                    //step
                    c.drawRect(x + barWidth + barWidth * 2 * i, calWeight(sList.get(i).step, total, height, y), x + barWidth * 2 + barWidth * 2 * i, y + height, paint);

                }else{
                    //attraction
                    c.drawRect(x + barWidth + barWidth * 2 * i, calWeight(sList.get(i).attractionCount, total, height, y), x + barWidth * 2 + barWidth * 2 * i, y + height, paint);

                }
                paint.setColor(Color.BLACK);
                paint.setTextSize(30);
                String[] d = sList.get(i).date.split("/");
                for(int j = 0; j < d.length;j++){
                    String tmp = d[j];
                    if(j != d.length - 1){
                        tmp += "/";
                    }
                    c.drawText(tmp, x + barWidth * (1 + 2 * i) + (barWidth / 5), y + height + height / 12 +30*j, paint);
                }

                if (i == 0) {
                    c.drawText(String.valueOf(total), x / 3 * 2, y, paint);
                }
            }
        }

    }

    public int calWeight(int rate,int total,int height,int y){
        int weight = height * rate / total;
        int itemY = y + height - weight;
        return itemY;
    }
}
