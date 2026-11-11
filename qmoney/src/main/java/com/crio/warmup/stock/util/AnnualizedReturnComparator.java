package com.crio.warmup.stock.util;

import java.util.Comparator;
import com.crio.warmup.stock.dto.AnnualizedReturn;

public class AnnualizedReturnComparator implements Comparator<AnnualizedReturn>{

    @Override
    public int compare(AnnualizedReturn return1, AnnualizedReturn return2) {
        // TODO Auto-generated method stub
        if(return1.getAnnualizedReturn().compareTo(return2.getAnnualizedReturn()) > 0) return -1;
        else if(return1.getAnnualizedReturn().compareTo(return2.getAnnualizedReturn()) < 0) return 1;
        return 0;
    }
    
}
