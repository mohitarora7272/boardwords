package com.boardwords.adapters;


import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.boardwords.MainActivity;
import com.boardwords.R;
import com.boardwords.modal.ItemObject;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.List;
import java.util.Random;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolders> implements MediaPlayer.OnCompletionListener {

    private List<ItemObject> itemList;
    private Context context;
    private int[] androidColors;
    private ItemClickListener mClickListener;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private MediaPlayer mp;

    public RecyclerViewAdapter(Context context, List<ItemObject> itemList) {
        this.itemList = itemList;
        this.context = context;
        mp = MediaPlayer.create(context, R.raw.button_sound);
        mp.setOnCompletionListener(this);
        if (itemList != null && itemList.size() > 0) {
            androidColors = new int[itemList.size()];
            for (int i = 0; i < itemList.size(); i++) {
                androidColors[i] = itemList.get(i).getColor();
            }
        }
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.btnCharacter.setText(itemList.get(position).getName());
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.btnCharacter.setBackgroundColor(context.getResources().getColor(randomAndroidColor));

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Button btnCharacter;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            btnCharacter = (Button) itemView.findViewById(R.id.btnCharacter);
            btnCharacter.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            playSoundButton();

            if (MainActivity.getInstance().getMenuTitle().equals(context.getString(R.string.time) + " " + context.getString(R.string.time00))) {
                MainActivity.getInstance().showTimerMessage("");
                return;
            }

            if (MainActivity.getInstance().getMenuTitle().equals(context.getString(R.string.time_out))) {
                MainActivity.getInstance().showTimerMessage("");
                return;
            }

            AlphaAnimation click = new AlphaAnimation(1F, 0.4F);
            click.setDuration(500);
            view.startAnimation(click);

            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    private void playSoundButton() {
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(context, R.raw.button_sound);
            }
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return itemList.get(id).getName();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
            anim.setDuration(800);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
}
