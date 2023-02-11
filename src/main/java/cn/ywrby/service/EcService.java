package cn.ywrby.service;

import cn.ywrby.domain.EBook;
import cn.ywrby.domain.EBookCol;

import java.io.IOException;
import java.util.List;

public interface EcService {
    boolean refreshEcData(Integer ecID);

    List<EBookCol> getEc();

    int addEc(EBookCol eBookCol);

    EBookCol getEcByID(Integer ec_id, Integer page, Integer pageSize);

    boolean updateEBookDetails(EBook eBook);
}
