package com.poskemon.epro.prservice.service.impl;

import com.poskemon.epro.prservice.domain.entity.Item;
import com.poskemon.epro.prservice.repository.ItemRepository;
import com.poskemon.epro.prservice.service.ItemService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    /**
     * 모든 아이템 조회
     *
     * @return Item List
     */
    @Override
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findItemsByDesc(String itemDescription) {
        return itemRepository.findAllByItemDescription(itemDescription);
    }
}
