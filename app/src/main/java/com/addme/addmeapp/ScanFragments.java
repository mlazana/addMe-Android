package com.addme.addmeapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ScanFragments extends FragmentStatePagerAdapter{

    int counttab;

    public ScanFragments(FragmentManager fm,int counttab){
        super(fm);
        this.counttab = counttab;
    }

    @Override
    public Fragment getItem(int i){
        switch (i) {
            case 0 :
                MyQrCode myqrcode = new MyQrCode();
                return myqrcode;
            case 1 :
                ScanQrCode scanqrcode = new ScanQrCode();
                return scanqrcode;
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return counttab;
    }

}
