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
import java.util.zip.Inflater;

import model.Empadres;

public class EmpadreAdapter extends ArrayAdapter<Empadres> {

    private final Context context;
    private final List<Empadres> empadresList;
    private final LayoutInflater inflater;
    private final int mRes;

    public EmpadreAdapter(@NonNull Context context, int resource, @NonNull List<Empadres> objects) {
        super(context, resource, objects);
        this.context = context;
        this.empadresList = objects;
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

        Empadres empadres = empadresList.get(position);

        textView.setText(empadres.getNombre());

        return view;
    }
}
