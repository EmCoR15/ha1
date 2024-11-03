package htw.berlin.prog2.ha1;

/**
 * Eine Klasse, die das Verhalten des Online Taschenrechners imitiert, welcher auf
 * https://www.online-calculator.com/ aufgerufen werden kann (ohne die Memory-Funktionen)
 * und dessen Bildschirm bis zu zehn Ziffern plus einem Dezimaltrennzeichen darstellen kann.
 * Enthält mit Absicht noch diverse Bugs oder unvollständige Funktionen.
 */
public class Calculator {

    private String screen = "0";

    private double latestValue = 0;

    private String latestOperation = "";

    //Hilft zu erkennen, ob der Bildschirminhalt neu ist oder nicht
    private boolean isNewInput = true;

    /**
     * @return den aktuellen Bildschirminhalt als String
     */
    public String readScreen() {
        return screen;
    }

    /**
     * Empfängt den Wert einer gedrückten Zifferntaste. Da man nur eine Taste auf einmal
     * drücken kann, muss der Wert positiv und einstellig sein und zwischen 0 und 9 liegen.
     * Führt in jedem Fall dazu, dass die gerade gedrückte Ziffer auf dem Bildschirm angezeigt
     * oder rechts an die zuvor gedrückte Ziffer angehängt angezeigt wird. Wird die Taste nach einer Operation gedrückt,
     * wird der Bildschirminhalt zurückgesetzt.
     * @param digit Die Ziffer, deren Taste gedrückt wurde
     */
    public void pressDigitKey(int digit) {
        if(digit > 9 || digit < 0) throw new IllegalArgumentException();

        if(screen.equals("0") || latestValue == Double.parseDouble(screen)) screen = "";

        //Wenn eine Operationstaste gedrückt wurde, wird der Bildschirminhalt zurückgesetzt
        if (isNewInput) {
            screen = "";
            isNewInput = false;
        }

       screen += digit;
    }

    /**
     * Empfängt den Befehl der C- bzw. CE-Taste (Clear bzw. Clear Entry).
     * Einmaliges Drücken der Taste löscht die zuvor eingegebenen Ziffern auf dem Bildschirm,
     * sodass "0" angezeigt wird, jedoch ohne zuvor zwischengespeicherte Werte zu löschen.
     * Wird daraufhin noch einmal die Taste gedrückt, dann werden auch zwischengespeicherte
     * Werte sowie der aktuelle Operationsmodus zurückgesetzt, sodass der Rechner wieder
     * im Ursprungszustand ist.
     */
    public void pressClearKey() {
        screen = "0";
        latestOperation = "";
        latestValue = 0.0;
        isNewInput = true;
    }

    /**
     * Empfängt den Wert einer gedrückten binären Operationstaste, also eine der vier Operationen
     * Addition, Substraktion, Division, oder Multiplikation, welche zwei Operanden benötigen.
     * Beim ersten Drücken der Taste wird der Bildschirminhalt nicht verändert, sondern nur der
     * Rechner in den passenden Operationsmodus versetzt.
     * Beim zweiten Drücken nach Eingabe einer weiteren Zahl wird direkt das aktuelle Zwischenergebnis
     * auf dem Bildschirm angezeigt. Falls hierbei eine Division durch Null auftritt, wird "Error" angezeigt.
     * @param operation "+" für Addition, "-" für Substraktion, "x" für Multiplikation, "/" für Division
     */
    public void pressBinaryOperationKey(String operation)  {
        calculateIntermediateResult();
        latestOperation = operation;
        isNewInput = true;
    }

    /**
     * Empfängt den Wert einer gedrückten unären Operationstaste, also eine der drei Operationen
     * Quadratwurzel, Prozent, Inversion, welche nur einen Operanden benötigen.
     * Beim Drücken der Taste wird direkt die Operation auf den aktuellen Zahlenwert angewendet und
     * der Bildschirminhalt mit dem Ergebnis aktualisiert. Falls hierbei eine Quadratwurzel aus einer
     * negativen Zahl berechnet werden soll, wird "Error" angezeigt.
     * @param operation "√" für Quadratwurzel, "%" für Prozent, "1/x" für Inversion
     */
    public void pressUnaryOperationKey(String operation) {
        double currentValue = Double.parseDouble(screen);
        double result;

        if (operation.equals("√") && currentValue < 0) {
            screen = "Error";
            isNewInput = true;
            return;
        }

        result = switch (operation) {
            case "√" -> Math.sqrt(currentValue);
            case "%" -> currentValue / 100;
            case "1/x" -> 1 / currentValue;
            default -> throw new IllegalArgumentException();
        };
        screen = formatResult(result);
        isNewInput = true;
    }


    /**
     * Empfängt den Befehl der gedrückten Dezimaltrennzeichentaste, im Englischen üblicherweise "."
     * Fügt beim ersten Mal Drücken dem aktuellen Bildschirminhalt das Trennzeichen auf der rechten
     * Seite hinzu und aktualisiert den Bildschirm. Daraufhin eingegebene Zahlen werden rechts vom
     * Trennzeichen angegeben und daher als Dezimalziffern interpretiert.
     * Beim zweimaligem Drücken, oder wenn bereits ein Trennzeichen angezeigt wird, passiert nichts.
     */
    public void pressDotKey() {
        if (!screen.contains(".")) screen += ".";
    }

    /**
     * Empfängt den Befehl der gedrückten Vorzeichenumkehrstaste ("+/-").
     * Zeigt der Bildschirm einen positiven Wert an, so wird ein "-" links angehängt, der Bildschirm
     * aktualisiert und die Inhalt fortan als negativ interpretiert.
     * Zeigt der Bildschirm bereits einen negativen Wert mit führendem Minus an, dann wird dieses
     * entfernt und der Inhalt fortan als positiv interpretiert.
     */
    public void pressNegativeKey() {
        screen = screen.startsWith("-") ? screen.substring(1) : "-" + screen;
    }

    /**
     * Empfängt den Befehl der gedrückten "="-Taste.
     * Wurde zuvor keine Operationstaste gedrückt, passiert nichts.
     * Wurde zuvor eine binäre Operationstaste gedrückt und zwei Operanden eingegeben, wird das
     * Ergebnis der Operation angezeigt. Falls hierbei eine Division durch Null auftritt, wird "Error" angezeigt.
     * Wird die Taste weitere Male gedrückt (ohne andere Tasten dazwischen), so wird die letzte
     * Operation (ggf. inklusive letztem Operand) erneut auf den aktuellen Bildschirminhalt angewandt
     * und das Ergebnis direkt angezeigt.
     */
    public void pressEqualsKey() {
        calculateIntermediateResult();
        latestOperation = "";  // Setzt Operation zurück
    }

    /**
     * Berechnet das Zwischenergebnis der aktuellen Operation und aktualisiert den Bildschirminhalt.
     * Wird aufgerufen, wenn eine binäre Operationstaste gedrückt wird oder die "="-Taste.
     * Bei Division durch Null wird "Error" angezeigt.
     */
    private void calculateIntermediateResult() {
        double currentValue = Double.parseDouble(screen);

        latestValue = switch (latestOperation) {
            case "+" -> latestValue + currentValue;
            case "-" -> latestValue - currentValue;
            case "x" -> latestValue * currentValue;
            // Division durch Null wird als Fehler behandelt
            case "/" -> currentValue == 0 ? Double.POSITIVE_INFINITY : latestValue / currentValue;
            default -> currentValue;
        };

        screen = formatResult(latestValue);
        isNewInput = true;
    }

    private String formatResult(double result) {
        if (Double.isInfinite(result)) return "Error";
        String resultStr = Double.toString(result);
        if (resultStr.endsWith(".0")) resultStr = resultStr.substring(0, resultStr.length() - 2);
        return resultStr.length() > 11 ? resultStr.substring(0, 10) : resultStr;
    }
}
