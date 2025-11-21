package com.example.project31.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project31.Dto.SectorDto;
import com.example.project31.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiSelectSearchAdapter extends RecyclerView.Adapter<MultiSelectSearchAdapter.ItemViewHolder> implements Filterable {
    private List<SectorDto> mSectorList = new ArrayList<>();
    private List<SectorDto> mSectorFilter = new ArrayList<>();
    private Context mContext;
    private CustomFilter mFilter;
    //public List<String> mEmailList = new ArrayList<>();

    public MultiSelectSearchAdapter(Context context, List<SectorDto> mSectorList) {
        mContext = context;
        this.mSectorList = mSectorList;
        this.mSectorFilter = mSectorList;
        mFilter = new CustomFilter();
    }

    public onItemClickListener onItemClickListener;

    public void setOnItemClickListener(MultiSelectSearchAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    public interface onItemClickListener {
        void onClick(SectorDto sectorDto);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, int i) {
        final SectorDto sectorDto = mSectorFilter.get(i);
        itemViewHolder.textView.setText(sectorDto.getSectorId());

        if (sectorDto.isSelect())
            itemViewHolder.checkBox.setChecked(true);
        else
            itemViewHolder.checkBox.setChecked(false);


        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sectorDto.isSelect()) {
                    sectorDto.setSelect(false);
                    itemViewHolder.checkBox.setChecked(false);

                } else {
                    sectorDto.setSelect(true);
                   itemViewHolder.checkBox.setChecked(true);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSectorFilter.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CheckBox checkBox;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }


    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (charSequence != null && charSequence.length() > 0) {
                ArrayList<SectorDto> filters = new ArrayList<>();
                charSequence = charSequence.toString().toUpperCase();
                for (int i = 0; i < mSectorList.size(); i++) {
                    if (mSectorList.get(i).getSectorId().toUpperCase().contains(charSequence) || mSectorList.get(i).getSectorId().toUpperCase().contains(charSequence)) {
//                        SectorDto sectorDto = new SectorDto(mSectorList.get(i).getSectorId());
//                        sectorDto.setSectorId(mSectorList.get(i).getSectorId());
//                        sectorDto.setSelect(mSectorList.get(i).isSelect());
                        filters.add(mSectorList.get(i));


                    }
                }
                results.count = filters.size();
                results.values = filters;

            } else {
                results.count = mSectorList.size();
                results.values = mSectorList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mSectorFilter = (ArrayList<SectorDto>) filterResults.values;

            notifyDataSetChanged();
        }



    }

    public String showInView(){
        String showInView="";
        for (SectorDto s: mSectorList){
            if(s.isSelect())
                showInView= showInView+s.getSectorId()+", ";
        }

        return  showInView.substring(0, showInView.length() - 2);
    }

}