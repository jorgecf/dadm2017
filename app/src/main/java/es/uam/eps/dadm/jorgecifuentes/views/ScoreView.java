package es.uam.eps.dadm.jorgecifuentes.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jorgecf on 4/03/17.
 */

public class ScoreView extends View {
    public ScoreView(Context context) {
        super(context);
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(1000, 100);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(1000, 100, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint(Color.CYAN));

        //TODO a variables

        canvas.drawCircle(0.2f * getWidth(), 0.5f * getHeight(), 0.5f * getHeight(), new Paint(Color.YELLOW));
        canvas.drawCircle(0.8f * getWidth(), 0.5f * getHeight(), 0.5f * getHeight(), new Paint(Color.YELLOW));


        Paint text=new Paint();
        text.setColor(Color.CYAN);
        text.setTextSize( 0.5f * getHeight());

        canvas.drawText("15", 0.2f * getWidth(), 0.5f * getHeight(), text);

    }
}
