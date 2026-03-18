package io.github.chance.coreapm.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TestRepository{

    public void find() {
        List<Integer> list = new ArrayList<>();
        System.out.println("result = " + list.get(0));
    }
}