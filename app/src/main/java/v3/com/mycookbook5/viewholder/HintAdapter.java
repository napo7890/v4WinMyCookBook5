package v3.com.mycookbook5.viewholder;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import v3.com.mycookbook5.models.DishType;

public class HintAdapter extends ArrayAdapter<DishType>{

    public HintAdapter(Context context, ArrayList<DishType> objects, int layoutResId){
        super(context, layoutResId, objects);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }

    public int getIndex(Spinner spinner, String dish_type) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(dish_type)){
                index = i;
                break;
            }
        }
        return index;
    }
}
