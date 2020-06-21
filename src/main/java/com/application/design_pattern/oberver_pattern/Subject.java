package com.application.design_pattern.oberver_pattern;

/**
 * 发布订阅模式：在对象之间定义一个一对多的依赖，
 * 当一个对象状态改变的时候，所有依赖的对象都会自动收到通知
 *
 * @author yanghaiyong
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Message message);
}
