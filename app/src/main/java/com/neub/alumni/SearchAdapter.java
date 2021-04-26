package com.neub.alumni;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter
        extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>
        implements Filterable {
    private List<SearchItem> searchList;
    private List<SearchItem> searchListFull;

    @Override
    public Filter getFilter() {
        return searchfilter;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, dept, session;

        SearchViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            dept = itemView.findViewById(R.id.dept);
            session = itemView.findViewById(R.id.session);
        }
    }

    SearchAdapter(List<SearchItem> searchList) {
        this.searchList = searchList;
        searchListFull = new ArrayList<>(searchList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout2, parent, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchItem currentItem = searchList.get(position);
        //holder.imageView.setImageResource(currentItem.getImageResource());
        holder.name.setText(currentItem.getName());
        holder.dept.setText(currentItem.getDept());
        //Sets designation for faculty instead of Session
        holder.session.setText(currentItem.getSession());
        Picasso.get()
                .load(currentItem.getImageResource())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    private Filter searchfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchItem> filterlist = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterlist.addAll(searchListFull);
            } else {
                String pattrn = constraint.toString().toLowerCase().trim();
                for (SearchItem item : searchListFull) {
                    if (item.getName().toLowerCase().contains(pattrn)) {
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchList.clear();
            searchList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
