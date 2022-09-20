import java.util.stream.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.*;
import static java.lang.System.out;


public class william_siqueira_aula1Pratico {
    
    public static String SALVAR_OP = "s";
    public static String PATH_NUMEROS = "numeros";
    public static String PATH_RESULTADOS = "resultados.csv";
    
    public static int N_PADRAO = 100_000;
    public static Random R = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws Exception {
        var dataExecucao = new Date().toString();
        var pathNumeros = Paths.get(PATH_NUMEROS);
        var pathResultados = Paths.get(PATH_RESULTADOS);
        var n = N_PADRAO;
        var salvar = false;
        
        if (args.length > 0) {
            try {
                n  = Integer.parseInt(args[0]);
            } catch (Exception e) {
                salvar = SALVAR_OP.equals(args[0]);
            }
        }

        if (args.length > 1) {
            salvar = SALVAR_OP.equals(args[1]);
        }
        
        if (n < 1) {
            out.printf("Número \"%d\" é inválido. Valor precisa ser maior que 0.\n", n);
            System.exit(0);
        }

        var numeros = gerar(n);        
        var tempo = 0l;


        tempo = System.currentTimeMillis();
        var primosR = Arrays.stream(numeros).map(i -> ehPrimoR(i) ? 1 : 0).toArray();
        var tempoPrimosR = System.currentTimeMillis() - tempo;

        // Pode ser melhorado usando nano
        tempo = System.currentTimeMillis();
        var primos = Arrays.stream(numeros).map(i -> ehPrimo(i) ? 1 : 0).toArray();
        var tempoPrimos = System.currentTimeMillis() - tempo;
        

        
        var resultado = String.format("%s,%d,%d,%d\n", dataExecucao, n, tempoPrimos, tempoPrimosR);
        out.printf(resultado);
        
        if (!Files.exists(pathResultados)) {
            Files.writeString(pathResultados, "data,n,iterativo,recursivo\n");
        }
        
        Files.writeString(pathResultados, resultado, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    
        if (salvar) {
            var nome = dataExecucao.replaceAll("\\s", "_") + ".csv";
            var numerosSaida = new ArrayList<String>();
            numerosSaida.add("n,iterativo,recursivo");
            IntStream.range(0, n).forEach(i -> {
                var linha = String.format("%d,%d,%d", numeros[i], primos[i], primosR[i]);
                numerosSaida.add(linha);
            });
            Files.createDirectories(pathNumeros);
            Files.write(pathNumeros.resolve(nome), numerosSaida);           
        }
    }
    
    public static int[] gerar(int n)  {
        return IntStream.range(0, n).map(i -> R.nextInt(n)).toArray();    
    }  

    
    public static boolean ehPrimo(int n) {
        if (n < 2) {
            return false;
        }
        
        for (var i = 2; i < (n / 2) + 1; i++ ) {
            if ( n % i == 0 ) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean ehPrimoR(int i) {
        return ehPrimoR(i, (i / 2) + 1);
    }
    
    public static boolean ehPrimoR(int i, int x) {
        if (i < 2) {
            return false;
        }
        
        if (x < 2) {
            return true;
        }        
        
        if (i % x == 0) {
            return false;
        }       

        return ehPrimoR(i, x - 1);        
   }

}
