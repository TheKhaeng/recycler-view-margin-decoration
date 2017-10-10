package com.thekhaeng.recyclerviewmargindecoration;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;
import com.thekhaeng.recyclerviewmargin.OnClickLayoutMarginItemListener;
import com.thekhaeng.recyclerviewmargindecoration.event.MarginData;

import static com.thekhaeng.recyclerviewmargindecoration.MainActivity.PagerStateAdapter.GRID;
import static com.thekhaeng.recyclerviewmargindecoration.MainActivity.PagerStateAdapter.LINEAR;
import static com.thekhaeng.recyclerviewmargindecoration.MainActivity.PagerStateAdapter.STAGGERED_GRID;

public class MarginFragment extends Fragment{

    public static final String KEY_LAYOUT = "key_layout";
    public static final String KEY_SPACE = "key_space";
    public static final String KEY_TOP_MARGIN = "key_top_margin";
    public static final String KEY_LEFT_MARGIN = "key_left_margin";
    public static final String KEY_RIGHT_MARGIN = "key_right_margin";
    public static final String KEY_BOTTOM_MARGIN = "key_bottom_margin";
    private LayoutMarginDecoration linearMargin;
    private LayoutMarginDecoration gridMargin;
    private LayoutMarginDecoration stagMargin;
    private RecyclerView rvMargin;

    public static MarginFragment newInstance( Bundle bundle ){
        MarginFragment fragment = new MarginFragment();
        fragment.setArguments( bundle );
        return fragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ){
        RxBus.get().register( this );
        View rootView = inflater.inflate( R.layout.fragment_margin, container, false );
        initInstance( rootView );
        return rootView;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState ){
        super.onActivityCreated( savedInstanceState );
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        RxBus.get().unregister( this );
    }

    @SuppressWarnings( "UnusedParameters" )
    private void initInstance( View rootView ){
        int orientation = getResources().getConfiguration().orientation;
        int orientationLinear;
        int orientationGrid;
        int orientationStaggeredGrid;
        if( orientation == Configuration.ORIENTATION_PORTRAIT ){
            orientationLinear = LinearLayoutManager.VERTICAL;
            orientationGrid = GridLayoutManager.VERTICAL;
            orientationStaggeredGrid = StaggeredGridLayoutManager.VERTICAL;
        }else{
            orientationLinear = LinearLayoutManager.HORIZONTAL;
            orientationGrid = GridLayoutManager.HORIZONTAL;
            orientationStaggeredGrid = StaggeredGridLayoutManager.HORIZONTAL;
        }

        rvMargin = (RecyclerView) rootView.findViewById( R.id.rv_margin );
        int itemSpace = getSpace();
        int layout = getArguments().getInt( KEY_LAYOUT );
        if( layout == LINEAR ){
            rvMargin.removeItemDecoration( linearMargin );
            LinearLayoutManager layout1 = new LinearLayoutManager( getContext(), orientationLinear, false );
            rvMargin.setLayoutManager( layout1 );
            linearMargin = new LayoutMarginDecoration( itemSpace );
            linearMargin.setPadding( rvMargin, getMarginTop(), getMarginBottom(), getMarginLeft(), getMarginRight() );
            linearMargin.setOnClickLayoutMarginItemListener( onClickItem() );
            rvMargin.addItemDecoration( linearMargin );
        }else if( layout == GRID ){
            int gridSpan = 3;
            rvMargin.removeItemDecoration( gridMargin );
            rvMargin.setLayoutManager( new GridLayoutManager( getContext(), gridSpan, orientationGrid, false ) );
            gridMargin = new LayoutMarginDecoration( gridSpan, itemSpace );
            gridMargin.setPadding( rvMargin, getMarginTop(), getMarginBottom(), getMarginLeft(), getMarginRight() );
            gridMargin.setOnClickLayoutMarginItemListener( onClickItem() );
            rvMargin.addItemDecoration( gridMargin );
        }else if( layout == STAGGERED_GRID ){
            int stagSpan = 3;
            rvMargin.removeItemDecoration( stagMargin );
            rvMargin.setLayoutManager( new StaggeredGridLayoutManager( stagSpan, orientationStaggeredGrid ) );
            stagMargin = new LayoutMarginDecoration( stagSpan, itemSpace );
            stagMargin.setPadding( rvMargin, getMarginTop(), getMarginBottom(), getMarginLeft(), getMarginRight() );
            stagMargin.setOnClickLayoutMarginItemListener( onClickItem() );
            rvMargin.addItemDecoration( stagMargin );
        }
        rvMargin.setAdapter( new MarginAdapter( getContext() ) );
    }

    public int getSpace(){
        return (int) getArguments().getFloat( KEY_SPACE );
    }

    private int getMarginTop(){
        return (int) getArguments().getFloat( KEY_TOP_MARGIN );
    }

    private int getMarginBottom(){
        return (int) getArguments().getFloat( KEY_BOTTOM_MARGIN );
    }

    private int getMarginLeft(){
        return (int) getArguments().getFloat( KEY_LEFT_MARGIN );
    }

    private int getMarginRight(){
        return (int) getArguments().getFloat( KEY_RIGHT_MARGIN );
    }

    @Subscribe
    public void onMarginData( MarginData event ){
        Bundle args = getArguments();
        args.putFloat( KEY_SPACE, event.getSpace() );
        args.putFloat( KEY_TOP_MARGIN, event.getMarginTop() );
        args.putFloat( KEY_LEFT_MARGIN, event.getMarginLeft() );
        args.putFloat( KEY_RIGHT_MARGIN, event.getMarginRight() );
        args.putFloat( KEY_BOTTOM_MARGIN, event.getMarginBottom() );
        initInstance( getView() );
    }


    @NonNull
    private OnClickLayoutMarginItemListener onClickItem(){
        return new OnClickLayoutMarginItemListener(){
            @Override
            public void onClick( Context context, View v, int position, int spanIndex, RecyclerView.State state ){
                Toast.makeText( context, "item: " + position + "\ncolumn: " + spanIndex, Toast.LENGTH_SHORT ).show();
            }
        };
    }

}
