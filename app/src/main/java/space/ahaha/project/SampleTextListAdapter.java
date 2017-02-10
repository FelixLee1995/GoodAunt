package space.ahaha.project;


/**
 * Created by ROhan on 2016/12/12 0012.
 */
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ms.square.android.expandabletextview.ExpandableTextView;

public class SampleTextListAdapter extends BaseAdapter {

    private final Context mContext;
    private final SparseBooleanArray mCollapsedStatus;
    private String[] sampleStrings;
    public SampleTextListAdapter(Context context, String name) {
        mContext  = context;
        mCollapsedStatus = new SparseBooleanArray();
        switch (name){
            case "rcbj":
                sampleStrings = mContext.getResources().getStringArray(R.array.Strrcbj);
                break;
            case "dsc":
                sampleStrings = mContext.getResources().getStringArray(R.array.Strdsc);
                break;
            case "dbdl":
                sampleStrings = mContext.getResources().getStringArray(R.array.Strdbdl);
                break;
            case "sfby":
                sampleStrings = mContext.getResources().getStringArray(R.array.Strsfby);
                break;
        }
    }

    @Override
    public int getCount() {
        return sampleStrings.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.expandableTextView.setText(sampleStrings[position], mCollapsedStatus, position);

        return convertView;
    }


    private static class ViewHolder{
        ExpandableTextView expandableTextView;
    }
}
