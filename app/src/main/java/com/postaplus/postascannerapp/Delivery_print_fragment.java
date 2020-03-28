package com.postaplus.postascannerapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

//import android.print.PrintManager;

//print fragment
public class Delivery_print_fragment extends Fragment {
   Button back,print;
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {

       View rootView = inflater.inflate(R.layout.activity_delivery_print, container, false);
       back=(Button)rootView.findViewById(R.id.btnbck);
       print=(Button)rootView.findViewById(R.id.printbutton);

       handleBackButton();
       print.setEnabled(false);

       return rootView;
   }

    private void handleBackButton() {
        back.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));
              getActivity().finish();
        });
    }
}