package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.entity.Item;
import java.util.List;

public interface ItemService {
    public List<Item> findAllItems();
    public List<Item> findItemsByDesc(String itemDescription);
}
