package ru.mail.sergey_balotnikov.literaturetranslator.books;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ru.mail.sergey_balotnikov.literaturetranslator.R;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BooksViewHolder> {

    private List<Book> bookList;
    private OnBookTitleClickListener listener;

    public BooksListAdapter(OnBookTitleClickListener listener) {
        this.listener = listener;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        if(bookList!=null&&!bookList.isEmpty()){
            holder.bind(bookList.get(position));
            holder.bookTitle.setOnClickListener(view ->
                    listener.onTitleClick(bookList.get(position).getPath()));
        }
    }

    @Override
    public int getItemCount() {
        return bookList!=null?bookList.size():0;
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder{
        private TextView bookTitle;
        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle=itemView.findViewById(R.id.tvBookTitle);
        }

        public void bind(Book book) {
            bookTitle.setText(book.getTitle().substring(0,book.getTitle().indexOf(".")));
        }
    }
    public interface OnBookTitleClickListener{
        void onTitleClick(String path);
    }
}
