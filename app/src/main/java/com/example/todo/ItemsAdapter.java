package com.example.todo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.R.layout.simple_list_item_1;

// Fill in data for adapter, data passed in from main activity.java
// construct item adapter

// Taking data from the model and inputting into row of recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int posiiton);
    }
    public interface OnLongClickListener{
        // Parameter as position: to know where the data is from and delete it later
        void onItemLongClicked(int position);
    }
    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener onClickListener;

    // Data about the model = strings
    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.onClickListener = clickListener;
    }

    @NonNull
    @Override
    // Creating each view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create new view and wrap it in ViewHolder
        // Use layout inflator to inflate a view
        // wrap it inside a View Holder and return it

        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return  new ViewHolder(todoView);
    }

    @Override
    // Responsible for binding data to a particular view holder
    // Taking data from specific location and putting it into a view holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // grab the item at the position

        String item = items.get(position);
        // bind the item into the specified view holder
        holder.bind(item);

    }

    @Override
    // Number of items available in data
    public int getItemCount() {
        return items.size();
    }
    // Container to provide esay access to views that represent each row of the list

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;
        public ViewHolder(View itemView){
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);

        }
        // update view inside of the view holder with this data
        public void bind(String item) {
            // get reference of view
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Communicate that this particular item from the list was collected,
                    // and send back to main activity
                    // Pass info from main activity to items --> create an interface
                    // Long pressed notify which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
