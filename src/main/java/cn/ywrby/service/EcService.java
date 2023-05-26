package cn.ywrby.service;

import cn.ywrby.domain.EBook;
import cn.ywrby.domain.EBookCol;

import java.util.List;

public interface EcService {
    boolean refreshEcData(Integer ecID);

    List<EBookCol> getEc();

    int addEc(EBookCol eBookCol) ;

    EBookCol getEcByID(Integer ec_id, Integer page, Integer pageSize);

    boolean updateEBookDetails(EBook eBook);

    List<EBook> getRandomEBook(int num);

    public boolean copyFile(String source, String dest);

    boolean deleteEC(int ec_id);

    boolean updateEC(EBookCol eBookCol);

    boolean deleteEBook(int eBookID);

    EBookCol searchEC(int ec_id, int type, List<String> searchList);
}
