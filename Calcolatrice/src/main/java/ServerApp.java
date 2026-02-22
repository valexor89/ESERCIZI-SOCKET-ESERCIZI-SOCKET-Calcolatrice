import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {

    public static void main(String[] args) throws Exception {
        System.out.println("Server started...");

        Socket client = new Socket();

        CalculationDTO calculation = new CalculationDTO();

        while (true) {
            ServerSocket socket = new ServerSocket(8080);

            client = socket.accept();
            // input stream di ricezione dei dati dal client
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            calculation = (CalculationDTO) in.readObject();

            calculation.setResult(ServerApp.executeOperation(calculation.getOperazione(), calculation.getOperando1(),
                    calculation.getOperando2()));

            // output stream di invio del risultato al client
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

            out.writeObject(calculation);

            client.close();
            socket.close();
        }

    }

    /**
     * metodo che esegue l'operazione richiesta dal client, prende in input
     * l'operazione da eseguire e i due operandi,
     * restituisce il risultato dell'operazione
     * 
     * @param operazione
     * @param operando1
     * @param operando2
     * @return
     */
    private static Result executeOperation(Character operazione, Double operando1, Double operando2) {
        Result result = new Result();
        switch (operazione) {
            case '+':
                result.setRisultato(operando1 + operando2);
                break;
            case '-':
                result.setRisultato(operando1 - operando2);
                break;
            case '*':
                result.setRisultato(operando1 * operando2);
                break;
            case '/':
                // per i double non viene rilanciata una eccezione, bisogna controllare
                // manualmente
                if (operando2 == 0.0) {
                    System.out.println(Result.DIVISION_BY_ZERO_ERROR);
                    result.setErrorDescription(Result.DIVISION_BY_ZERO_ERROR);
                    result.setHasError(true);
                    result.setRisultato(0.0);
                } else {
                    result.setRisultato(operando1 / operando2);
                }
                break;
            default:
                throw new IllegalArgumentException(Result.UNKNOWN_OPERATION_ERROR);
        }
        ;

        return result;
    }

}
