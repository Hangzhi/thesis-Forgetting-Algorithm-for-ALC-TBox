package test;

import org.apache.commons.io.output.TeeOutputStream;
import org.semanticweb.owlapi.io.OntologyIRIMappingNotFoundException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class TestDriver {
    //Ontology Select



    public static void ontoSelect() throws OWLOntologyStorageException, IOException, CloneNotSupportedException, OWLOntologyCreationException {
        //String ontoInputRoot="E:\\dyw\\Data\\BioPortal_Ontologies";
        String ontoInputRoot= "E:\\dyw\\Data\\Result\\OntoSelected\\corpus5";
        String ontoSaveRoot=  "E:\\dyw\\Data\\Result\\OntoSelected";
        String ontoDataPath= "E:\\dyw\\Data\\Result\\OntoData";
        OntologySelect.ontologySelect(ontoInputRoot, ontoSaveRoot, ontoDataPath);
    }

    //CRselect

    public static void crSelect() throws OWLOntologyStorageException, IOException, CloneNotSupportedException, OWLOntologyCreationException {

        String crOutPath="E:\\dyw\\Data\\Result\\CRselected";
        String[] corpus1={"corpus1","corpus2","corpus3"};
        String[] flag1={"1","2","3"};
        Double[] percent1={0.1,0.3};
        String selectedOntoinputPath="E:\\dyw\\Data\\Result\\OntoSelected";

        for (String crps:corpus1){
            for(String flg :flag1){
                for(Double pct :percent1){
                    OntologySelect.crSelect(selectedOntoinputPath,crOutPath,crps,flg,pct);
                }
            }
        }

    }
    //CRForget


    public static void crForget() throws OWLOntologyStorageException, IOException, CloneNotSupportedException, OWLOntologyCreationException {




        String[] corpus2 = {"corpus3"};//, "corpus2", "corpus3"};
        Double[] percent2 = {0.1};//, 0.3};
        String[] flag2={"1"};
        String ontoInputPath = "D:\\Senior\\thesis\\DataPrep\\OntoSelected";
        String conceptsInputPath = "D:\\Senior\\thesis\\DataPrep\\CRselected";
        String rolesInputPath = "D:\\Senior\\thesis\\DataPrep\\CRselected";
        String ontoOutPath="D:\\Senior\\thesis\\DataPrep\\OntoForgetted";
        String dataLogPath="D:\\Senior\\thesis\\DataPrep\\OntoData\\forgetLog";
        String status ="fine";
        for(String cps:corpus2)
            for(Double pct:percent2)
                for(String flag:flag2)
                    RCForget.forgetRC(cps,pct,ontoInputPath,flag,ontoOutPath,conceptsInputPath,rolesInputPath,dataLogPath,status);

    }

    //SuccessTest

    //mainDriver


    public static void main(String[] args) throws OWLOntologyStorageException, IOException, CloneNotSupportedException, OWLOntologyCreationException {


        //Log all the things in Console
        FileOutputStream fos = new FileOutputStream("D:\\Senior\\thesis\\DataPrep\\Log\\runLog\\" + System.currentTimeMillis() + "out_log.txt");
        TeeOutputStream myOut = new TeeOutputStream(System.out, fos);
        PrintStream ps = new PrintStream(myOut, true);
        System.setOut(ps);
        //Root1 String tmp="E:\\dyw\\Data\\Result\\OntoSelected";
        //Ontology Select

        //ontoSelect();

        //CRselect

        //crSelect();

        //CRForget
        crForget();

        //SuccessTest

        //mainDriver

    }


}
