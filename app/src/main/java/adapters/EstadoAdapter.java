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

import model.Estados;

public class EstadoAdapter extends ArrayAdapter<Estados> {

    private final Context context;
    private final List<Estados> estadosList;
    private final LayoutInflater inflater;
    private final int mRes;

    public EstadoAdapter(@NonNull Context context, int resource, @NonNull List<Estados> objects) {
        super(context, resource, objects);
        this.context = context;
        this.estadosList = objects;
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

        Estados estados = estadosList.get(position);

        textView.setText(estados.getNombre());

        return view;
    }
}
