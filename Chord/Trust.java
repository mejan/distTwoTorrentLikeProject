package Chord;

import Server.SuperNode;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mejan on 2016-05-24.
 */

public class Trust {
    final double reliability = 0.5;
    final HashMap<Integer, ArrayList<Double>> previousRating;
    final HashMap<Integer,Double> weights;
    final HashMap<Integer, Double> reliabilities;
    final NodeImpl self;
    int prevNode, succNode;

    public Trust(NodeImpl self){
        this.self = self;
        previousRating = new HashMap<>();
        weights = new HashMap<>();
        reliabilities = new HashMap<>();
    }

    private void updateCredibility(int neighbor){
        //neighbors previous trust for a certain node.
        double neighborsRating = getNodeRating(neighbor);

        double changeRate = 1-reliability*Math.abs(neighborsRating-Math.random()*(1.0 - 0.3) + 0.3);
        double weight = getWeight(neighbor) * changeRate;
        weights.put(neighbor, weight);
    }

    //what!?
    double getNodeRating(int neighbor){
        if(previousRating.containsKey(neighbor))
            return previousRating.get(neighbor).get(previousRating.get(neighbor).size()-1);
        return -1;
    }

    double getWeight(int id){
        if(!weights.containsKey(id))
            weights.put(id, 0.5);

        return weights.get(id);
    }


    double reputation(int toContact) throws RemoteException, NotBoundException, MalformedURLException {
        Node pred = self.getPredecessor();
        Node succ = self.getSuccessor();
        SuperNode superNode = (SuperNode)Naming.lookup("//" + Chord.superNodeIp + ":" + Chord.superNodePort + "/superNode");

        int numberOfNodes = superNode.getNumberOfNodes();
        double predRating = -1;
        double succRating = -1;

        for(int i = 0; i < Math.log(numberOfNodes) / Math.log(2) || predRating != -1 && (predRating == succRating); i++){
            predRating = pred.getNodeRating(toContact);
            succRating = succ.getNodeRating(toContact);
            if(predRating == -1)
                pred = pred.getPredecessor();
            if(succRating == -1)
                succ = pred.getSuccessor();
        }

        double predWeight = getWeight(prevNode);
        double succWeight = getWeight(succNode);

        if(predRating == -1){ predRating = 0.5; predWeight = 0.5; }
        if(succRating == -1){ succRating = 0.5; succWeight = 0.5; }

        prevNode = pred.getId();
        succNode = succ.getId();
        return (predWeight * predRating + succWeight * succRating) / 2;
    }

    double getTradeRate(int toContact){
        double numberOfTrades  = 0;
        if(previousRating.containsKey(toContact)) {
            numberOfTrades = previousRating.get(toContact).size();
        }

        if(numberOfTrades > 15)
            return 1.0;
        return numberOfTrades/15;
    }

    boolean isTrusted(int toContact) throws RemoteException, NotBoundException, MalformedURLException {
        double rel = reliability(toContact);


        double tmpRet = getTradeRate(toContact)*rel+(1-getTradeRate(toContact))*reputation(toContact);

        if(tmpRet >= 0.5 || rel ==0){

            if(!previousRating.containsKey(toContact)){
                ArrayList<Double> tmpAr = new ArrayList<Double>(){{add(Math.random()*(1-0.3)+0.3);}};
                previousRating.put(toContact, tmpAr);
            } else{
                previousRating.get(toContact).add(Math.random()*(1-0.3)+0.3);
            }

            updateCredibility(prevNode);
            updateCredibility(succNode);

            return true;
        }

        return false;
    }

    double reliability(int toContact){
        if(previousRating.containsKey(toContact)) {
            double sum = 0;
            for (double tmp : previousRating.get(toContact)) {
                sum += tmp;
            }

            return sum / previousRating.get(toContact).size();
        }

        return 0.0;
    }
}