import java.io.Serializable;

/**
 * DTO sta per Data Transfer Object, solitamente rappresenta un oggetto che
 * viene trasferito tra client e server,
 * contiene i dati necessari per eseguire una determinata operazione
 * 
 * @param operazione       operazione da inserire: +, -, *, /
 * @param operando1        operando1 della calcolatrice
 * @param operando2        operando2 della calcolatrice
 * @param risultato        risultato della calcolatrice, viene calcolato dal
 *                         server e
 *                         restituito al client
 * @param errorDescription descrizione dell'errore, viene restituita al client
 *                         in caso di errore
 */

public class CalculationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Character operazione;

    private Double operando1;

    private Double operando2;

    private Result result;

    public Character getOperazione() {
        return this.operazione;
    }

    public void setOperazione(Character operazione) {
        if (operazione != '+' && operazione != '-' && operazione != '*' && operazione != '/') {
            throw new IllegalArgumentException("Operazione non valida");
        }
        this.operazione = operazione;
    }

    public Double getOperando1() {
        return this.operando1;
    }

    public void setOperando1(Double operando1) {
        this.operando1 = operando1;
    }

    public Double getOperando2() {
        return this.operando2;
    }

    public void setOperando2(Double operando2) {
        this.operando2 = operando2;
    };

    public Result getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
