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

import model.Decesos;

public class DecesoAdapter extends ArrayAdapter<Decesos>{

    private final Context context;
    private final List<Decesos> decesosList;
    private final LayoutInflater inflater;
    private final int mRes;

    public DecesoAdapter(@NonNull Context context, int resource, @NonNull List<Decesos> objects) {
        super(context, resource, objects);
        this.context = context;
        this.decesosList = objects;
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

        Decesos decesos = decesosList.get(position);

        textView.setText(decesos.getNombre());

        return view;
    }
}
