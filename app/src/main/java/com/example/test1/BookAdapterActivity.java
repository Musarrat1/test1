package com.example.test1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapterActivity extends RecyclerView.Adapter<BookAdapterActivity.BookViewHolder> {

    private List<Book> bookList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public BookAdapterActivity(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_list_item, parent, false);
        return new BookViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewAuthor.setText(book.getAuthor());
        holder.textViewGenre.setText(book.getGenre());
        holder.textViewDescription.setText(book.getDescription());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void removeBookById(String bookId) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getBookId().equals(bookId)) {
                bookList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewGenre, textViewDescription;

        public BookViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.editText_book_title);
            textViewAuthor = itemView.findViewById(R.id.editText_book_author);
            textViewGenre = itemView.findViewById(R.id.editText_book_genre);
            textViewDescription = itemView.findViewById(R.id.editText_book_description);



            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}