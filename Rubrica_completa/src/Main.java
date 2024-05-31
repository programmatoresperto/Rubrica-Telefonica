import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static tools.utility.Wait;
import static tools.utility.menu;

public class Main {
    public static void main(String[] args) {
        // Definizione del menu delle operazioni disponibili
        String[] operazioni = {"RUBRICA",
                "[1] Inserimento",
                "[2] Visualizzazione",
                "[3] Ricerca",
                "[4] Ricerca Numero telefonico",
                "[5] Modifica contatto",
                "[6] cancellazione",
                "[7] Riservato",
                "[8] Carica saldo telefonico",
                "[9] Telefona",
                "[10] Ordina (Selection sort, insertion sort, bubble sort)",
                "[11] Salva file",
                "[12] Carica file",
                "[13] Fine"};

        boolean Sitel = true;  // Variabile di controllo per la lettura dei dati
        boolean occultato=true; //variabile di controllo per la lettura dei dati
        final int nMax = 100;    // Numero massimo di contatti gestiti
        int contattiInseriti = 0; // Contatore dei contatti venduti
        int posizione = 0; // Variabile per tenere traccia della posizione dell'array
        Contatto[] rubrica = new Contatto[nMax]; // Array di contatti

        Scanner keyboard = new Scanner(System.in); // Scanner per la lettura degli input

        boolean fine = true; // Variabile di controllo del ciclo principale
        String password = Password(keyboard);
        do {
            // Switch case per la gestione delle diverse opzioni del menu
            switch (menu(operazioni, keyboard)) {
                case 1:
                    // Inserimento di un nuovo contatto
                    if (contattiInseriti < nMax) {
                        rubrica[contattiInseriti] = leggiPersona(Sitel, occultato, keyboard);
                        contattiInseriti++;
                    } else {
                        System.out.println("Non ci sono più contratti da vendere");
                        Wait(2);
                    }
                    break;
                case 2: {
                    // Visualizzazione dei contatti
                    if (contattiInseriti != 0) {
                        visualizza(rubrica, contattiInseriti);
                        Wait(2);
                    } else {
                        System.out.println("Non ci sono contratti\n");
                        Wait(2);
                    }
                    break;
                }

                case 3: {
                    // Ricerca di un contatto
                    if (contattiInseriti != 0) {
                        if (ricerca(rubrica, leggiPersona(false, false, keyboard), contattiInseriti)) {
                            System.out.println("Il contatto esiste");
                            Wait(2);
                        } else {
                            System.out.println("Il contatto non esiste");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;
                }

                case 4:
                    // Ricerca e visualizzazione del numero telefonico di un contatto
                    if (contattiInseriti != 0) {
                        posizione = RicercaIndex(rubrica, leggiPersona(false, false, keyboard), contattiInseriti);
                        if (posizione != -1) {
                            System.out.println(rubrica[posizione].cognome + " " + rubrica[posizione].nome + ": " + rubrica[posizione].telefono);
                            Wait(2);
                        } else {
                            System.out.println("Contatto inesistente");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;

                case 5:
                    // Modifica del numero telefonico di un contatto
                    Contatto numero = new Contatto();
                    int scelta;
                    if (contattiInseriti != 0) {
                        posizione = RicercaIndex(rubrica, leggiPersona(false, false, keyboard), contattiInseriti);
                        if (posizione != -1) {
                            System.out.println("Vuoi modificare il numero telefonico (si = 1 | no = 0): ");
                            scelta = keyboard.nextInt();
                            keyboard.nextLine();
                            if (scelta == 1) {
                                System.out.println("Modifica numero telefonico: ");
                                numero.telefono = keyboard.nextLine();
                                rubrica[posizione].telefono = numero.telefono;
                            } else {
                                System.out.println("Numero telefonico non modificato");
                                Wait(2);
                            }
                        } else {
                            System.out.println("Contatto inesistente");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;
                case 6:
                    // Cancellazione di un contatto
                    if (contattiInseriti != 0) {
                        posizione = RicercaIndex(rubrica, leggiPersona(false, false, keyboard), contattiInseriti);
                        if (posizione != -1) {
                            contattiInseriti = cancellazione(rubrica, posizione, contattiInseriti);
                        } else {
                            System.out.println("Contatto inesistente");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;

                case 7:
                    // Accesso ai contatti riservati se è stata impostata una password
                    if (password != null) {
                        Riservato(keyboard, password, rubrica, contattiInseriti);
                        Wait(2);
                    }
                    break;

                case 8:
                    // Aggiunta di saldo telefonico a un contatto
                    AggiuntaSaldo(rubrica, keyboard, contattiInseriti);
                    break;
                case 9:
                    // Simulazione di una telefonata
                    Telefona(rubrica, keyboard, contattiInseriti);
                    break;
                case 10:
                    // Ordinamento dei contatti
                    String ordine;
                    System.out.println("Quale algoritmo di ordinamento si vuole usare per ordinare la rubrica? selection, insertion, bubble");
                    ordine = keyboard.nextLine();
                    switch (ordine) {
                        case "selection":
                            Selection(rubrica, contattiInseriti);
                            Wait(2);
                            break;
                        case "insertion":
                            Insertion(rubrica, contattiInseriti);
                            Wait(2);
                            break;
                        case "bubble":
                            Bubble(rubrica, contattiInseriti);
                            Wait(2);
                            break;
                    }
                    break;
                case 11:
                    // Salvataggio dei contatti su file
                    try {
                        ScriviNcontatti("archivio.csv", rubrica, contattiInseriti);
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                        break;
                    }
                    break;
                case 12:
                    // Caricamento dei contatti da file
                    try {
                        //contrattiVenduti = LeggiFile("archivio.csv", gestore, contrattiVenduti);
                        rubrica=LeggiNcontatti("archivio.csv");
                        contattiInseriti = rubrica.length;
                    } catch (IOException ex) {
                        System.out.println(ex);
                        break;
                    }
                    break;
                default:
                    // Uscita dal programma
                    fine = false;
                    break;
            }
        } while (fine);
    }


    // Metodo per impostare la password per l'accesso ai contatti riservati
    private static String Password(Scanner input) {
        String password;
        System.out.println("Tramite questa funzione si possono visualizzare i contatti nascosti");
        System.out.println("Impostare la password");
        password = input.nextLine();
        return password;
    }

    // Funzione per leggere i dati di un contatto
    private static Contatto leggiPersona(boolean Sitel, boolean occultato, Scanner keyboard) {
        String[] tipoC = {"Telefono", "1]abitazione", "2]cellulare", "3]aziendale"};
        Contatto persona = new Contatto();
        System.out.println("\nInserisci il nome: ");
        persona.nome = keyboard.nextLine();
        System.out.println("\nInserisci il cognome: ");
        persona.cognome = keyboard.nextLine();

        if (Sitel) {
            System.out.println("\nInserisci il numero di telefono: ");
            persona.telefono = keyboard.nextLine();
            switch (menu(tipoC, keyboard)) {
                case 1 -> persona.tipo = tipoContratto.abitazione;
                case 2 -> persona.tipo = tipoContratto.cellulare;
                default -> persona.tipo = tipoContratto.aziendale;
            }
        }
        System.out.println("Inserire il saldo attuale");
        persona.saldo = keyboard.nextDouble();
        keyboard.nextLine();

        if(occultato){
            System.out.println("Il contatto inserito deve essere nascosto (si o no)");
            persona.nascosto = keyboard.nextLine();
        }

        return persona;
    }

    // Funzione per visualizzare tutti i contatti
    private static void visualizza(Contatto[] rubrica, int contattiInseriti) {
        for (int i = 0; i < contattiInseriti; i++) {
            if (rubrica[i].nascosto.equals("si")) {
                continue;
            }
            System.out.println(rubrica[i].toString());
        }
    }

    // Funzione per cercare un contatto nell'array dei contatti
    private static boolean ricerca(Contatto[] rubrica, Contatto contatto, int contattiInseriti) {
        boolean ricerca = false;
        for (int i = 0; i < contattiInseriti; i++) {
            if (rubrica[i].nascosto.equals("si")) {
                continue;
            }
            if (contatto.nome.equals(rubrica[i].nome) && contatto.cognome.equals(rubrica[i].cognome)) {
                ricerca = true;
                break;
            }
        }
        return ricerca;
    }

    // Funzione per ottenere l'indice di un contatto nell'array
    private static int RicercaIndex(Contatto[] rubrica, Contatto ricerca, int contattiInseriti) {
        int indice = -1;
        for (int i = 0; i < contattiInseriti; i++) {
            if (rubrica[i].nascosto.equals("si")) {
                continue;
            }
            if (ricerca.nome.equals(rubrica[i].nome) && ricerca.cognome.equals(rubrica[i].cognome)) {
                indice = i;
                break;
            }
        }
        return indice;
    }



    // Funzione per cancellare un contatto dall'array
    public static int cancellazione(Contatto[] rubrica, int posizione, int contattiInseriti) {
        if (posizione != rubrica.length - 1) {
            for (int i = posizione; i < contattiInseriti - 1; i++) {
                if (rubrica[i].nascosto.equals("si")) {
                    continue;
                }
                rubrica[i] = rubrica[i + 1];
            }
        }
        contattiInseriti--;
        return contattiInseriti;
    }
    // Metodo per accedere ai contatti riservati
    private static void Riservato(Scanner input, String password, Contatto[] rubrica, int contattiInseriti) {
        String tentativo;
        System.out.println("Inserire la password");
        tentativo = input.nextLine();
        // Se la password è corretta, visualizza tutti i contatti
        if (tentativo.equals(password)) {
            for (int i = 0; i < contattiInseriti; i++) {
                System.out.println(rubrica[i].stampa());
            }
        } else {
            // Se la password è errata, termina il programma
            System.out.println("Password errata");
            System.out.println("Spegnimento in corso");
            Wait(3);
            Protezione();
        }
    }

    // Metodo per terminare il programma
    private static void Protezione()
    {
        System.exit(0);
    }

    // Funzione per aggiungere saldo a un contatto
    public static void AggiuntaSaldo(Contatto[] rubrica, Scanner keyboard, int contattiInseriti) {
        Contatto ricarica = new Contatto();
        int posizione;
        if (contattiInseriti != 0) {
            posizione = RicercaIndex(rubrica, leggiPersona(false, false, keyboard), contattiInseriti);
            if (posizione != -1) {
                System.out.println("Inserire la ricarica");
                ricarica.saldo = Integer.parseInt(keyboard.nextLine());
                rubrica[posizione].saldo += ricarica.saldo;
            } else {
                System.out.println("Contatto non trovato");
                Wait(2);
            }
        } else {
            System.out.println("Non sono ancora presenti contratti venduti");
            Wait(2);
        }
    }

    // Funzione per simulare una telefonata
    public static void Telefona(Contatto[] rubrica, Scanner keyboard, int contattiInseriti) {
        int posizione;
        if (contattiInseriti != 0) {
            posizione = RicercaIndex(rubrica, leggiPersona(false, false, keyboard), contattiInseriti);
            if (posizione != -1) {
                System.out.println("Telefonata in corso");
                Wait(1);
                System.out.println(".");
                Wait(1);
                System.out.println(".");
                Wait(1);
                System.out.println(".");
                Wait(1);
                System.out.println("Telefonata terminata");
                Wait(2);
                rubrica[posizione].saldo--;
            } else {
                System.out.println("Contatto non trovato");
                Wait(2);
            }
        } else {
            System.out.println("Non sono ancora presenti contratti venduti");
            Wait(2);
        }
    }

    //Funzione per ordinare l'algoritmo tramite selection sort
    private static void Selection(Contatto[] rubrica, int contattiInseriti) {
        for (int i = 0; i < contattiInseriti - 1; i++) {
            int minore = i;
            for (int j = i + 1; j < contattiInseriti; j++) {
                if (rubrica[j].cognome.compareToIgnoreCase(rubrica[minore].cognome) < 0) {
                    minore = j;
                } else if (rubrica[j].cognome.compareToIgnoreCase(rubrica[j - 1].cognome) == 0) {
                    if (rubrica[j].nome.compareToIgnoreCase(rubrica[j - 1].nome) < 0) {
                        minore = i;
                    }
                }
            }
            Swap(rubrica, i, minore);
        }
        visualizza(rubrica, contattiInseriti);
    }

    //Funzione per eseguire lo swap
    private static void Swap(Contatto[] rubrica, int i, int j) {
        Contatto temp = rubrica[i];
        rubrica[i] = rubrica[j];
        rubrica[j] = temp;
    }

    //Funzione per ordinare l'algoritmo tramite insertion sort
    private static void Insertion(Contatto[] rubrica, int contattiInseriti) {
        for (int i = 1; i < contattiInseriti; ++i) {
            Contatto riferimento = rubrica[i];
            int j = i - 1;
            if (j >= 0 && rubrica[j].cognome.compareToIgnoreCase(riferimento.cognome) > 0) {
                while (j >= 0 && rubrica[j].cognome.compareToIgnoreCase(riferimento.cognome) > 0) {
                    rubrica[j + 1] = rubrica[j];
                    j--;
                }
            } else if (j >= 0 && rubrica[j].cognome.compareToIgnoreCase(riferimento.cognome) == 0) {
                if (j >= 0 && rubrica[j].nome.compareToIgnoreCase(riferimento.nome) > 0) {
                    while (j >= 0 && rubrica[j].nome.compareToIgnoreCase(riferimento.nome) > 0) {
                        rubrica[j + 1] = rubrica[j];
                        j--;
                    }
                }

            }
            rubrica[j + 1] = riferimento;
        }
        visualizza(rubrica, contattiInseriti);
    }

    //Funzione per ordinare l'algoritmo tramite bubble sort
    private static void Bubble(Contatto[] rubrica, int contattiInseriti) {
        for (int i = 0; i < contattiInseriti; i++) {
            for (int j = 1; j < contattiInseriti - i; j++) {
                if (rubrica[j].cognome.compareToIgnoreCase(rubrica[j - 1].cognome) < 0) {
                    Swap(rubrica, i, j);
                } else if (rubrica[j].cognome.compareToIgnoreCase(rubrica[j - 1].cognome) == 0) {
                    if (rubrica[j].nome.compareToIgnoreCase(rubrica[j - 1].nome) < 0) {
                        Swap(rubrica, i, j);
                    }
                }
            }
        }
        visualizza(rubrica, contattiInseriti);
    }

    //Funzione per salvare i dati e il numero di contatti inseriti su file
    private static void ScriviNcontatti(String fileName, Contatto[] gestore, int contrattiVenduti) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(contrattiVenduti+"\r\n");
        for (int i = 0; i < contrattiVenduti; i++) {
            writer.write(gestore[i].toString() + "\r\n");
        }
        writer.flush();
        writer.close();

    }

    //Funzione per importare i dati e il numero di contatti inseriti da file
    private static Contatto[] LeggiNcontatti(String fileName) throws IOException {
        FileReader reader = new FileReader(fileName);
        Scanner input = new Scanner(reader);
        String lineIn;
        String[] vetAttributi;
        int contaElementi = 0;
        Contatto rubrica2[]=new Contatto[Integer.parseInt(input.nextLine())];
        while (input.hasNextLine()) {
            lineIn = input.nextLine();
            vetAttributi = lineIn.split(",");
            Contatto persona = new Contatto();
            persona.nome = vetAttributi[0];
            persona.cognome = vetAttributi[1];
            persona.telefono = vetAttributi[2];
            switch (vetAttributi[3]) {
                case "abitazione":
                    persona.tipo = tipoContratto.abitazione;
                    break;
                case "cellulare":
                    persona.tipo = tipoContratto.cellulare;
                    break;
                case "aziendale":
                    persona.tipo = tipoContratto.aziendale;
                    break;
            }
            persona.saldo = Double.parseDouble(vetAttributi[4]);
            rubrica2[contaElementi] = persona;
            contaElementi++;
        }
        return rubrica2;
    }
}