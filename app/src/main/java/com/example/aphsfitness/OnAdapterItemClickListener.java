package com.example.aphsfitness;

public interface OnAdapterItemClickListener {
    void onClick(int id, String type);
    void onLongClick(int position, String type, int id);
}
