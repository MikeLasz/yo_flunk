package com.example.yo_flunk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RulesActivity extends AppCompatActivity {

    TextView txtViewRules;
    String txtIntro, txtAufstellung, txtAblauf, txtStrafen, txtFaq;
    LinearLayout layoutAufstellung, layoutAblauf, layoutStrafen, layoutFaqs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        layoutAufstellung = findViewById(R.id.btAufstellung);
        layoutAblauf = findViewById(R.id.bt_ablauf);
        layoutStrafen = findViewById(R.id.bt_strafen);
        layoutFaqs = findViewById(R.id.bt_faqs);

        txtViewRules = (TextView) findViewById(R.id.rules);
        txtIntro = "Flunkyball, ausgesprochen wie es geschrieben wird (und nicht “Flankybohl”), ist eine Trinksportart, in der sich zwei Teams gegenüber stehen.\n" + "Ausgerüstet mit einem Bier pro Spieler, geht es darum alle Biere eines Teams schnellstmöglichst auszutrinken. \n" + "Zum Spielen, werden eine Mittelflasche, ein Ball, und Bier benötigt.";
        txtAufstellung = "In der klassischen Variante treten zwei Teams gegeneinander an. \n"+"Zunächst wird ein Mittelpunkt festgelegt. Auf dem Mittelpunkt wird eine zu 1/3-gefüllte PET-Flasche (1l – 1.5l) gestellt. \n"+"Jeweils 5 Meter/Schritte von dem Mittelpunkt entfernt, auf gegenüberliegenden Seiten, werden die Grundlinien, parallel zueinander, gekennzeichnet. Dabei wird jedem Team eine Grundlinie zugeordnet. Die Spieler stellen sich hinter dieser Linie in fester Reihenfolge auf. Jeder Spieler hat sein geschlossenes Bier vor sich auf oder maximal 10cm hinter der Grundlinie stehen. \n" +
                "Ein optionaler Schiedsrichter positioniert sich mittig zwischen beiden Teams außerhalb des Spielfeldes. \n" +
                "Gespielt wird mit einem standardmäßigen Tennisball. Unter Umständen kann auch ein Handball oder eine Zwiebel benutzt werden.";
        txtAblauf = "Das Spiel beginnt mit einer Runde Schnick-Schnack-Schnuck, welche von einer Person je Team ausgetragen wird. Das Team des Siegers darf den ersten Wurf machen.\n" +"\n" +
                "Nun beginnt das eigentliche Spiel:\n"+ "Abwechselnd müssen die Spieler nun versuchen die Mittel-Flasche mit einem Wurf des Balls zum Umfallen zu bringen. Der Ball muss die Hand über der Schulter verlassen! Das heißt er wird nicht gekegelt, gebowlt, oder gekullert. Hier achtet der Schiedsrichter darauf, dass die Grundlinie nicht übertreten wird.\n"+ "Es gilt, der linksaußen stehende Spieler der beiden Teams beginnt. Danach geht es der Reihe nach. \n"+ "Es können nun folgende Szenarien eintreten:\n" +
                "1. Treffer: Kippt die Mittel-Flasche, öffnet das werfende Team ihr Bier und beginnt zu trinken. Das gegnerische Team stellt schnellstmöglich die Flasche auf den Mittelpunkt, sammelt den Ball ein und alle Teammitglieder positionieren sich zurück hinter die Grundlinie. Erst dann gibt der Schiedsrichter ein lautstarkes “Stopp”-Signal. Beim Spiel ohne Schiedrichter gibt das Team selbst ein Stopp-Signal. Das trinkende Team muss sofort aufhören zu trinken. Es ist erlaubt sein Bier - mit dem Flaschenboden nach unten - in der Hand und den Mund am Flaschenhals zu halten. Das Bier muss nach einigen Sekunden wieder auf die ursprüngliche Position gestellt werden. Nun darf das andere Team werfen.\n" +
                "2. Bierflasche getroffen: Wird eine geöffnete Bierflasche des gegnerischen Teams getroffen, sodass sie umfällt, muss der jeweilige Besitzer des Bieres seine Flasche schnellstmöglichst wieder aufstellen. Der betroffene Spieler muss das Bier exen und bekommt ein neues, geschlossenes Bier (gemäß des Ausrufs: “Ex und ‘n Neues!”). Dieses darf dann aber geöffnet werden. Sobald das alte Bier geext ist und der Spieler ein neues Bier vor sich stehen hat, darf dieses Team werfen. Um den Treffer einer Bierflasche zu verhindern, darf man den Ball mit dem Körper aufhalten.\n" +
                "3. Kein Treffer: In diesem Fall darf das andere Team werfen.\n" +
                "\n" +
                "Spielende:\n"+ "Sobald ein Spieler der Meinung ist, dass seine Bierflasche ausgetrunken ist, darf er diese beim Schiedsrichter abgeben. Der Kronkorkentest entscheidet darüber, ob das Bier tatsächlich leer ist. Der Kronkorentest darf nur vom Schiedsrichter (oder unter Aufsicht des gegnerischen Teams) und nach einem Treffer des abgebenden Spielers durchgeführt werden. Beim Kronkorkentest hält der Schiedsrichter das Bier drei Sekunden lang kopfüber und senkrecht über einen Kronkorken. Tropft so viel Bier in den Kronkorken, dass er überläuft, so gilt das Bier nicht als leer. Zur Strafe bekommt der Spieler ein Strafbier. Ansonsten ist der Spieler fertig und muss damit das Spielfeld verlassen. Ist jeder Spieler eines Teams fertig, so hat das Team gewonnen. \n" +
                "Es ist erlaubt eine bereits ausgetrunkene Bierflasche noch nicht zum Kronkorkentest abzugeben, obwohl das Bier bereits leer ist.";
        txtStrafen = "Beim Flunkyball gibt es eine Reihe von Regeln, deren Verstoß mit einer Strafe geahndet wird. Diese sind entweder eine Strafrunde oder ein Strafbier. Erhält ein Spieler eine Strafrunde, so darf er beim nächsten Treffer des Teams nicht trinken. Erhält ein Spieler ein Strafbier, so muss er sein aktuelles Bier exen (gemäß des Ausrufs: “Ex und ‘n Neues!”) und erhält ein neues, geschlossenes Bier.\n"+ "Grundsätzlich gilt: Der Schiedsrichter hat immer Recht! Die folgende Tabelle bietet eine Übersicht über die Strafen.\n"
                +"\n"+"Übertreten der Grundlinie\n" +
                "Erst Verwarnung, dann Strafrunde\n" +"\n"+
                "Werfen, obwohl das gegnerische Team noch nicht bereit ist\n" +
                "Strafrunde\n" +"\n"+
                "Überschäumen des Bieres\n" +
                "Strafrunde\n" +"\n"+
                "Zuhalten des Bieres\n" +
                "Strafrunde\n" + "\n" +
                "Umfallen des Bieres\n" +
                "Strafbier\n" +"\n"+
                "Kronkorkentest nicht bestanden\n" +
                "Strafbier\n" +"\n"+
                "Unsportsmanlike\n" +
                "Im Ermessen des Schiedrichters\n";
        txtFaq = "1. Ein Spieler fällt während eines Spiels unfallbedingt aus. Was nun?\n" +
                "\tSollte ein Spieler unfallbedingt spontan ausfallen, so wird das Spiel unterbrochen und das Team darf einen Ersatzspieler organisieren. \tDafür hat das Team maximal fünf Minuten Zeit. Sollte in der vorgegebenen Zeitspanne kein geeigneter Ersatzspieler gefunden werden, muss das Team entweder aufgeben oder aber mit einer Person weniger weiterspielen. Die Mannschaft muss in diesem Fall jedoch mit der Anzahl der Startbiere der ursprünglichen Teammitglieder plus eventuelle bis dahin erhaltene Strafbiere weiterspielen. Das gilt auch für alle folgenden Spiele, die mit einem Spieler weniger angetreten werden.\n" +
                "\n" +"\n"+
                "2. Der Schiedsrichter ist sich wegen einer Sache unsicher. Darf er sich bewegen und das Spiel unterbrechen?\n" +
                "\tJa. Der Schiedsrichter darf das Spiel zur jederzeit unterbrechen. Auch darf er seine neutrale Position verlassen, um Bierflaschen zu inspizieren. Beispielsweise um zu prüfen, ob ein Bier übergeschäumt ist.\n" +
                "\n" +
                "3. Ich habe ein spritziges Biermischgetränk. Darf ich damit Flunkyball spielen?\n" +
                "\tNein. Als Getränk ist ausschließlich reines Bier zugelassen, keinerlei \tMischgetränke oder andere Alkoholika. Die Flaschen haben dabei 0,5l \tInhalt zu haben und müssen aus Glas bestehen. Solange das Bier als \tsolches eindeutig zu erkennen ist (ab mindestens 4,5 Vol.% \tAlkoholgehalt, kohlensäurehaltig und nach dem Deutschen \tReinheitsgebot von 1516 gebraut), ist es zulässig. Die Form der Flaschen \tsollte Standard-Pils-Flaschen entsprechen (schlank oder bauchig). Bei Flaschen mit Bügelverschluss ist auf die Unversehrtheit des Siegels zu achten\n" +
                "\n" +
                "4. Darf ich einen Strohhalm benutzen, um mein Bier schneller auszutrinken?\n" +
                "\tNein. Es dürfen keinerlei Hilfsmittel wie z.B. Strohhalme zum Trinken \tgenutzt werden. Ein Verstoß kann zur Disqualifikation führen, sowohl wenn der Regelverstoß vor dem ersten Trinken als auch wenn er erst nach dem ersten Trinken entdeckt wird, unabhängig davon, ob eine Nutzung von Hilfsmitteln nachgewiesen werden kann oder nicht. Erlaubt ist jedoch das einmalige Nutzen eines Hilfsmittels (Bsp. Flaschenöffner, Feuerzeug) zum Öffnen der Bierflasche vor dem ersten Trinken.\n" +
                "\n" +
                "5. Darf ich mit einem bereits vor dem Spiel geöffneten Bier spielen?\n" +
                "\tNein.\n" +
                "\n" +
                "6. Darf ich mein Bier bereits anfassen, wenn mein Team zum Wurf ansetzt?\n" +
                "\tNein. Es ist erlaubt sich vor dem Bier in die Hocke zu begeben, allerdings darf die Flasche erst bei einem Treffer berührt werden. \n" +
                "\n" +
                "7. Darf ich mein Bier nach einem Treffer schütteln, um die Schaumbildung zu reduzieren?\n" +
                "\tJa.\n" +
                "8. Das gegnerische Team ist mit dem Wurf an der Reihe. Ich bin allerdings noch nicht bereit. Wie signalisiere ich das dem gegnerischen Team?\n" +
                "\tIst man noch nicht bereit, so darf man dies durch Heben der Hand signalisieren. Dies verpflichtet das gegnerische Team dazu mit dem Werfen zu warten. Wird der Ball geworfen, obwohl die Hand gehoben ist, gilt der Wurf \tals ungültig. Trinkt dennoch einer der Spieler (aufgrund eines ungültigen \tTreffers), so erhält jeder Spieler des jeweiligen Teams eine Strafrunde.\n" +
                "\t";

        txtViewRules.setText(txtIntro);
        txtViewRules.setMovementMethod(new ScrollingMovementMethod());
    }

    public void ShowAufstellung(View view) {
        txtViewRules.setText(txtAufstellung);

        layoutAblauf.setBackground(null);
        layoutStrafen.setBackground(null);
        layoutFaqs.setBackground(null);
        layoutAufstellung.setBackgroundResource(R.drawable.view_withoutframe_foot);

    }

    public void ShowAblauf(View view) {
        txtViewRules.setText(txtAblauf);
        layoutAufstellung.setBackground(null);
        layoutStrafen.setBackground(null);
        layoutFaqs.setBackground(null);
        layoutAblauf.setBackgroundResource(R.drawable.view_withoutframe_foot);
    }

    public void ShowStrafen(View view) {
        txtViewRules.setText(txtStrafen);
        layoutAblauf.setBackground(null);
        layoutAufstellung.setBackground(null);
        layoutFaqs.setBackground(null);
        layoutStrafen.setBackgroundResource(R.drawable.view_withoutframe_foot);
    }

    public void ShowFaq(View view) {
        txtViewRules.setText(txtFaq);
        layoutAblauf.setBackground(null);
        layoutStrafen.setBackground(null);
        layoutAufstellung.setBackground(null);
        layoutFaqs.setBackgroundResource(R.drawable.view_withoutframe_foot);
    }
}