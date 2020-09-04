package test;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.*;

import forgetting.Forgetter;


public class RCForget {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static void forgetRC(String corpus, Double percent, String ontoInPath, String flag, String ontoOutPath, String conceptsInPath, String rolesInPath,String dataLogPath,String status)
            throws OWLOntologyCreationException, IOException, CloneNotSupportedException, OWLOntologyStorageException {

        ArrayList<String> ontoFilePaths = new ArrayList<String>();

        try {
            //readfile("C:/Users/DELL/Documents/test_data/BioPortal_Ontologies");
            ontoFilePaths = ReadFiles.readfile(ontoInPath + "\\" + corpus+"\\" + status);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }

        System.out.println("Ontology Files Founded in" + ontoInPath);

        System.out.println("-----" + flag + "_" + Double.toString(percent) + "_" + corpus);
        Integer ontoNum = ontoFilePaths.size();

        //log
        String logPath=dataLogPath+"\\" + corpus + "_" + flag + "_" + String.valueOf(percent)+".csv";
        ReadFiles.clearInfoForFile(logPath);

        //output
        String outputDir=ontoOutPath + "\\" + corpus + "\\" + Double.toString(percent)
                + "\\" + flag ;//;
        ReadFiles.mkDirectory(outputDir);
        ReadFiles.deleteDir(outputDir);



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
                    Integer iSigSize = o.getIndividualsInSignature().size();

                    File cFile;
                    File rFile;
                    String line_;
                    BufferedReader creader;
                    BufferedReader rreader;
//                    String cFileName = conceptsInPath + "\\" + corpus + "_" + flag + "_" + String.valueOf(percent) + ontoName + ".concepts";
//                    String rFileName = rolesInPath + "\\" + corpus + "_" + flag + "_" + String.valueOf(percent) + ontoName + ".roles";

                    String cFileName= conceptsInPath+"\\"+String.valueOf(percent)+"_"+flag+"_"+ontoName+".concepts";
                    String rFileName= conceptsInPath+"\\"+String.valueOf(percent)+"_"+flag+"_"+ontoName+".roles";
                    OWLDataFactory factory = managerCurr.getOWLDataFactory();
                    cFile = new File(cFileName);
                    rFile = new File(rFileName);
                    Set<OWLClass> sig_c = new HashSet<OWLClass>();
                    Set<OWLObjectProperty> sig_r = new HashSet<OWLObjectProperty>();

                    if (cFile.exists()) {
                        creader = new BufferedReader(new FileReader(cFile));
                        List<OWLClass> concepts_lst = new ArrayList<OWLClass>(o.getClassesInSignature());

                        line_ = creader.readLine();

                        while (line_ != null) {
                            sig_c.add(factory.getOWLClass(IRI.create(line_)));
                            // System.out.println("concept:"+concepts_lst.get(Integer.parseInt(line_.trim())));
                            line_ = creader.readLine();
                        }
                    }
                    if (rFile.exists()) {
                        rreader = new BufferedReader(new FileReader(rFile));

                        List<OWLObjectProperty> roles_lst = new ArrayList<OWLObjectProperty>(o.getObjectPropertiesInSignature());

                        line_ = rreader.readLine();
                        while (line_ != null) {
                            sig_r.add(factory.getOWLObjectProperty(IRI.create(line_)));
                            // System.out.println("roles:"+roles_lst.get(Integer.parseInt(line_.trim())));
                            line_ = rreader.readLine();
                        }
                    }

                    Forgetter f = new Forgetter();


                    OutputStream os = new FileOutputStream(
                            new File(outputDir+ "\\" + ontoName));


                    Runtime r = Runtime.getRuntime();
                    r.gc();
                    long startMem=r.freeMemory();
                    long begin= System.currentTimeMillis();

                    OWLOntology o_ = f.Forgetting(sig_c, sig_r, o);
                    managerCurr.saveOntology(o_, new OWLXMLOntologyFormat(), os);

                    //Date date1= new Date(System.currentTimeMillis());
                    //String endTime = sdf.format(date);
                    long endMem=r.freeMemory();
                    long end=System.currentTimeMillis();
                    String memCost = String.valueOf((startMem-endMem)/1024);

                    long executionTime =end-begin;
                    String content=ontoName+","+ontoSize+","+cSigSize+","+rSigSize+","+corpus+","+flag+","+percent.toString()+","+ executionTime+","+memCost;
                    out.write(content + "\n");
                    out.close();
                }
        }


    }


    public static void main(String[] args) throws Exception {

        String[] corpusL = {"corpus1"};//, "corpus2", "corpus3"};
        Double[] percentL = {0.1,0.3};//, 0.3};
        String[] flagL={"1"};
        String ontoInputPath = "E:\\dyw\\Test2\\Result\\OntoSelected";
        String conceptsInputPath = "E:\\dyw\\Test2\\Result\\CRselected\\Concepts";
        String rolesInputPath = "E:\\dyw\\Test2\\Result\\CRselected\\Roles";
        String ontoOutPath="E:\\dyw\\Test2\\Result\\OntoForgetted";
        String dataLogPath="E:\\dyw\\Data\\Result\\OntoData\\forgetLog ";

        for(String cps:corpusL)
            for(Double pct:percentL)
                for(String flag:flagL)
                    forgetRC(cps,pct,ontoInputPath,flag,ontoOutPath,conceptsInputPath,rolesInputPath,dataLogPath,"waiting");

    }


}
