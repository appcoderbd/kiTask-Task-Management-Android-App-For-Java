package com.dscappstudio.kitask.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dscappstudio.kitask.R;


public class GraphFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View graphView = inflater.inflate(R.layout.fragment_graph, container, false);

        return graphView;
    }
}