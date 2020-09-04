package test;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.util.ArrayList;

public class owlSummary {

    public static void main(String[] args) throws OWLOntologyStorageException, IOException, CloneNotSupportedException, OWLOntologyCreationException {
        String rootPath="D:\\Senior\\thesis\\xuanWu\\DATA\\";
        String [] corpus={"corpus1_","corpus2_","corpus3_"};

        for (String c: corpus){
        ArrayList<String> ontoFilePaths = new ArrayList<String>();
        try {
            //readfile("C:/Users/DELL/Documents/test_data/BioPortal_Ontologies");
            ontoFilePaths = ReadFiles.readfile( rootPath+ "\\" + c);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }

        String logPath="D:\\Senior\\thesis\\xuanWu\\resultSummary\\owlsum"+"\\" + c.substring(0,7)+".csv";

        //String tmp="D:\\Senior\\thesis\\xuanWu\\resultSummary\\owlsum"
        Integer ontoNum = ontoFilePaths.size();

        for (int i = 0; i < ontoNum; i++) {
            {
                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(logPath, true)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                OWLOntologyManager managerCurr = OWLManager.createOWLOntologyManager();

                //get the name of each onto
                String filePathCurr = ontoFilePaths.get(i);

                String[] tmps = filePathCurr.split("/");
                String ontoName = tmps[tmps.length - 1];
                System.out.println(ontoName);

                IRI iri = IRI.create(filePathCurr);
                OWLOntology o = managerCurr.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri),
                        new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));

                Integer ontoSize = o.getTBoxAxioms(true).size();
                Integer cSigSize = o.getClassesInSignature().size();//1
                Integer rSigSize = o.getObjectPropertiesInSignature().size();//2
                String content=ontoName+","+ontoSize+","+cSigSize+","+rSigSize;//+","+corpus+","+flag+","+percent.toString()+","+ executionTime+","+memCost
                out.write(content + "\n");
                out.close();
            }
        }
        }
    }

    }
