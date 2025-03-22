package com.rose.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ImageRectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    // 使用 volatile 关键字保证可见性和禁止指令重排
    private static volatile ImageRectangle instance;

    private final List<ImageRectangle> rectanglesList = new ArrayList<>();
    // 私有构造函数，防止外部实例化
    private ImageRectangle() {
        // 初始化矩形框列表
        rectanglesList.add(new ImageRectangle(579, 208, 607, 198));
        rectanglesList.add(new ImageRectangle(1202, 208, 607, 198));
        rectanglesList.add(new ImageRectangle(1830, 208, 607, 198));
        rectanglesList.add(new ImageRectangle(580, 416, 607, 198));
        rectanglesList.add(new ImageRectangle(1202, 416, 607, 198));
        rectanglesList.add(new ImageRectangle(1830, 416, 607, 198));
        rectanglesList.add(new ImageRectangle(580, 634, 607, 198));
    }

    // 双检锁单例模式获取实例的方法
    public static ImageRectangle getInstance() {
        if (instance == null) {
            synchronized (ImageRectangle.class) {
                if (instance == null) {
                    instance = new ImageRectangle();
                }
            }
        }
        return instance;
    }

    // 获取矩形框列表
    public List<ImageRectangle> getRectanglesList() {
        return rectanglesList;
    }

}
