package com.example.handiwalk.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handiwalk.ViewModels.FavouriteLocationViewModel;
import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.Adapters.LocationObjectAdapter;
import com.example.handiwalk.MainActivity;
import com.example.handiwalk.R;
import com.google.android.material.navigation.NavigationView;

public class FavouriteFragment extends Fragment implements LocationObjectAdapter.OnListItemClickListener {
  RecyclerView mTestList;
  NavigationView navigationView;
  LocationObjectAdapter mListAdapter;
  FavouriteLocationViewModel viewModel;
  View view;


  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.favourite_layout, container, false);

    viewModel = new ViewModelProvider(this).get(FavouriteLocationViewModel.class);

    mTestList = view.findViewById(R.id.favouriteRv);
    mTestList.hasFixedSize();
    mTestList.setLayoutManager(new LinearLayoutManager(getContext()));

    mListAdapter = new LocationObjectAdapter(this);
    viewModel.init().observe(getViewLifecycleOwner(), listObjects -> mListAdapter.update(listObjects));
    mTestList.setAdapter(mListAdapter);

    return view;
  }

  @Override
  public void onListItemClick(LocationModel clickedItemIndex) {
    NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
    MainActivity main = (MainActivity) getActivity();
    navigationView = main.findViewById(R.id.nav_view);
    NavigationUI.onNavDestinationSelected(navigationView.getMenu().getItem(1), navController);
    Toast.makeText(getContext(), "Location: " + clickedItemIndex.getName(), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onRateClick(LocationModel clickedItemIndex) {

  }

  @Override
  public void onFavClick(LocationModel clickedItemIndex) {

  }


}
