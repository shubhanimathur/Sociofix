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

        import com.example.project31.Dto.AreaDto;
import com.example.project31.Dto.SectorDto;
import com.example.project31.R;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.stream.Collectors;

public class MultiSelectSearchAreaAdapter extends RecyclerView.Adapter<MultiSelectSearchAreaAdapter.ItemViewHolder> implements Filterable {
    private List<AreaDto> mAreaList = new ArrayList<>();
    private List<AreaDto> mAreaFilter = new ArrayList<>();
    private Context mContext;
    private CustomFilter mFilter;


    public MultiSelectSearchAreaAdapter(Context context, List<AreaDto> mAreaList) {
        mContext = context;
        this.mAreaList = mAreaList;
        this.mAreaFilter = mAreaList;
        mFilter = new CustomFilter();
    }

    public onItemClickListener onItemClickListener;

    public void setOnItemClickListener(MultiSelectSearchAreaAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    public interface onItemClickListener {
        void onClick(AreaDto AreaDto);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, int i) {
        final AreaDto AreaDto = mAreaFilter.get(i);
        itemViewHolder.textView.setText(AreaDto.getAreaName());

        if (AreaDto.isSelect())
            itemViewHolder.checkBox.setChecked(true);
        else
            itemViewHolder.checkBox.setChecked(false);


        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AreaDto.isSelect()) {
                    AreaDto.setSelect(false);
                    itemViewHolder.checkBox.setChecked(false);

                } else {
                    AreaDto.setSelect(true);
                    itemViewHolder.checkBox.setChecked(true);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAreaFilter.size();
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
                ArrayList<AreaDto> filters = new ArrayList<>();
                charSequence = charSequence.toString().toUpperCase();
                for (int i = 0; i < mAreaList.size(); i++) {
                    if (mAreaList.get(i).getAreaName().toUpperCase().contains(charSequence) || mAreaList.get(i).getAreaName().toUpperCase().contains(charSequence)) {
//                        AreaDto areaDto = new AreaDto(,mAreaList.get(i).getAreaName());
//                        AreaDto.setAreaName(mAreaList.get(i).getAreaName());
//                        AreaDto.setSelect(mAreaList.get(i).isSelect());
                        filters.add(mAreaList.get(i));


                    }
                }
                results.count = filters.size();
                results.values = filters;

            } else {
                results.count = mAreaList.size();
                results.values = mAreaList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mAreaFilter = (ArrayList<AreaDto>) filterResults.values;

            notifyDataSetChanged();
        }


    }
    public String showInView(){
        String showInView="";
        for (AreaDto a: mAreaList){
            if(a.isSelect())
                showInView= showInView+a.getAreaName()+", ";
        }

        return  showInView.substring(0, showInView.length() - 2);
    }
}
