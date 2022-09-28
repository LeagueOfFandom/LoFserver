package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.CommonItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoViewService {

    public CommonItem getVideoListTest(){
        List<String> videoList = new ArrayList<>();
        videoList.add("fiY08uGY3dM");
        videoList.add("i7tT8BBy5zs");
        videoList.add("z1ai61tvmMo");
        videoList.add("gyD8bzTgRmM");
        videoList.add("V0N4C2Ax68Q");
        return new CommonItem(videoList);
    }
}
