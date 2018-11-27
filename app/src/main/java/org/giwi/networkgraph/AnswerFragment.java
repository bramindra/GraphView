package org.giwi.networkgraph;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerFragment extends Fragment {

    public static String answer;
    private String listAnswer;

    public AnswerFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);

        TextView textView_answerText = view.findViewById(R.id.answerText);
        ArrayList<String> path = new ArrayList<>();
        listAnswer = "";
        textView_answerText.setText("");

        int i=0;
        while(true){
            if(i<answer.length()){
                if(answer.charAt(i)==')'){
                    for(int j = i; j>=0; j--){
                        if(answer.charAt(j)=='('){
                            path.add(answer.substring(j+1,i));
                            break;
                        }
                    }
                }
            }
            else{
                break;
            }
            i+=1;
        }

        for(int k = 0; k < path.size(); k++){
            listAnswer += path.get(k)+'\n';
        }

        textView_answerText.setText(listAnswer);
        listAnswer = "";

        return view;
    }

}
