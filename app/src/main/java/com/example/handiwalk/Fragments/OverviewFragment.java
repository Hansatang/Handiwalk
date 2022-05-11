package com.example.handiwalk.Fragments;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handiwalk.ViewModels.OverviewViewModel;
import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.Adapters.LocationObjectAdapter;
import com.example.handiwalk.MainActivity;
import com.example.handiwalk.R;
import com.google.android.material.navigation.NavigationView;


public class OverviewFragment extends Fragment implements LocationObjectAdapter.OnListItemClickListener {
    RecyclerView mTestList;
    NavigationView navigationView;
    LocationObjectAdapter mListAdapter;
    OverviewViewModel viewModel;
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
        NavigationUI.onNavDestinationSelected(navigationView.getMenu().getItem(1), navController
        );
        //  Navigation.findNavController(view).navigate(R.id.MapFrag);
        Toast.makeText(getContext(), "Location: " + clickedItemIndex.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRateClick(LocationModel clickedItemIndex) {

        if(clickedItemIndex != null) {
            LayoutInflater inflater = (LayoutInflater)
                    getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.review_window,null);
            TextView text = popupView.findViewById(R.id.locationNameRate);

            Button cancelButton = popupView.findViewById(R.id.reviewCancelButton);
            Button okButton = popupView.findViewById(R.id.reviewOkButton);


            text.setText(clickedItemIndex.getName());
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
                System.out.println("OK BUTTON PRESSED");

                //TODO

            });

        }
    }

    float convertDpToPx(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
