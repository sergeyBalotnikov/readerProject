package ru.mail.sergey_balotnikov.literaturetranslator.words;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mail.sergey_balotnikov.literaturetranslator.R;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordEntity;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordHolder> {

    private List<WordEntity> wordEntityList;

    public void setWordEntityList(List<WordEntity> wordEntityList) {
        this.wordEntityList = wordEntityList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_word_entity, parent, false);
        return new WordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder holder, int position) {
        holder.bind(wordEntityList.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class WordHolder extends RecyclerView.ViewHolder {
        private TextView translate;
        private TextView original;
        public WordHolder(@NonNull View itemView) {
            super(itemView);
            translate = itemView.findViewById(R.id.tvTranslateItem);
            original = itemView.findViewById(R.id.tvOriginalItem);
        }

        public void bind(WordEntity wordEntity) {

        }
    }
}
