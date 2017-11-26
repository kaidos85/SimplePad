package com.kaidos85.simplepad;

/**
 * Created by Aidos on 02.06.2016.
 */
public class SectionItem implements Item {
    public String Title;

    public SectionItem(String _title) {
        Title = _title;
    }

    @Override
    public boolean isSection(){
        return true;
    }

}
