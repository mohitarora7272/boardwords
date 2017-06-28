package com.boardwords.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.boardwords.R;
import com.boardwords.modal.WordsPOJO;
import com.boardwords.utils.MediaPlayerUtil;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings("ALL")
public class FoldingCellListAdapter extends ArrayAdapter<WordsPOJO> implements MediaPlayer.OnCompletionListener {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Context context;
    private int[] androidColors;
    private List<WordsPOJO> listCh;
    private MediaPlayerUtil mediaPlayerUtil;

    public FoldingCellListAdapter(Context context, List<WordsPOJO> listCh) {
        super(context, 0, listCh);
        this.context = context;
        this.listCh = listCh;
        androidColors = context.getResources().getIntArray(R.array.color_array);
        mediaPlayerUtil = new MediaPlayerUtil(context);
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        WordsPOJO item = listCh.get(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.title = (TextView) cell.findViewById(R.id.title_price);
            viewHolder.content_title = (TextView) cell.findViewById(R.id.content_title);
            viewHolder.tv_One = (TextView) cell.findViewById(R.id.tv_One);
            viewHolder.tv_Two = (TextView) cell.findViewById(R.id.tv_Two);
            viewHolder.tv_Three = (TextView) cell.findViewById(R.id.tv_Three);
            viewHolder.tv_Four = (TextView) cell.findViewById(R.id.tv_Four);
            viewHolder.tv_Five = (TextView) cell.findViewById(R.id.tv_Five);
            viewHolder.tv_Six = (TextView) cell.findViewById(R.id.tv_Six);
            viewHolder.tv_Seven = (TextView) cell.findViewById(R.id.tv_Seven);
            viewHolder.tv_Eight = (TextView) cell.findViewById(R.id.tv_Eight);
            viewHolder.tv_Nine = (TextView) cell.findViewById(R.id.tv_Nine);
            viewHolder.tv_Ten = (TextView) cell.findViewById(R.id.tv_Ten);

            viewHolder.rating_all_Cell = (RatingBar) cell.findViewById(R.id.rating_all_Cell);
            viewHolder.rating_all_Content = (RatingBar) cell.findViewById(R.id.rating_all_Content);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        viewHolder.title.setText(item.getBoardName());
        viewHolder.content_title.setText(item.getBoardName());

        viewHolder.rating_all_Cell.setRating(item.getAverageRating());
        viewHolder.rating_all_Content.setRating(item.getAverageRating());

        int randomAndroidColor1 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_One.setText(item.getWordsList().get(0));
        viewHolder.tv_One.setBackgroundColor(randomAndroidColor1);
        int randomAndroidColor2 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Two.setText(item.getWordsList().get(1));
        viewHolder.tv_Two.setBackgroundColor(randomAndroidColor2);
        int randomAndroidColor3 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Three.setText(item.getWordsList().get(2));
        viewHolder.tv_Three.setBackgroundColor(randomAndroidColor3);
        int randomAndroidColor4 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Four.setText(item.getWordsList().get(3));
        viewHolder.tv_Four.setBackgroundColor(randomAndroidColor4);
        int randomAndroidColor5 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Five.setText(item.getWordsList().get(4));
        viewHolder.tv_Five.setBackgroundColor(randomAndroidColor5);
        int randomAndroidColor6 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Six.setText(item.getWordsList().get(5));
        viewHolder.tv_Six.setBackgroundColor(randomAndroidColor6);
        int randomAndroidColor7 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Seven.setText(item.getWordsList().get(6));
        viewHolder.tv_Seven.setBackgroundColor(randomAndroidColor7);
        int randomAndroidColor8 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Eight.setText(item.getWordsList().get(7));
        viewHolder.tv_Eight.setBackgroundColor(randomAndroidColor8);
        int randomAndroidColor9 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Nine.setText(item.getWordsList().get(8));
        viewHolder.tv_Nine.setBackgroundColor(randomAndroidColor9);
        int randomAndroidColor10 = androidColors[new Random().nextInt(androidColors.length)];
        viewHolder.tv_Ten.setText(item.getWordsList().get(9));
        viewHolder.tv_Ten.setBackgroundColor(randomAndroidColor10);


        //set custom btn handler for list item from that item
//        if (item.getRequestBtnClickListener() != null) {
//            viewHolder.tv_One.setOnClickListener(item.getRequestBtnClickListener());
//            viewHolder.tv_Two.setOnClickListener(item.getRequestBtnClickListener());
//        } else {
//            // (optionally) add "default" handler if no handler found in item
//            viewHolder.tv_One.setOnClickListener(defaultRequestBtnClickListener);
//            viewHolder.tv_Two.setOnClickListener(defaultRequestBtnClickListener);
//        }

        viewHolder.tv_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        viewHolder.tv_Ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsPOJO item = listCh.get(position);
                setAnimationClick(view);
                mediaPlayerUtil.playSoundButton(context);
            }
        });
        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    private void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    private void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // Set Animation Click
    private void setAnimationClick(View view) {
        AlphaAnimation click = new AlphaAnimation(1F, 0.4F);
        click.setDuration(500);
        view.startAnimation(click);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView content_title;
        TextView tv_One;
        TextView tv_Two;
        TextView tv_Three;
        TextView tv_Four;
        TextView tv_Five;
        TextView tv_Six;
        TextView tv_Seven;
        TextView tv_Eight;
        TextView tv_Nine;
        TextView tv_Ten;
        RatingBar rating_all_Cell;
        RatingBar rating_all_Content;
    }
}