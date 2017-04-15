package es.uam.eps.dadm.jorgecifuentes.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * RecyclerView con AdapterDataObserver para mostrar un texto personalizado cuando se quede vacio el Adapter.
 *
 * @author Jorge Cifuentes
 */
public class EmptyRecyclerView extends RecyclerView {

    private View emptyView;

    /* Los tres constructores de la RecyclerView. */
    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Observador: nos sirve para mantener control de cuando la lista se vacia o se modifica.
     */
    final private AdapterDataObserver observer = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            checkAdapterState();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkAdapterState();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkAdapterState();
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {

        final Adapter prev = getAdapter();
        if (prev != null) {
            prev.unregisterAdapterDataObserver(this.observer);
        }

        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.observer);
        }

        this.checkAdapterState();
    }

    /**
     * Si el adaptador esta vacio, deja visible el menssaje de aviso.
     */
    void checkAdapterState() {
        if (this.emptyView != null && getAdapter() != null) {

            if (this.getAdapter().getItemCount() == 0) {
                this.emptyView.setVisibility(View.VISIBLE);
                this.setVisibility(View.GONE);
            } else {
                this.emptyView.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
            }

        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        this.checkAdapterState();
    }

}