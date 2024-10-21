package com.dette.repository.implement;

import com.dette.core.Repository;
import com.dette.entities.Detail;

public interface DetailRepository extends Repository<Detail>{
    Detail selectBy(String name);

    Detail selectById(int id);
}
