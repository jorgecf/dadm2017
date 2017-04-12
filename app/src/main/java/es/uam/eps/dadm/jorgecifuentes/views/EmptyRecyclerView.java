package es.uam.eps.dadm.jorgecifuentes.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SimpleAdapter;

//recyclerView con empty text TODO comentarios
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
            checkEmptyRecyclerView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkEmptyRecyclerView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkEmptyRecyclerView();
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

        checkEmptyRecyclerView();
    }

    void checkEmptyRecyclerView() {
        if (emptyView != null && getAdapter() != null) {

            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;

            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            this.setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkEmptyRecyclerView();
    }

}