/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class FlightPair implements Comparable{
    public Flight flightOut,flightBack;

    public FlightPair(Flight flightOut, Flight flightBack) {
        this.flightOut = flightOut;
        this.flightBack = flightBack;
    }
    
    @Override
    public int compareTo(Object o) {
        FlightPair otherPair = (FlightPair) o;
        
        return (this.flightOut.getPrice()+this.flightBack.getPrice())
                -(otherPair.flightOut.getPrice()+otherPair.flightBack.getPrice());
    }
    
    
    @Override
    public String toString(){
        
        return flightOut.getPrice()+flightBack.getPrice()+"";
        
    }
    
}
