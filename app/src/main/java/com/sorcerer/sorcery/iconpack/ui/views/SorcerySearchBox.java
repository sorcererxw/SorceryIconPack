package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.quinny898.library.persistentsearch.SearchBox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Sorcerer on 2016/2/29 0029.
 */
public class SorcerySearchBox extends SearchBox {
    public SorcerySearchBox(Context context) {
        super(context);
    }

    public SorcerySearchBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SorcerySearchBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void closeSearch()
            throws Exception {

        Class<?> searchType = super.getClass();

        //访问私有方法
        //getDeclaredMethod可以获取到所有方法，而getMethod只能获取public
        Method method = searchType.getDeclaredMethod("closeSearch", String.class);

        //压制Java对访问修饰符的检查
        method.setAccessible(true);

        //调用方法;person为所在对象
        method.invoke(this);

//        //访问私有属性
//        Field field = personType.getDeclaredField("name");
//
//        field.setAccessible(true);
//
//        //为属性设置值;person为所在对象
//        field.set(person, "WalkingDog");
//
//        System.out.println("The Value Of The Field is : " + person.getName());
    }
}
