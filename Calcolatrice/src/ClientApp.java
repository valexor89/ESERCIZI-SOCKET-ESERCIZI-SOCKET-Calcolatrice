import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 8080);

        // output stream di invio al server
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        CalculationDTO calculation = ClientApp.prepareCalculation();

        out.writeObject(calculation);

        // input stream di ricezione dal server
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        CalculationDTO response = (CalculationDTO) in.readObject();

        calculation.setResult((Result) response.getResult());

        if (calculation.getResult().getHasError()) {
            System.out.println("Errore: " + calculation.getResult().getErrorDescription());
        } else {
            System.out.println("Risultato: " + calculation.getResult().getRisultato());
        }
        socket.close();

    }

    /**
     * metodo che prepara l'oggetto CalculationDTO da inviare al server, prende in
     * input l'operazione da eseguire e i due operandi,
     * restituisce un oggetto CalculationDTO con i dati inseriti dall'utente
     * 
     * @return un oggetto CalculationDTO con i dati inseriti dall'utente
     */
    private static CalculationDTO prepareCalculation() {
        CalculationDTO calculation = new CalculationDTO();

        Scanner scanner = new Scanner(System.in);

        System.out.print("inserire l'operazione da esegure: +, -, *, /: ");
        char operazione = scanner.next().charAt(0);
        calculation.setOperazione(operazione);

        System.out.print("inserire l'operando 1: ");
        Double op1 = scanner.nextDouble();
        calculation.setOperando1(op1);

        System.out.print("inserire l'operando 2: ");
        Double op2 = scanner.nextDouble();
        calculation.setOperando2(op2);

        scanner.close();
        return calculation;
    }

}
