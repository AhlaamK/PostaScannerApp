package com.postaplus.postascannerapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

       back.setOnClickListener(new OnClickListener() {

           @Override
           public void onClick(View v) {
               v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));

               // moveTaskToBack(true);
                 getActivity().finish();


           }
       });
       print.setEnabled(false);
      /* print.setOnClickListener(new OnClickListener() {

           @SuppressLint("NewApi")
           @Override
           public void onClick(View v) {
               v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));

                PrintManager printManager = (PrintManager) getActivity().getBaseContext()
                           .getSystemService(Context.PRINT_SERVICE);

                   // Set job name, which will be displayed in the print queue
                   String jobName = getActivity().getBaseContext().getString(R.string.app_name) + " Document";

                   // Start a print job, passing in a PrintDocumentAdapter implementation
                   // to handle the generation of a print document
                   printManager.print(jobName, new MyPrintDocumentAdapter(getActivity().getBaseContext()),
                           null); //

           }
       });*/
       return rootView;
   }
}