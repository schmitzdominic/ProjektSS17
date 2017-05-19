package de.projektss17.bonpix.exceptions;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

public class E_NoBonFoundException extends Exception {

    public E_NoBonFoundException(Context context){
        S.outLong((AppCompatActivity)(context), context.getResources().getString(R.string.c_ocr_kassenzettel_nicht_erkannt));
    }

    public E_NoBonFoundException(Context context, String ClassName, String Message){
        Log.e(ClassName, Message);
        S.outLong((AppCompatActivity)(context), context.getResources().getString(R.string.c_ocr_kassenzettel_nicht_erkannt));
    }
}
