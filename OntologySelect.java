package test;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class OntologySelect {

    /**
     * @author Yiwei
     */


    public static void ontologySelect(String ontoInputRoot, String ontoSaveRoot, String ontoDataPath)
            throws OWLOntologyCreationException, CloneNotSupportedException, OWLOntologyStorageException, IOException {
        String corpus = "";//  [0,1000);[1000,5000);[5000,10000]
        //System.setOut(new PrintStream(new File("C:\\Users\\DELL\\Desktop\\毕设\\code\\OntoLDiff-master\\Output\\thesisDataOutput\\outLog.txt")));

        //Scanner sc1 = new Scanner(System.in);
        //System.out.println("Test File Path: ");
        String filePath1 = ontoInputRoot;

        ArrayList<String> ontoFilePaths = new ArrayList<String>();
        try {
            //readfile("C:/Users/DELL/Documents/test_data/BioPortal_Ontologies");
            ontoFilePaths = ReadFiles.readfile(filePath1);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        System.out.println("Ontology Files Founded");

        //Scanner sc2 = new Scanner(System.in);
        //System.out.println("Save Path: ");

        ///C:/Users/DELL/Desktop/毕设/code/OntoLDiff-master/
        //String ontosavePath = "C:\\Users\\DELL\\Desktop\\毕设\\code\\OntoLDiff-master\\Result\\OntoSelected";
        String ontosavePath=ontoSaveRoot;
        String ontoabsPathJV = ontosavePath.replaceAll("\\\\", "/");

        Integer ontoNum = ontoFilePaths.size(); //TODO : change to ontoFilePaths
        for (int i = 0; i < ontoNum; i++) {


            OWLOntologyManager managerCurr = OWLManager.createOWLOntologyManager();

            //get the name of each onto
            String filePathCurr = ontoFilePaths.get(i);
            String[] tmps = filePathCurr.split("/");
            String ontoName = tmps[tmps.length - 1];
            System.out.println(ontoName);

            //if (ontoName[0]=='c')
            IRI iriCurr = IRI.create(filePathCurr);
            OWLOntology ontoCurr = managerCurr.loadOntologyFromOntologyDocument(new IRIDocumentSource(iriCurr),
                    new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(false));
            //Integer ontoSize = ontoCurr.getLogicalAxiomCount();
            //System.out.println(ontoSize);
            Integer ontoSize = ontoCurr.getTBoxAxioms(true).size();
            //System.out.println(tmp);
            Integer cSigSize = ontoCurr.getClassesInSignature().size();//1
            //System.out.println(cSigSize);
            Integer rSigSize = ontoCurr.getObjectPropertiesInSignature().size();//2
            Integer iSigSize = ontoCurr.getIndividualsInSignature().size();

            //String logPath = "C:\\Users\\DELL\\Desktop\\毕设\\code\\OntoLDiff-master\\Result\\OntoData";
            String logPath=ontoDataPath;

            if (ontoSize >= 10 && ontoSize < 1000) {
                corpus = "corpus1";
            } else if (ontoSize >= 1000 && ontoSize < 5000) {
                corpus = "corpus2";
            } else if (ontoSize >= 5000 && ontoSize <= 10000) {
                corpus = "corpus3";
            } else {
                corpus = "corpus4";
            }

            ReadFiles.appendLine(logPath + "\\ontodata_" + corpus + ".csv", ontoName + ',' + ontoSize.toString() + ',' + cSigSize.toString() + ',' + rSigSize.toString() + ',' + iSigSize.toString());

            OutputStream os;
            try {
                os = new FileOutputStream(new File(ontoabsPathJV + "\\" + corpus + "\\" + ontoName));
                managerCurr.saveOntology(ontoCurr, new OWLXMLOntologyFormat(), os);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OWLOntologyStorageException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public static void crSelect(String filePath1, String savePath,String corpus, String flag, Double percent)
            throws OWLOntologyCreationException, CloneNotSupportedException, OWLOntologyStorageException, IOException{
        //double [] percents={0.1,0.3};

        //Scanner sc1 = new Scanner(System.in);
        //System.out.println("Input File Path: ");
        //String filePath1 = sc1.next();
        filePath1=filePath1+"\\"+corpus;
        ArrayList<String> ontoFilePaths=new ArrayList<String>();
        try {
            //readfile("C:/Users/DELL/Documents/test_data/BioPortal_Ontologies");
            ontoFilePaths= ReadFiles.readfile(filePath1);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        System.out.println("Ontology Files Founded");

//        Scanner sc2 = new Scanner(System.in);
//        System.out.println("Save Root: ");
//        String savePath = sc2.next();
        String absPathJV= savePath.replaceAll("\\\\","/");


        Integer ontoNum=ontoFilePaths.size(); //TODO : change to ontoFilePaths
        for (int i =0 ; i< ontoNum ; i++) {

            OWLOntologyManager managerCurr = OWLManager.createOWLOntologyManager();

            //get the name of each onto
            String filePathCurr = ontoFilePaths.get(i);
            String[] tmps = filePathCurr.split("/");
            String ontoName = tmps[tmps.length - 1];

            IRI iriCurr = IRI.create(filePathCurr);
            OWLOntology ontoCurr = managerCurr.loadOntologyFromOntologyDocument(new IRIDocumentSource(iriCurr),
                    new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(false));
            Integer ontoSize = ontoCurr.getLogicalAxiomCount();
            Integer cSigSize = ontoCurr.getClassesInSignature().size();//1
            Integer rSigSize = ontoCurr.getObjectPropertiesInSignature().size();//2
            Integer iSigSize = ontoCurr.getIndividualsInSignature().size();

            System.out.println(ontoName);

            //randomly select n entities to be forgetten
            Set<OWLClass> cSig = new HashSet<>(ontoCurr.getClassesInSignature());
            Set<OWLObjectProperty> rSig = new HashSet<>(ontoCurr.getObjectPropertiesInSignature());


            List<OWLClass> cSigL = new ArrayList<OWLClass>(cSig);
            Collections.shuffle(cSigL);
            int cForgetNum = (int) (percent * cSigSize);
            // TODO: change the number of limit
            List<OWLClass> cTmpL = cSigL.stream().limit(cForgetNum).collect(Collectors.toList());
            String conceptPath=absPathJV+"\\Concepts\\"+corpus+"_"+flag+"_"+String.valueOf(percent)+ontoName+".concepts";
            for (OWLClass concept:cTmpL){
                String cIRI=concept.getIRI().toString();
                ReadFiles.appendLine(conceptPath,cIRI);
            }

            //role names shuffle
            List<OWLObjectProperty> rSigL = new ArrayList<OWLObjectProperty>(rSig);
            Collections.shuffle(rSigL);
            int rForgetNum = (int) (percent * rSigSize);
            List<OWLObjectProperty> rTmpL = rSigL.stream().limit(rForgetNum).collect(Collectors.toList());
            rSig.clear();
            String rolePath=absPathJV+"\\Roles\\"+corpus+"_"+flag+"_"+String.valueOf(percent)+ontoName+".roles";
            for (OWLClass concept:cTmpL){
                String cIRI=concept.getIRI().toString();
                ReadFiles.appendLine(rolePath,cIRI);
            }

        }
    }

    public static void main(String[] args)
            throws OWLOntologyCreationException, CloneNotSupportedException, OWLOntologyStorageException, IOException
    {
            String ontoInputRoot="C:/Users/DELL/Documents/test_data/BioPortal_Ontologies";
            String ontoSaveRoot=  "C:\\Users\\DELL\\Desktop\\毕设\\code\\OntoLDiff-master\\Result\\OntoSelected";
            String ontoDataPath= "C:\\Users\\DELL\\Desktop\\毕设\\code\\OntoLDiff-master\\Result\\OntoData";
            ontologySelect(ontoInputRoot, ontoSaveRoot, ontoDataPath);

            //Double forgetPercent = 0.05;
            String crPath="C:\\Users\\DELL\\Desktop\\毕设\\code\\OntoLDiff-master\\Result\\CRselected";
            String[] corpus={"corpus1","corpus2","corpus3"};
            String[] flag={"1"};
            Double[] percent={0.1,0.3};
            String inputPath="C:\\Users\\DELL\\Desktop\\毕设\\code\\OntoLDiff-master\\Result\\OntoSelected";

            for (String crps:corpus){
                for(String flg :flag){
                    for(Double pct :percent){
                        crSelect(inputPath,crPath,crps,flg,pct);
                    }
                }
            }



    }


}

//input： C:/Users/DELL/Documents/test_data/BioPortal_Ontologies/Part1
//Save:  C:\Users\DELL\Desktop\毕设\code\OntoLDiff-master\Output\thesisDataOutput

