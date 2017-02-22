package es.uam.eps.dadm.jorgecifuentes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;

/**
 * Created by jorgecf on 22/02/17.
 */

public class ReversiView extends View {

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int size;
    private TableroReversi tablero;
    private float heightOfTile;
    private float widthOfTile;
    private float radio;


    public ReversiView(Context context) {
        super(context);
        init();
    }

    private void
    init() {
        backgroundPaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthSize < heightSize)
            width = height = heightSize = widthSize;
        else
            width = height = widthSize = heightSize;

        this.setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float boardWidth = getWidth();
        float boardHeight = getHeight();
        canvas.drawRect(0, 0, boardWidth, boardHeight, backgroundPaint);
        drawSquares(canvas, linePaint);
    }

    private void drawSquares(Canvas canvas, Paint linePaint) {

        float centerRaw, centerColumn;
        for (int i = 0; i < size; i++) {
            int pos = size - i - 1;
            centerRaw = heightOfTile * (1 + 2 * pos) / 2f;
            for (int j = 0; j < size; j++) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f;
                setPaintColor(paint, i, j);
                canvas.drawRect(centerColumn, centerRaw, radio, paint);
            }
        }
    }

    private void setPaintColor(Paint paint, int i, int j) {
        if (board.getTablero(i, j) == tablero.JUGADOR1) paint.setColor(Color.BLUE);
        else if (board.getTablero(i, j) == tablero.VACIO) paint.setColor(Color.GRAY);
        else paint.setColor(Color.GREEN);
    }

}

