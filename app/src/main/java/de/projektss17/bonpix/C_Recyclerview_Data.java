package de.projektss17.bonpix;

import android.app.LauncherActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sascha on 17.04.2017.
 */

public class C_Recyclerview_Data {
    private static final String[] titles = {"Test1", "Test2", "Test3"};
    private static final int[] icons =  {android.R.drawable.ic_popup_reminder, android.R.drawable.ic_menu_add, android.R.drawable.ic_menu_delete};

    public static List<C_Recyclerview_List_Item> getListData(){
        List<C_Recyclerview_List_Item> data = new ArrayList<>();


        for (int x =0; x< 4; x++) {
            // Create ListItem with dummy data, then add them to our List.
            for (int i = 0; i < titles.length && i < icons.length; i++){
                C_Recyclerview_List_Item item = new C_Recyclerview_List_Item();
                item.setImageResId(icons[i]);
                item.setTitle(titles[i]);
                data.add(item);

            }
        }


        return data;
    }
}
