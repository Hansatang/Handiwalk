package com.example.handiwalk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;


public class ChooseFrag extends Fragment implements LocationObjectAdapter.OnListItemClickListener {
    RecyclerView mTestList;
    NavigationView navigationView;
    LocationObjectAdapter mListAdapter;
    ChooseViewModel viewModel;
    View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.choose_lay, container, false);

        viewModel = new ViewModelProvider(this).get(ChooseViewModel.class);

        mTestList = view.findViewById(R.id.rv);
        mTestList.hasFixedSize();
        mTestList.setLayoutManager(new LinearLayoutManager(getContext()));


        mListAdapter = new LocationObjectAdapter(this);
        viewModel.init().observe(getViewLifecycleOwner(), listObjects -> mListAdapter.update(listObjects));
        mTestList.setAdapter(mListAdapter);


        return view;
    }

    @Override
    public void onListItemClick(LocationObject clickedItemIndex) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
        MainActivity main = (MainActivity) getActivity();
        navigationView = main.findViewById(R.id.nav_view);
        viewModel.setSnap(clickedItemIndex);
        NavigationUI.onNavDestinationSelected(navigationView.getMenu().getItem(1),
                navController
        );


        //  Navigation.findNavController(view).navigate(R.id.MapFrag);
        Toast.makeText(getContext(), "Location: " + clickedItemIndex.getName(), Toast.LENGTH_SHORT).show();
    }

}
