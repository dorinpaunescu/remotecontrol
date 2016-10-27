package com.example.dorinpaunescu.remotecontrol.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dorinpaunescu.remotecontrol.R;
import com.example.dorinpaunescu.remotecontrol.properties.PropConfigHolder;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by dorin.paunescu on 10/26/2016.
 */
public class MultiColumnAdapter extends BaseAdapter{

    private List<Map<String,String>> itemsContent;
    private LayoutInflater inflater;

    public MultiColumnAdapter(List<Map<String,String>> itemsContent, LayoutInflater inflater){
        this.itemsContent = itemsContent;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return itemsContent.size();
    }

    @Override
    public Map<String,String> getItem(int position) {
        return itemsContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView firstColumn;
        TextView secondColumn;
        Button editButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Map<String, String> map = itemsContent.get(position);
        ViewHolder holder;

        if(convertView == null){
            convertView=inflater.inflate(R.layout.activity_listview, null);
            TextView firstColumn = (TextView)convertView.findViewById(R.id.firstColumn);
            TextView secondColumn = (TextView)convertView.findViewById(R.id.secondColumn);
            Button editButton = (Button) convertView.findViewById(R.id.edit_btn);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(map.get(Constants.FIRST_COLUMN));

                    final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    final EditText input = new EditText(v.getContext());
                    input.setText(map.get(Constants.SECOND_COLUMN));
                    alert.setView(input);
                    alert.setTitle("Change property value");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            String value = input.getText().toString().trim();
                            map.put(Constants.SECOND_COLUMN, value);
                            notifyDataSetChanged();
                            PropConfigHolder.getInstance().getProperties().put(map.get(Constants.FIRST_COLUMN), value);
                            PropConfigHolder.getInstance().save();
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();

                }
            });

            holder = new ViewHolder();
            holder.editButton = editButton;
            holder.firstColumn = firstColumn;
            holder.secondColumn = secondColumn;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        System.out.println("Value: " + map.get(Constants.SECOND_COLUMN));
        holder.firstColumn.setText(map.get(Constants.FIRST_COLUMN));
        holder.secondColumn.setText(map.get(Constants.SECOND_COLUMN));

        return convertView;
    }
}
