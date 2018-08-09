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

import model.Siniigas;

public class SiniigaAdapter extends ArrayAdapter<Siniigas> {

    private final Context context;
    private final List<Siniigas> siniigasList;
    private final LayoutInflater inflater;
    private final int mRes;

    public SiniigaAdapter(Context context, int resource, List<Siniigas> objects){
        super(context, resource, objects);
        this.context = context;
        this.siniigasList = objects;
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

        Siniigas siniigas = siniigasList.get(position);

        textView.setText(siniigas.getCodigo());

        return view;
    }
}
