import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Afvink2Sorteren {

    public static ArrayList<String> regelsbestand = new ArrayList<>();

    public static ArrayList<String> geneIDArray = new ArrayList<>();
    public static ArrayList<String> symbolArray = new ArrayList<>();
    public static ArrayList<String> chromosomeArray = new ArrayList<>();
    public static ArrayList<String> tempArmMapLocArray = new ArrayList<>();

    public static ArrayList<String> armArray = new ArrayList<>();
    public static ArrayList<String> mapLocationArray = new ArrayList<>();

    public static ArrayList<ArrayList<String>> tempArray1 = new ArrayList<>();
    public static ArrayList<String> tempArray2 = new ArrayList<>();

    public static ArrayList<ArrayList<String>> sortedArray = new ArrayList<>();

    public static void main(String[] args) {

        JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = fc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            String bestandslocatie = selectedFile.getAbsolutePath();

            File bestand = new File(bestandslocatie);
            Scanner bestandlezer = null;
            try {
                bestandlezer = new Scanner(bestand);
            } catch (FileNotFoundException e) {
                System.out.println("Bestand is niet gevonden. Voer een nieuw bestand in.");
            }
            while (true)  {
                assert bestandlezer != null;
                if (!bestandlezer.hasNextLine()) break;
                String regel = bestandlezer.nextLine();
                regelsbestand.add(regel);
            }
            regelsbestand.remove(0);

            for (int i = 0; i < regelsbestand.size(); i++) {
                //Waardes na splitten op tabs in array
                //tax-id 0 - GeneID 1 - Symbol 2 - LocusTag	3 - Synonyms 4 - dbXrefs 5 - chromosome 6 - map_location 7 -
                //description 8 - type_of_gene 9 - Symbol_from_nomenclature_authority 10 -
                //Full_name_from_nomenclature_authority 11 - Nomenclature_status 12 - Other_designations 13 -
                //Modification_date 14 - Feature_type 15
                geneIDArray.add(Arrays.toString(new String[]{regelsbestand.get(i).split("\t")[1]}));
                symbolArray.add(Arrays.toString(new String[]{regelsbestand.get(i).split("\t")[2]}));
                chromosomeArray.add(Arrays.toString(new String[]{regelsbestand.get(i).split("\t")[6]}));
                tempArmMapLocArray.add(Arrays.toString(new String[]{regelsbestand.get(i).split("\t")[7]}));

                int pArm = tempArmMapLocArray.get(i).indexOf('p');
                int qArm = tempArmMapLocArray.get(i).indexOf('q');

                if (pArm != -1) {
                    armArray.add("p");
                    mapLocationArray.add(tempArmMapLocArray.get(i).replace("]", "").substring(pArm+1));
                } else if (qArm != -1) {
                    armArray.add("q");
                    mapLocationArray.add(tempArmMapLocArray.get(i).replace("]", "").substring(qArm+1));
                } else {
                    armArray.add("Onbekend");
                    mapLocationArray.add("-");
                }
            }

            tempArray1.add(geneIDArray);
            tempArray1.add(symbolArray);
            tempArray1.add(chromosomeArray);
            tempArray1.add(armArray);
            tempArray1.add(mapLocationArray);

            for (int i = 0; i < geneIDArray.size(); i++) {
                tempArray2.add(geneIDArray.get(i) + " " + symbolArray.get(i) + " " + chromosomeArray.get(i) + " " +
                        armArray.get(i) + " " + mapLocationArray.get(i));
                sortedArray.add(new ArrayList<>());
                sortedArray.get(i).add(Arrays.toString(tempArray2.get(i).split(" ")).replace("[", "").replace("]", ""));
            }

            //Waardes voor nieuwe gene
            String GeneID = "test, ";
            String Symbol = "test, ";
            String Chromosome = "test, ";
            String Arm = "test, ";
            String MapLocation = "test";
            ArrayList<String> Gene = new ArrayList<>();
            Gene.add(GeneID + Symbol + Chromosome + Arm + MapLocation);

            int helft = sortedArray.size() / 2;
            long start1 = System.nanoTime();
            sortedArray.add(helft, Gene);
            long end1 = System.nanoTime();
            System.out.println("Gene toevoegen in midden duurt " + (end1-start1) + " nanoseconden");

            long start2 = System.nanoTime();
            sortedArray.get(10000);
            long end2 = System.nanoTime();
            System.out.println("Gene opzoeken op pos 10000 duurt " + (end2-start2) + " nanoseconden");

            //Gene ID = 10000
            String id = "[10000]";

            long start3 = System.nanoTime();
            for (int i = 0; i < geneIDArray.size(); i++) {
                if (geneIDArray.get(i).equals(id)) {
                    i = geneIDArray.size();
                }
            }
            long end3 = System.nanoTime();
            System.out.println("Gene opzoeken op basis van gene_id duurt " + (end3-start3) + " nanoseconden");

            long start4 = System.nanoTime();
            Collections.sort(sortedArray.get(0));
            long end4 = System.nanoTime();
            System.out.println("Sorteren van de array duurt " + (end4-start4) + " nanoseconden");
        }
    }
}