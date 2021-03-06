package org.giwi.networkgraph;

import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleNode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Objects;

import giwi.org.networkgraph.GraphSurfaceView;
import giwi.org.networkgraph.beans.NetworkGraph;
import giwi.org.networkgraph.beans.Vertex;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    private View viewB;

    public GraphFragment() {
        // Required empty public constructor
    }

    public static String graphString;
    public static ArrayList<String> vertexname;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NetworkGraph graph = new NetworkGraph();

        ArrayList<String> cost = new ArrayList<>();
        ArrayList<String> vertex = new ArrayList<>();
        vertexname = new ArrayList<>();
        ArrayList<Node> nodesarray = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        int i=0;
        while(true){
            if(i<graphString.length()){
                if(graphString.charAt(i)=='\''){
                    for(int j = i+1; j<graphString.length(); j++){
                        if(graphString.charAt(j)=='\''){
                            if(!vertex.contains(graphString.substring(i+1,j))){
                                vertexname.add(graphString.substring(i+1,j));
                                nodes.add(new SimpleNode(graphString.substring(i+1,j)));
                            }
                            vertex.add(graphString.substring(i+1,j));
                            i=j;
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

        i=0;
        while(true){
            if(i<graphString.length()){
                if(graphString.charAt(i)==')'){
                    for(int j = i; j>=0; j--){
                        if(graphString.charAt(j)==','){
                            cost.add(graphString.substring(j+2,i));
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

        for(int k = 0; k< nodes.size(); k++) {
            graph.getVertex().add(new Vertex(nodes.get(k), ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.avatar)));
        }

        for(int k = 0; k< vertex.size(); k++){
            for(int l = 0; l< nodes.size(); l++){
                if (vertex.get(k).equals(nodes.get(l).getLabel())){
                    nodesarray.add(nodes.get(l));
                }
            }
        }

        for(int k = 0; k< nodesarray.size(); k+=2) {
            graph.addEdge(new SimpleEdge(nodesarray.get(k), nodesarray.get(k + 1), cost.get(k / 2)));
        }

        GraphSurfaceView surface = viewB.findViewById(R.id.mysurface);
        surface.init(graph);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        viewB=view;
        return view;
    }
}