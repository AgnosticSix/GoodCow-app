package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.upc.agnosticsix.goodcow.R;

import java.util.List;

import model.Cow;

public class BovinoAdapter extends ArrayAdapter<Cow> {

    private final Context context;
    private final List<Cow> cowList;
    private final LayoutInflater inflater;
    private final int mRes;

    public BovinoAdapter(@NonNull Context context, int resource, @NonNull List<Cow> objects) {
        super(context, resource, objects);
        this.context = context;
        this.cowList = objects;
        this.inflater = LayoutInflater.from(context);
        this.mRes = resource;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = inflater.inflate(mRes, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.textViewItem);

        Cow cow = cowList.get(position);

        textView.setText(cow.getNombre()+" "+cow.getMatricula());

        return view;
    }
}
