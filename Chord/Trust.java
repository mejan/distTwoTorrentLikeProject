package Chord;

import java.net.MalformedURLException;
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
    Node prevNode, succNode;
    double prevStartValue = 0;

    public Trust(NodeImpl self){
        this.self = self;
        previousRating = new HashMap<>();
        weights = new HashMap<>();
        reliabilities = new HashMap<>();
        prevStartValue = (Math.random()*(1-0.4)+0.4);
    }

    private void updateCredibility(Node neighbor) throws RemoteException {
        //neighbors previous trust for a certain node.
        double neighborsRating = getNodeRating(neighbor);
        //Change rate for weigths
        double changeRate = 1-(0.5*reliability*Math.abs(/*neighborsRating - */(Math.random()*(0.12-0.0) + 0.0)));
        //calc the new weight (high means more trust, low means less trust)
        double weight = getWeight(neighbor) * changeRate;
        //set the new weight
        weights.put(neighbor.getId(), weight);
    }

    //what!?
    double getNodeRating(Node neighbor) throws RemoteException {
        if(previousRating.containsKey(neighbor.getId()))
            return previousRating.get(neighbor.getId()).get(previousRating.get(neighbor.getId()).size()-1);
        return -1;
    }

    double getWeight(Node id) throws RemoteException {
        if(!weights.containsKey(id.getId()))
            weights.put(id.getId(), 1.0);

        return weights.get(id.getId());
    }


    double reputation(Node toContact) throws RemoteException, NotBoundException, MalformedURLException {
        Node pred = self.getPredecessor();
        Node succ = self.getSuccessor();

        int numberOfNodes = Chord.getNumberOfNodesInChord();
        double predRating = -1;
        double succRating = -1;

        for(int i = 0; i < Math.log(numberOfNodes) / Math.log(2); i++){
            if (predRating != -1 && (predRating == succRating)) break;
            //System.out.println("nigga plasse: "+i+" true/false: "+(i<Math.log(numberOfNodes) / Math.log(2) || predRating != -1 && (predRating == succRating))+ " log exp value: "+ ((int)Math.log(numberOfNodes) / Math.log(2)));
            predRating = pred.getNodeRating(toContact);
            succRating = succ.getNodeRating(toContact);
            if(predRating == -1)
                pred = pred.getPredecessor();
            if(succRating == -1)
                succ = pred.getSuccessor();
        }

        double predWeight = getWeight(pred);
        double succWeight = getWeight(succ);

        if(predRating == -1){ predRating = 0.5; predWeight = 0.5; }
        if(succRating == -1){ succRating = 0.5; succWeight = 0.5; }

        prevNode = pred;
        succNode = succ;
        return (predWeight * predRating + succWeight * succRating) / 2;
    }

    double getTradeRate(Node toContact) throws RemoteException {
        double numberOfTrades  = 0;
        if(previousRating.containsKey(toContact.getId())) {
            numberOfTrades = previousRating.get(toContact.getId()).size();
        }

        if(numberOfTrades > 15)
            return (Math.random()*(1-0.4) + 0.4); //ändra random
        return numberOfTrades/15;
    }

    boolean isTrusted(final Node toContact) throws RemoteException, NotBoundException, MalformedURLException {
        double rel = reliability(toContact);


        double tmpRet = getTradeRate(toContact)*rel+(1-getTradeRate(toContact))*reputation(toContact);

        if(tmpRet >= 0.5 || rel ==0){

            if(!previousRating.containsKey(toContact.getId())){
                ArrayList<Double> tmpAr = new ArrayList<Double>(){{add((toContact.getPrevStartValue()));}};//Math.random()*(1-0.4) + 0.4));}};
                previousRating.put(toContact.getId(), tmpAr);
            } else{
                previousRating.get(toContact.getId()).add(toContact.getPrevStartValue());//(Math.random()*(1-0.4) + 0.4));
            }

            updateCredibility(prevNode);
            updateCredibility(succNode);

            return true;
        }

        return false;
    }

    double reliability(Node toContact) throws RemoteException {
        if(previousRating.containsKey(toContact.getId())) {
            double sum = 0;
            for (double tmp : previousRating.get(toContact.getId())) {
                sum += tmp;
            }

            return sum / previousRating.get(toContact.getId()).size();
        }

        return 0.0;
    }
    double getPrevStartValue(){
        return this.prevStartValue;
    }
}