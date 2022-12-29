/**
 * Inspired by: http://stackoverflow.com/a/6022474/1521064
 * A Multispinner implementation that allows to select multiple instances.
 * This is used in PrepareFlunkActivity to select multiple players from a RecyclerView
 */

package com.example.yo_flunk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.yo_flunk.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MultiSpinner extends Spinner {

    private CharSequence[] entries;
    private boolean[] selected;
    private MultiSpinnerListener listener;
    private TypedArray a;
    private String defaultText;


    public MultiSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        a = context.obtainStyledAttributes(attrs, R.styleable.MultiSpinner);
        entries = a.getTextArray(R.styleable.MultiSpinner_android_entries);
        if (entries != null) {
            selected = new boolean[entries.length]; // false-filled by default
        }
        a.recycle();
    }

    private OnMultiChoiceClickListener mOnMultiChoiceClickListener = new OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            selected[which] = isChecked;
        }
    };

    private DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // build new spinner text & delimiter management
            StringBuffer spinnerBuffer = new StringBuffer();
            for (int i = 0; i < entries.length; i++) {
                if (selected[i]) {
                    spinnerBuffer.append(entries[i]);
                    spinnerBuffer.append(", ");
                }
            }

            // Remove trailing comma
            if (spinnerBuffer.length() > 2) {
                spinnerBuffer.setLength(spinnerBuffer.length() - 2);
            }

            // display new text
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item,
                    new String[] { spinnerBuffer.toString() })
            {
                // Textview that is being displayed after selecting some players
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    Typeface externalFont = ResourcesCompat.getFont(getContext(), R.font.nunito_semibold);
                    ((TextView) v).setTypeface(externalFont);
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
                    ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
                    return v;
                }

                // New dropdown after selecting players
                public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                    View v =super.getDropDownView(position, convertView, parent);

                    Typeface externalFont= ResourcesCompat.getFont(getContext(), R.font.nunito_bold);
                    ((TextView) v).setTypeface(externalFont);
                    ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
                    return v;
                }
            };
            setAdapter(adapter);

            if (listener != null) {
                listener.onItemsSelected(selected);
            }

            // hide dialog
            dialog.dismiss();
        }
    };

    @Override
    public boolean performClick() {
        new AlertDialog.Builder(getContext())
                .setMultiChoiceItems(entries, selected, mOnMultiChoiceClickListener)
                .setPositiveButton(android.R.string.ok, mOnClickListener)
                .show();
        return true;
    }

    public void setMultiSpinnerListener(MultiSpinnerListener listener) {
        this.listener = listener;
    }


    public void setItems(CharSequence[] items, String allText,
                         MultiSpinnerListener listener) {
        this.entries = items;
        this.defaultText = allText;
        this.listener = listener;

        // all selected by default
        selected = new boolean[items.length];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText }
        ){
            // Initial Textview: "Flunkyballer hinzufügen"
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont = ResourcesCompat.getFont(getContext(), R.font.nunito_bold);

                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.medium_size));
                ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
                return v;
            }

            // Initial Dropdown Settings
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont= ResourcesCompat.getFont(getContext(), R.font.nunito_bold);
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
                return v;
            }
        };
        setAdapter(adapter);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}

