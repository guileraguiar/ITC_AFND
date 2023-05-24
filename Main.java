import java.util.*;

public class Main {

    private Map<String, Map<String, Set<String>>> transicoes;
    private String estadoInicial;
    private Set<String> estadoFinal;

    public Main(Set<String> estados, Set<String> alfabeto, Map<String, Map<String, Set<String>>> transicoes,
                String estadoInicial, Set<String> estadoFinal) {

        this.transicoes = transicoes;
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
    }

    public String execute(String input) {
        Set<String> estadoAtual = new HashSet<>();
        estadoAtual.add(estadoInicial);
        FechamentoEpsilon(estadoAtual);

        for (char symbol : input.toCharArray()) {
            Set<String> proximoEstado = new HashSet<>();
            for (String estado : estadoAtual) {
                if (transicoes.containsKey(estado) && transicoes.get(estado).containsKey(String.valueOf(symbol))) {
                    proximoEstado.addAll(transicoes.get(estado).get(String.valueOf(symbol)));
                }
            }
            FechamentoEpsilon(proximoEstado);
            estadoAtual = proximoEstado;
        }

        for (String estado : estadoAtual) {
            if (estadoFinal.contains(estado)) {
                return "ACEITA";
            }
        }

        return "RECUSA";
    }

    private void FechamentoEpsilon(Set<String> estados) {
        Stack<String> stack = new Stack<>();
        stack.addAll(estados);

        while (!stack.isEmpty()) {
            String estado = stack.pop();
            if (transicoes.containsKey(estado) && transicoes.get(estado).containsKey("ε")) {
                Set<String> estadosEpsilon = transicoes.get(estado).get("ε");
                for (String estadoEpsilon : estadosEpsilon) {
                    if (!estados.contains(estadoEpsilon)) {
                        estados.add(estadoEpsilon);
                        stack.push(estadoEpsilon);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Map<String, Map<String, Set<String>>> transicoes = new HashMap<>();
        Set<String> estados = new HashSet<>();
        Set<String> alfabeto = new HashSet<>();
        String estadoInicial = "";
        Set<String> estadoFinal = new HashSet<>();

        Scanner scanner = new Scanner(System.in);

        System.out.print("AFND - Autômato Finito Não Determinístico ");

        System.out.print("\nDigite os estados separados por vírgula: ");
        String estadosInput = scanner.nextLine();
        estados.addAll(Arrays.asList(estadosInput.split(",")));

        System.out.print("\nDigite o alfabeto separado por vírgula: ");
        String alfabetoInput = scanner.nextLine();
        alfabeto.addAll(Arrays.asList(alfabetoInput.split(",")));

        System.out.print("\nDigite o estado inicial: ");
        estadoInicial = scanner.nextLine();

        System.out.print("\nDigite o estado final: ");
        String estadoFinalInput = scanner.nextLine();
        estadoFinal.addAll(Arrays.asList(estadoFinalInput.split(",")));

        System.out.println("\nDigite as transições no formato 'estadoAtual,simbolo,estadoFuturo1,estadoFuturo2,...'");
        System.out.println("Digite 'fim' para encerrar a leitura das transições.");

        while (true) {
            System.out.print("\nDigite uma transição: ");
            String transicaoInput = scanner.nextLine();
            if (transicaoInput.equalsIgnoreCase("fim")) {
                break;
            }
            String[] parts = transicaoInput.split(",");
            String estadoAtual = parts[0];
            String simbolo = parts[1];
            String[] proximosEstados = Arrays.copyOfRange(parts, 2, parts.length);

            transicoes.putIfAbsent(estadoAtual, new HashMap<>());
            transicoes.get(estadoAtual).putIfAbsent(simbolo, new HashSet<>());
            transicoes.get(estadoAtual).get(simbolo).addAll(Arrays.asList(proximosEstados));
        }

        Main afnd = new Main(estados, alfabeto, transicoes, estadoInicial, estadoFinal);

        while (true) {
            System.out.print("\nDigite uma cadeia de 0s e 1s (ou 'sair' para encerrar): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("sair")) {
                break;
            }
            if (!input.matches("[01]+")) {
                System.out.println("Número inválido. Digite apenas 0s e 1s.");
                continue;
            }
            String result = afnd.execute(input);
            System.out.println("Resultado: " + result);
        }

        scanner.close();
    }
}
