package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class Test2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
    }

    class SaveTask extends AsyncTask {

        ProgressDialog dialog;

        List<Node> list = new ArrayList<>();

        class Node {

            List<String> components = new ArrayList<>();
            String name;
        }

        public SaveTask(Context context) {
            dialog = new ProgressDialog(context);
            dialog.show();

            String[] strings = ResourceUtil.getStringArray(context, R.array.appfilter);
            for (String s : strings) {
                String[] tmp = s.split("|");
                boolean flag = false;
                for (Node node : list) {
                    if (node.name.equals(tmp[1])) {
                        node.components.add(tmp[0]);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    Node node = new Node();
                    node.components.add(tmp[0]);
                    node.name = tmp[1];
                    list.add(node);
                }

            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            return null;
        }
    }

//    @AVClassName(TestBean.TEST_OBJECT)
//    public class TestBean extends AVObject {
//        static final String TEST_OBJECT = "TestObject";
//        public static final String COLUMN_APP_WORDS = "words";
//        public static final String COLUMN_COMPONENTS = "components";
//
//        public String getWords(){
//            return getString(COLUMN_APP_WORDS);
//        }
//
//        public String[] getComponent(){
//            return
//        }
//    }

}
