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
public class ga {
    public int value;
    public   ga(int n,int which,int v[][]) throws Exception {
        Configuration conf = new DefaultConfiguration();
        conf.setPreservFittestIndividual(true);
        FitnessFunction myfunction = new fit(v);
        conf.setFitnessFunction(myfunction);
        int c = 60;
        Gene[] sampleGene = new Gene[n];
        for (int i = 0; i < n; i++) {
            sampleGene[i] = new IntegerGene(conf, 6, c - 6);
        }
        IChromosome samplechromosome = new Chromosome(conf, sampleGene);
        conf.setSampleChromosome(samplechromosome);
        conf.setPopulationSize(300);
        Genotype popution;
        popution = Genotype.randomInitialGenotype(conf);
        for (int i = 0; i < 100; i++) {
            popution.evolve();
        }

        IChromosome bestSolutionSoFar = popution.getFittestChromosome();

        value= fit.getValueAtGene(bestSolutionSoFar,which);
    }
}
