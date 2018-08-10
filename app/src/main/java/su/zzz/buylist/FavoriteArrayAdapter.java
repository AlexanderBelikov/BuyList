package su.zzz.buylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoriteArrayAdapter extends ArrayAdapter<Favorite> {
    private Context mContext;
    private ArrayList<Favorite> favoriteArray = new ArrayList<>();

    public FavoriteArrayAdapter(Context context, ArrayList<Favorite> favoriteArray){
        super(context, 0, favoriteArray);
        this.mContext = context;
        this.favoriteArray = favoriteArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.i("getView", "position: "+Integer.toString(position) + " - "+favoriteArray.get(position).getName());
        View itemView = convertView;
        Favorite f =  favoriteArray.get(position);
        if (itemView == null) {
            Log.i("getView", favoriteArray.get(position).getName()+" is null");
            itemView = LayoutInflater.from(mContext).inflate(R.layout.card_favorite, parent, false);
        }
        TextView nameView = itemView.findViewById(R.id.name);
        nameView.setText(f.getName());
        CheckBox needView = itemView.findViewById(R.id.need);
        needView.setOnCheckedChangeListener(null);
        needView.setChecked(f.getNeed());
        final String fName = f.getName();
        needView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("Checked", fName);
            }
        });

        return itemView;
    }
}
