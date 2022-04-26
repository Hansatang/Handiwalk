package com.example.handiwalk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.SupportMapFragment;

public class ChooseFrag extends Fragment {
    RecyclerView mTestList;

    // ListAdapter mListAdapter;
    ChooseViewModel viewModel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_lay, container, false);

        viewModel = new ViewModelProvider(this).get(ChooseViewModel.class);

        mTestList = view.findViewById(R.id.rv);
        mTestList.hasFixedSize();
        mTestList.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.init().observe(getViewLifecycleOwner(), message -> {
                    System.out.println(message.get(0).getDescription());
                }
        );
        //  viewModel = new ViewModelProvider(this).get(ListObjectModel.class);
//        viewModel = new ViewModelProvider(requireActivity()).get(ListObjectModel.class);
//        mListAdapter = new ListAdapter(this);
//        viewModel.getListObjects().observe(getViewLifecycleOwner(), listObjects -> mListAdapter.update(listObjects));
//        mTestList.setAdapter(mListAdapter);

        return view;
    }


//    @Override
//    public void onListItemClick(ListObject clickedItemIndex) {
//        Toast.makeText(getContext(), "Letter Clicked: " + clickedItemIndex.getName(), Toast.LENGTH_SHORT).show();
//    }
}
