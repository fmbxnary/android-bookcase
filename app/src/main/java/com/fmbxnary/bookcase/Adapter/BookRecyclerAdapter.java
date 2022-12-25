package com.fmbxnary.bookcase.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbxnary.bookcase.R;
import com.fmbxnary.bookcase.Model.Book;

import java.util.ArrayList;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.myViewHolder> {
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    private ArrayList<Book> books;
    private OnItemClickListener listener;

    public BookRecyclerAdapter(ArrayList<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_single_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.et_rv_bookName.setText(books.get(position).getTitle());
        holder.et_rv_authorName.setText(books.get(position).getAuthor());
        Bitmap bmp = BitmapFactory.decodeByteArray(books.get(position).getCover(), 0, books.get(position).getCover().length);
        holder.iv_rv_bookCover.setImageBitmap(Bitmap.createScaledBitmap(bmp, 75, 100, false));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView et_rv_bookName, et_rv_authorName;
        ImageView iv_rv_bookCover;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            et_rv_authorName = itemView.findViewById(R.id.et_rv_authorName);
            et_rv_bookName = itemView.findViewById(R.id.et_rv_bookName);
            iv_rv_bookCover = itemView.findViewById(R.id.iv_rv_bookCover);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(books.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
