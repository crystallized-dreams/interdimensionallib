package ru.crystallized_dreams.interdimensionallib.common.types;

import java.util.function.Function;

public enum Easing {
    LINEAR(v->v),
    QUAD_IN(v->v*v),
    QUAD_OUT(v->-v*(v-2)),
    QUAD_IN_OUT(v->v<0.5?2*v*v:-2*v*v+4*v-1),
    CUBIC_IN(v->v*v*v),
    CUBIC_OUT(v->--v*v*v+1),
    CUBIC_IN_OUT(v->{v*=2; if(v<1) return 0.5*v*v*v; v-=2; return 0.5*(v*v*v+2);}),
    QUART_IN(v->v*v*v*v),
    QUART_OUT(v->1-Math.pow(1-v,4)),
    QUART_IN_OUT(v->v<0.5?8*v*v*v*v:1-Math.pow(-2*v+2,4)/2),
    SINE_IN(v->1-Math.cos((v*Math.PI)/2)),
    SINE_OUT(v->Math.sin((v*Math.PI)/2)),
    SINE_IN_OUT(v->-0.5*(Math.cos(Math.PI*v)-1)),
    EXPO_IN(v->(v==0)?0:Math.pow(2,10*(v-1))),
    EXPO_OUT(v->(v==1)?1:1-Math.pow(2,-10*v)),
    EXPO_IN_OUT(v->(v==0)?0.0:(v==1)?1.0:(v<0.5)?0.5*Math.pow(2,(20*v)-10):-0.5*Math.pow(2,(-20*v)+10)+1),
    CIRC_IN(v->1-Math.sqrt(1-Math.pow(v,2))),
    CIRC_OUT(v->Math.sqrt(1-Math.pow(v-1,2))),
    CIRC_IN_OUT(v->v<0.5?(1-Math.sqrt(1-Math.pow(2*v,2)))/2:(Math.sqrt(1-Math.pow(-2*v+2,2))+1)/2),
    ;

    private Function<Double, Double> func;
    Easing(Function<Double, Double> func) { this.func=func; }

    public static Easing fromId(int easing) {
        return switch (easing){
            case 1->QUAD_IN;
            case 2->QUAD_OUT;
            case 3->QUAD_IN_OUT;
            case 4->CUBIC_IN;
            case 5->CUBIC_OUT;
            case 6->CUBIC_IN_OUT;
            case 7->QUART_IN;
            case 8->QUART_OUT;
            case 9->QUART_IN_OUT;
            case 10->SINE_IN;
            case 11->SINE_OUT;
            case 12->SINE_IN_OUT;
            case 13->EXPO_IN;
            case 14->EXPO_OUT;
            case 15->EXPO_IN_OUT;
            case 16->CIRC_IN;
            case 17->CIRC_OUT;
            case 18->CIRC_IN_OUT;
            default->LINEAR;
        };
    }
    public static int toId(Easing easing) {
        return switch (easing){
            case QUAD_IN->1;
            case QUAD_OUT->2;
            case QUAD_IN_OUT->3;
            case CUBIC_IN->4;
            case CUBIC_OUT->5;
            case CUBIC_IN_OUT->6;
            case QUART_IN->7;
            case QUART_OUT->8;
            case QUART_IN_OUT->9;
            case SINE_IN->10;
            case SINE_OUT->11;
            case SINE_IN_OUT->12;
            case EXPO_IN->13;
            case EXPO_OUT->14;
            case EXPO_IN_OUT->15;
            case CIRC_IN->16;
            case CIRC_OUT->17;
            case CIRC_IN_OUT->18;
            default->0;
        };
    }

    public double apply(double val) { return func.apply(Math.clamp(val,-1,1)); }
}
