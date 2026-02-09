import java.io.Serializable;

public class Result implements Serializable {
    private Double risultato;

    private String errorDescription;

    private Boolean hasError;

    public static final String DIVISION_BY_ZERO_ERROR = "impossibile dividere per zero";

    public static final String UNKNOWN_OPERATION_ERROR = "operazione sconosciuta";

    public static final String SUCCESS = "esecuzione avvenuta con successo";

    public static final String GENERIC_ERROR = "errore generico";

    public Result() {
        this.hasError = false;
        this.errorDescription = Result.SUCCESS;
    }

    public Double getRisultato() {
        return this.risultato;
    }

    public void setRisultato(Double risultato) {
        this.risultato = risultato;
    }

    public Boolean getHasError() {
        return this.hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
