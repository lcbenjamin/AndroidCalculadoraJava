package com.lcbenjamin.calculadora;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    private TextView displayExpressao;
    private TextView displayResultado;
    private String outputExpressao;
    private String outputResultado;
    private Double resultado;
    private String operacaoAtual;
    private MediaPlayer somClick;
    protected ArrayList<String> expressoes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Instancia o TextView da expressão e do resultado*/
        displayExpressao = findViewById(R.id.displayExpressao);
        displayResultado = findViewById(R.id.displayResultado);

        outputExpressao = new String("");
        outputResultado = new String("");
        operacaoAtual = new String("");

        somClick = MediaPlayer.create(MainActivity.this,R.raw.click);
    }

    private void atualizaDisplay(String caracter) {

        outputExpressao += caracter;

        displayExpressao.setText(outputExpressao);

    }

    public void onClickNumero(View view){

        String numero = ((Button) view).getText().toString();

        atualizaDisplay(numero);

        somClick.start();
    }

    public void onClickOperador(View view){

        /* Atribui operador */
        String operador = ((Button) view).getText().toString();

        /* Valida se existe alguma numero digitado. */
        if(outputExpressao.isEmpty()) return;

        /* Valida se ultimo caracter digitado é um operador.*/
        if(isOperador(outputExpressao.charAt((outputExpressao.length() -1) ))) return ;

        /* Valida se existe uma operacao conflitante na expressao */
        if(validaConflitoOperacao(operador.charAt(0))) return ;

        /* Informa operacao atual */
        operacaoAtual = operador;

        atualizaDisplay(operador);

        somClick.start();
    }

    public void onClickIgual(View view){

        /* Valida se */
        if(operacaoAtual == null || operacaoAtual.isEmpty())  return;

        String[] expressao = new String[10];

        if(isOperador( operacaoAtual.charAt(0)) ){

            expressao = outputExpressao.split(Pattern.quote(operacaoAtual));
            if(expressao.length < 2 ) return;

        } else if( isFuncao(operacaoAtual) ) {

            expressao[0]  = outputExpressao.replaceAll("\\D+","") ;

        }



        if(getResultador(expressao,operacaoAtual)){

            displayResultado.setText(resultado.toString());

            expressoes.add(outputExpressao + " = " + resultado);

        } else {
            Toast.makeText(this,"Operação invalida",Toast.LENGTH_SHORT);
        }

    }

    public void onClickReset(View view){

        if(outputExpressao.length() > 0){

            outputExpressao = "";
            outputResultado = "";
            resultado = null;
            operacaoAtual = "";
            displayResultado.setText("");
            displayExpressao.setText("");
        }

        somClick.start();
    }

    private boolean isOperador(char op){

        switch (op){
            case '+':return true;
            case '-':return true;
            case 'x':return true;
            case '÷':return true;
            case '%':return true;
            case '^':return true;
            default: return false;
        }
    }

    private boolean isFuncao(String func){

        boolean sucesso = false;

        if(func.contains("sin")) sucesso = true;
        if(func.contains("cos")) sucesso = true;
        if(func.contains("tan")) sucesso = true;
        if(func.contains("raiz")) sucesso = true;
        if(func.contains("potencia")) sucesso = true;
        if(func.contains("fat")) sucesso = true;

        return sucesso;
    }

    private boolean validaConflitoOperacao(char op){

        boolean sucesso = false;

        switch (op){
            case '+':  // Adição
                if( outputExpressao.contains("x") || outputExpressao.contains("÷") || outputExpressao.contains("-")) {
                    sucesso = true;
                }
                break;
            case '-':  // Subtração
                if( outputExpressao.contains("x") || outputExpressao.contains("÷") || outputExpressao.contains("+")) {
                    sucesso = true;
                }
                break;
            case 'x':  // Multiplicação
                if( outputExpressao.contains("+") || outputExpressao.contains("÷") || outputExpressao.contains("-")) {
                    sucesso =  true;
                }
                break;
            case '÷':  // Divisão
                if( outputExpressao.contains("x") || outputExpressao.contains("+") || outputExpressao.contains("-")) {
                    sucesso =  true;
                }
                break;
            case '%':  // Percentual
                if( outputExpressao.contains("x") || outputExpressao.contains("+") || outputExpressao.contains("-")
                        || outputExpressao.contains("÷") || outputExpressao.contains("%")) {
                    sucesso =  true;
                }
                break;
            default:
                sucesso = false;
        }

        return sucesso;
    }

    private boolean getResultador(String[] expressao, String operacao){

        Double resultadoParcial = 0.0;
        boolean sucesso = false;

        /* Calcula operações de adição  */
        if(operacao.equals("+")){

            for(String valor : expressao){

                resultadoParcial += Double.parseDouble(valor);
            }
            resultado = resultadoParcial;
            sucesso = true;
        }

        /* Calcula operações de Subtração  */
        else if (operacao.equals("-")){

            for(int i = 0 ; i < expressao.length ; i++){

                if(i == 0) {
                    resultadoParcial = Double.parseDouble(expressao[0]);
                } else {
                    resultadoParcial -= Double.parseDouble(expressao[i]);
                }
            }
            resultado = resultadoParcial;
            sucesso = true;
        }

        /* Calcula operações de Multiplicação  */
        else if (operacao.equals("x")){

            for(int i = 0 ; i < expressao.length ; i++){

                if(i == 0) {
                    resultadoParcial = Double.parseDouble(expressao[0]);
                } else {
                    resultadoParcial *= Double.parseDouble(expressao[i]);
                }
            }
            resultado = resultadoParcial;
            sucesso = true;
        }

        /* Calcula operações de Divisão  */
        else if (operacao.equals("÷")){

            for(int i = 0 ; i < expressao.length ; i++){

                if(i == 0) {
                    resultadoParcial = Double.parseDouble(expressao[0]);
                } else {
                    resultadoParcial /= Double.parseDouble(expressao[i]);
                }
            }
            resultado = resultadoParcial;
            sucesso = true;
        }

        /* Calcula operações de percentual  */
        else if (operacao.equals("%")){

            Double valorObtido = Double.parseDouble(expressao[0]);
            Double valorTotal = Double.parseDouble(expressao[1]);

            try {
                resultadoParcial = (valorObtido / 100) * valorTotal;
                resultado = resultadoParcial;
                sucesso = true;

            } catch (NumberFormatException e){
                Toast.makeText(this,"Erro na montagem da expressão matemática",Toast.LENGTH_SHORT);
                sucesso = false;
            }
        }

        /* Calcula seno  */
        else if (operacao.contains("sin")){

            try {

                resultadoParcial = Math.sin(Double.parseDouble(expressao[0]));
                resultado = resultadoParcial;
                sucesso = true;

            } catch (NumberFormatException e){
                Toast.makeText(this,"Erro na montagem da expressão matemática",Toast.LENGTH_SHORT);
                sucesso = false;
            }
        }

        /* Calcula Cosseno  */
        else if (operacao.contains("cos")){

            try {

                resultadoParcial = Math.cos(Double.parseDouble(expressao[0]));
                resultado = resultadoParcial;
                sucesso = true;

            } catch (NumberFormatException e){
                Toast.makeText(this,"Erro na montagem da expressão matemática",Toast.LENGTH_SHORT);
                sucesso = false;
            }
        }

        /* Calcula Tangente  */
        else if (operacao.contains("tan")){

            try {

                resultadoParcial = Math.tan(Double.parseDouble(expressao[0]));
                resultado = resultadoParcial;
                sucesso = true;

            } catch (NumberFormatException e){
                Toast.makeText(this,"Erro na montagem da expressão matemática",Toast.LENGTH_SHORT);
                sucesso = false;
            }
        }

        /* Calcula Raiz quadrada  */
        else if (operacao.contains("raiz")){

            try {

                resultadoParcial = Math.sqrt(Double.parseDouble(expressao[0]));
                resultado = resultadoParcial;
                sucesso = true;

            } catch (NumberFormatException e){
                Toast.makeText(this,"Erro na montagem da expressão matemática",Toast.LENGTH_SHORT);
                sucesso = false;
            }
        }

        /* Calcula Potencia  */
        else if (operacao.equals("^")){

            Double base = Double.parseDouble(expressao[0]);
            Double exponencial = Double.parseDouble(expressao[1]);

            try {
                resultadoParcial = Math.pow(base,exponencial);
                resultado = resultadoParcial;
                sucesso = true;

            } catch (NumberFormatException e){
                Toast.makeText(this,"Erro na montagem da expressão matemática",Toast.LENGTH_SHORT);
                sucesso = false;
            }
        }

        /* Calcula fatorial  */
        else if (operacao.equals("fat")){

                resultadoParcial = calculaFatorial(Double.parseDouble(expressao[0]));
                resultado = resultadoParcial;
                sucesso = true;

        }


        return sucesso;
    }

    public void onClickEnviar(View view){

        if(expressoes != null){

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("expressoes",expressoes);
            Intent intent = new Intent(this, MainActivityLog.class);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {

            Toast.makeText(this,"Não existem dados para enviar para a tela de LOG",Toast.LENGTH_SHORT);

        }

    }

    public void onClickSen(View view){

        onClickReset(view);

        operacaoAtual = "sin";
        atualizaDisplay("Sin(");
    }

    public void onClickCos(View view){

        onClickReset(view);

        operacaoAtual = "cos";
        atualizaDisplay("Cos(");
    }

    public void onClickTan(View view){

        onClickReset(view);

        operacaoAtual = "tan";
        atualizaDisplay("Tan(");
    }

    public void onClickRaiz(View view){

        onClickReset(view);

        operacaoAtual = "raiz";
        atualizaDisplay("√");
    }

    public void onClickPotencia(View view){

        operacaoAtual = "^";
        atualizaDisplay("^");
    }

    public void onClickFatorial(View view){

        onClickReset(view);

        operacaoAtual = "fat";
        atualizaDisplay("!");
    }

    public Double calculaFatorial (Double numero){

        Double i = numero -1;

        while (i > 0){
            numero =  numero * i;
            i--;
        }

        return numero ;
    }
}
