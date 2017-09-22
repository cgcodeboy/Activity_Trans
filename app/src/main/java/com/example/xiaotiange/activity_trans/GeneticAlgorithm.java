package com.example.xiaotiange.activity_trans;

import android.util.Log;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
/**
 * Created by 仇雨辰 on 2017/9/5.
 */
class GeneticAlgorithm {

    private static String TAG = "algorithm";

    private int value;

    public  GeneticAlgorithm(int scale,int which,int v[][],Configuration conf) throws Exception {
        Configuration.reset();
        conf.setPreservFittestIndividual(true);
        FitnessFunction myfunction = new Fit(v);
        conf.setFitnessFunction(myfunction);
        conf.setPopulationSize(80);

        int c = 60;
        Gene[] sampleGene = new Gene[scale];
        for (int i = 0; i < scale; i++) {
            sampleGene[i] = new IntegerGene(conf, 10, c - 10);
        }

        IChromosome samplechromosome = new Chromosome(conf, sampleGene);
        conf.setSampleChromosome(samplechromosome);

        Genotype popution;
        popution = Genotype.randomInitialGenotype(conf);
        for (int i = 0; i < 20; i++) {
            popution.evolve();
        }

        IChromosome bestSolutionSoFar = popution.getFittestChromosome();

        value= Fit.getValueAtGene(bestSolutionSoFar,which);
    }

    public int getValue() {
        return value;
    }
}
