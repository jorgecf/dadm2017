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
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Tablero;

/**
 * Clase que representa un tablero grafico de Reversi.
 *
 * @author Jorge Cifuentes
 */
public class ReversiView extends View {

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int playerColor;
    private int oponentColor;

    private float heightOfTile;
    private float widthOfTile;
    private float radio;
    private int size;

    private TableroReversi board;

    private OnPlayListener onPlayListener;

    /**
     * Interfaz que define la ejecucion al clickar en una casilla [i, j].
     */
    public interface OnPlayListener {
        void onPlay(int row, int column);
    }


    public ReversiView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.backgroundPaint.setColor(ContextCompat.getColor(context, R.color.colorAccentLight));

        this.gridPaint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        this.gridPaint.setStrokeWidth(3);

        this.linePaint.setStrokeWidth(2);

        // Colores de las fichas.
        this.playerColor = Color.BLACK;
        this.oponentColor = Color.WHITE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        // Establecemos una dimension cuadrada con el lado igual al mayor entre altura y anchura.
        if (widthSize < heightSize)
            width = height = heightSize = widthSize;
        else
            width = height = widthSize = heightSize;

        this.setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // Dimension de cada casilla.
        this.widthOfTile = w / size;
        this.heightOfTile = h / size;

        // Establecemos el radio de los circulos relativo a la dimension de las casillas.
        if (this.widthOfTile < this.heightOfTile)
            this.radio = this.widthOfTile * 0.3f;
        else
            this.radio = this.heightOfTile * 0.3f;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawBoard(canvas, linePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (this.board.getEstado() != Tablero.EN_CURSO) {
            Snackbar.make(findViewById(R.id.board_reversiview), R.string.round_already_finished, Snackbar.LENGTH_SHORT).show();
            return super.onTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onPlayListener.onPlay(fromEventToI(event), fromEventToJ(event));
        }

        return true;
    }

    /**
     * Dibuja el tablero cuadrado, esto es, el fondo, las fichas creadas y las lineas divisorias.
     *
     * @param canvas canvas sobre el que pintar
     * @param paint  pintura a usar
     */
    private void drawBoard(Canvas canvas, Paint paint) {

        float centerRaw, centerColumn;

        // Fondo del tablero.
        canvas.drawRect(0, 0, getWidth(), getHeight(), this.backgroundPaint);

        // Fichas y lineas divisorias.
        for (int i = 0; i < size; i++) {

            int pos = this.size - i - 1;
            centerRaw = this.heightOfTile * (1 + 2 * pos) / 2f;

            int steps = i * (this.getWidth() / this.size);
            canvas.drawLine(steps, 0, steps, this.getHeight(), this.gridPaint); // lineas verticales
            canvas.drawLine(0, steps, this.getWidth(), steps, this.gridPaint); // lineas horizontales

            for (int j = 0; j < size; j++) {
                centerColumn = this.widthOfTile * (1 + 2 * j) / 2f;
                this.setPaintColor(paint, i, j);
                canvas.drawCircle(centerColumn, centerRaw, this.radio, paint);
            }
        }
    }

    /**
     * Actualiza el color de la Paint de acuerdo al color de la casilla en el tablero.
     *
     * @param paint pintura a actualizar
     * @param i     coordenada i
     * @param j     coordenada j
     */
    private void setPaintColor(Paint paint, int i, int j) {

        if (this.board.getTablero(i, j) == TableroReversi.Color.NEGRO)
            paint.setColor(this.playerColor);
        else if (this.board.getTablero(i, j) == TableroReversi.Color.BLANCO)
            paint.setColor(this.oponentColor);
        else
            paint.setColor(Color.TRANSPARENT);
    }


    private int fromEventToI(MotionEvent event) {
        int pos = (int) (event.getY() / this.heightOfTile);
        return this.size - pos - 1;
    }

    private int fromEventToJ(MotionEvent event) {
        return (int) (event.getX() / this.widthOfTile);
    }

    public void setOnPlayListener(OnPlayListener listener) {
        this.onPlayListener = listener;
    }

    public void setBoard(Tablero board) {
     //   this.board = (TableroReversi) board;
        this.board=new TableroReversi();
        try {
            this.board.stringToTablero(board.tableroToString());
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
        this.size = this.board.getSize();
    }

}