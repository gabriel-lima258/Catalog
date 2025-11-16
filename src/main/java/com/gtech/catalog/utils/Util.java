package com.gtech.catalog.utils;

import com.gtech.catalog.entities.Product;
import com.gtech.catalog.projetions.IdProjection;
import com.gtech.catalog.projetions.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    // metodo de transformar uma lista desordenada em ordenada
    // ID generico herdar com ? uma lista de entidade que implementa id projection
    // ordened vem de uma projection e unordered vem de uma entidade
    public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unordered) {
        // O(1)
        Map<ID, IdProjection<ID>> map = new HashMap<>();
        for (IdProjection<ID> obj: unordered) {
            map.put(obj.getId(), obj);
        }

        List<IdProjection<ID>> result = new ArrayList<>();
        for (IdProjection<ID> obj: ordered) {
            result.add(map.get(obj.getId()));
        }

        return result;
    }
}
