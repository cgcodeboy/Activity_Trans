package com.example.xiaotiange.activity_trans;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

class Fit extends FitnessFunction {

    public int vv[][]=new int[1000][1000];

    public Fit(int a[][]){
       System.arraycopy(a,0,vv,0,a.length);
   }

    @Override
    protected double evaluate(IChromosome a_subject) {
        int sum=0;
        int c=60;
        int numOfGens=a_subject.size();
        int v[][] = new int[numOfGens][2];

        for(int i=0;i<numOfGens;i++)
            for(int j=0;j<2;j++) {
               // v[i][j] = rand.nextInt(300) + 50;
                  v[i][j] =vv[i][j] ;
            }
        for(int i=0;i<numOfGens;i=i+2) {
            sum += ((Integer) a_subject.getGene(i).getAllele()) * (v[i][0] - v[i][1]) + c * v[i][1];
        }
        return 1/sum;
    }

    public static int getValueAtGene(IChromosome a_potentialSolution,int a_position) {
        Integer value =(Integer) a_potentialSolution.getGene(a_position).getAllele();
        return value.intValue();
    }
}
