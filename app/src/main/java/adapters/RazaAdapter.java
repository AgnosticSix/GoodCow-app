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

import model.Razas;

public class RazaAdapter extends ArrayAdapter<Razas> {

    private final Context context;
    private final List<Razas> razasList;
    private final LayoutInflater inflater;
    private final int mRes;

    public RazaAdapter(@NonNull Context context, int resource, @NonNull List<Razas> objects) {
        super(context, resource, objects);
        this.context = context;
        this.razasList = objects;
        this.inflater = LayoutInflater.from(context);
        this.mRes = resource;
    }

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

        Razas razas = razasList.get(position);

        textView.setText(razas.getNombre());

        return view;
    }
}
