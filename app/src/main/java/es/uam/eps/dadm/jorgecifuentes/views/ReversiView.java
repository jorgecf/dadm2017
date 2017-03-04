package es.uam.eps.dadm.jorgecifuentes.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 1/03/17.
 */

public class ReversiView extends View {

    private final String DEBUG = "ReversiView";

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float heightOfTile;
    private float widthOfTile;
    private float radio;
    private int size;

    private TableroReversi board;
    private OnPlayListener onPlayListener;


    public interface OnPlayListener {
        void onPlay(int row, int column);
    }


    public ReversiView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.backgroundPaint.setColor(ContextCompat.getColor(context, R.color.colorAccentLight));

        this.gridPaint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        this.gridPaint.setStrokeWidth(3);

        this.linePaint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 500;
        String wMode, hMode;

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

        widthOfTile = w / size;
        heightOfTile = h / size;

        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f;
        else
            radio = heightOfTile * 0.3f;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float boardWidth = getWidth();
        float boardHeight = getHeight();

        canvas.drawRect(0, 0, boardWidth, boardHeight, backgroundPaint);
        this.drawCircles(canvas, linePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (board.getEstado() != Tablero.EN_CURSO) {
            Snackbar.make(findViewById(R.id.board_reversiview), R.string.round_already_finished, Snackbar.LENGTH_SHORT).show();
            return super.onTouchEvent(event);
        } else if (board.movimientosValidos().size() == 0) {
            Snackbar.make(findViewById(R.id.board_reversiview), R.string.no_valid_movements, Snackbar.LENGTH_SHORT).show();
            return super.onTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onPlayListener.onPlay(fromEventToI(event), fromEventToJ(event));
        }

        return true;
    }

    private void drawCircles(Canvas canvas, Paint paint) {

        float centerRaw, centerColumn;

        for (int i = 0; i < size; i++) {

            int pos = size - i - 1;
            centerRaw = heightOfTile * (1 + 2 * pos) / 2f;

            int steps = i * (this.getWidth() / size);
            canvas.drawLine(steps, 0, steps, this.getHeight(), gridPaint); // lineas verticales
            canvas.drawLine(0, steps, this.getWidth(), steps, gridPaint); // lineas horizontales


            for (int j = 0; j < size; j++) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f;
                setPaintColor(paint, i, j);
                canvas.drawCircle(centerColumn, centerRaw, radio, paint);
            }
        }
    }


    private void setPaintColor(Paint paint, int i, int j) {
        if (board.getTablero(i, j) == TableroReversi.Color.NEGRO)
            paint.setColor(Color.BLACK);
        else if (board.getTablero(i, j) == TableroReversi.Color.BLANCO)
            paint.setColor(Color.WHITE);
        else
            paint.setColor(Color.TRANSPARENT);
    }


    private int fromEventToI(MotionEvent event) {
        int pos = (int) (event.getY() / heightOfTile);
        return size - pos - 1;
    }

    private int fromEventToJ(MotionEvent event) {
        return (int) (event.getX() / widthOfTile);
    }

    public void setOnPlayListener(OnPlayListener listener) {
        this.onPlayListener = listener;
    }

    public void setBoard(int size, Tablero board) {
        this.size = size;
        this.board = (TableroReversi) board;
    }

    public void setBoard(Tablero board) {
        this.board = (TableroReversi) board;
    }


}


