package com.neub.alumni;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<com.neub.alumni.Adapter.Viewholder> {

    private List<ModelClass> modelClassList;

    public Adapter(List<ModelClass> modelClassList) {
        this.modelClassList = modelClassList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {


        String user =modelClassList.get(position).getUser();
        String text =modelClassList.get(position).getBody();

        viewholder.setData(user,text);

    }

    @Override
    public int getItemCount() {
        
        return modelClassList.size();
    }

    static class Viewholder extends RecyclerView.ViewHolder{

         private TextView user;
         private TextView body;


         public Viewholder(@NonNull View itemView) {
             super(itemView);

             user = itemView.findViewById(R.id.title);
             body = itemView.findViewById(R.id.body);

         }
         void setData(String titleText, String bodyText){


             user.setText(titleText);
             body.setText(bodyText);





             }
         }
     }

