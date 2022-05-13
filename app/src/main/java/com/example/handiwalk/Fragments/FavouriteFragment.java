package com.example.handiwalk.Fragments;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.example.handiwalk.ViewModels.OverviewViewModel;
import com.google.android.material.navigation.NavigationView;

public class FavouriteFragment extends Fragment implements LocationObjectAdapter.OnListItemClickListener {
  RecyclerView mTestList;
  NavigationView navigationView;
  LocationObjectAdapter mListAdapter;
  FavouriteLocationViewModel favouriteLocationViewModel;
  OverviewViewModel viewModel;
  View view;


  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.favourite_layout, container, false);

    favouriteLocationViewModel = new ViewModelProvider(this).get(FavouriteLocationViewModel.class);
    viewModel = new ViewModelProvider(this).get(OverviewViewModel.class);
    mTestList = view.findViewById(R.id.favouriteRv);
    mTestList.hasFixedSize();
    mTestList.setLayoutManager(new LinearLayoutManager(getContext()));

    favouriteLocationViewModel.getFavourite();
    mListAdapter = new LocationObjectAdapter(this);
    favouriteLocationViewModel.init().observe(getViewLifecycleOwner(), listObjects -> mListAdapter.update(listObjects));
    favouriteLocationViewModel.getResult().observe(getViewLifecycleOwner(), listObjects -> favouriteLocationViewModel.getFavourite());
    mTestList.setAdapter(mListAdapter);
    return view;
  }

  @Override
  public void onListItemClick(LocationModel clickedItem) {
    NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
    MainActivity main = (MainActivity) getActivity();
    navigationView = main.findViewById(R.id.nav_view);
    viewModel.setSnap(clickedItem);
    NavigationUI.onNavDestinationSelected(navigationView.getMenu().getItem(0), navController);
    Toast.makeText(getContext(), "Location: " + clickedItem.getName(), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onRateClick(LocationModel clickedItem) {

    if (clickedItem != null) {
      LayoutInflater inflater = (LayoutInflater)
          getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
      View popupView = inflater.inflate(R.layout.review_window, null);
      TextView text = popupView.findViewById(R.id.locationNameRate);

      Button cancelButton = popupView.findViewById(R.id.reviewCancelButton);
      Button okButton = popupView.findViewById(R.id.reviewOkButton);
      RatingBar ratingBar = popupView.findViewById(R.id.rating);


      text.setText(clickedItem.getName());
      boolean focusable = true;
      final PopupWindow popupWindow = new PopupWindow(popupView, view.getWidth(),
          view.getHeight(),
          focusable);
      popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      popupWindow.setOutsideTouchable(false);
      popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

      cancelButton.setOnClickListener(view -> {
        popupWindow.dismiss();
        System.out.println("CANCEL BUTTON PRESSED");
      });
      okButton.setOnClickListener(view -> {
        popupWindow.dismiss();
        System.out.println("OK BUTTON PRESSED. Rating is: " + ratingBar.getRating());

        viewModel.setReview(clickedItem, ratingBar.getRating());

      });

    }
  }

  @Override
  public void onFavClick(LocationModel clickedItemIndex) {
    favouriteLocationViewModel.deleteFav(clickedItemIndex);
  }


}
