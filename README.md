# Relatório - Sistema para Folhas de pagamento - WePayU

- Trabalho apresentado à disciplina de Programação 2 - matéria presente na grade do Curso de Ciência da Computação, como parte dos requisitos necessários para a composição da AB1.
  - Professor: Mário Hozano.
  - Alunos: João Jacinto e Caio Rocha


## Apresentação do Relatório:

- O presente relatório consiste em uma explicação e exemplificação dos padrões de arquitetura de códigos utilizados durante o desenvolvimento do sistema WePayU, como também o destaque dos aspectos positivos/negativos de cada arquitetura levando em consideração as necessidades da aplicação.
  - Padrões de Design de Desenvolvimento implementados:
    - Padrão "Facade"
    - Padrão de "Camadas"
    - Padrão "Repository"
    - Padrão "Models"
    - Padrão "Services"

### Padrão "Facade"

O "Facade" é considerado um padrão de projeto/design estrutural que busca abstrair a inicialização/estruturação de objetos que seriam essenciais para o funcionamento da aplicação. Esse padrão sugere a implementação de um encapsulamento de "subsistemas" da aplicação em um objeto único de Interface permitindo a inicialização individual dos objetos e garantindo uma maior facilidade no manuseio de dependências. A classe "Facade" deve redirecionar as chamadas do código principal para os objetos apropriados/específicos do subsistema, além disso, caso o código principal não gerencie o ciclo de vida ou inicialize o subsistema, a classe também fica responsável por essa tarefa. De outro modo, é necessário uma certa cautela para manusear as classes que seguem esse padrão pois, caso a classe "Facade" acople todas as outras classes inerentes à aplicação, ela pode se tornar um "objeto deus", fugindo do propósito inicial sugerido/resolvido pelo padrão "Facade".

- Vantagens de utilização do padrão "Facade":

  1. Interfaces mais simplificadas: a disponibilização de uma interface simplificada e única que permite a manipulação de toda a aplicação.
  2. Desacoplamento: o padrão permite o desacoplamento entre o código do Cliente e o código relacionado ao subsistema, desse modo, o cliente fica isento dos detalhes de implementação do próprio subsistema da aplicação.
  3. Encapsulamento: Os detalhes internos do subsistema são encapsulados juntamente com a "fachada". O Cliente somente possui o acesso à Interface relacionada à "Fachada".
  4. Manutenção e Refatoração: facilita o processo de melhorias/refatoração, uma vez que mudanças realizadas no subsistema podem ser realizadas sem afetar diretamente o cliente.
  5. Boas práticas e promoção de Leitura e Usabilidade: ao promover a utilização de uma interface clara e simples, como também a adoção de nomenclaturas de métodos que refletem as suas reais operações, o padrão facilita o bom manuseio do sistema como um todo.

- Desvantagens de utilização do padrão "Facade":

  1. Flexibilidade Limitada: o acesso ao subsistema proporcionado pela Interface "facade" talvez não forneça o acesso necessário para alguns clientes.
  2. Acoplamento implícito: mudanças na Interface podem, sem aviso, afetar os clientes.
  3. Redução da Visibilidade da Complexidade do Subsistema: uma vez que a "Facade" abstrai complexidade do sistema, ela pode omitir
     informações cruciais para o funcionamento do subsistema.

- Exemplo de implementação:

```java
public class Facade {
  private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();
  private final SistemaFolha sistemaFolha = new SistemaFolha();
  private final SistemaVendas sistemaVendas = new SistemaVendas();
  private final SistemaEmpregados sistemaEmpregados = new SistemaEmpregados();
  private final SistemaTaxaSindical sistemaTaxaSindical = new SistemaTaxaSindical();
  public List<String> listaIdMembros = new ArrayList<>();

  public void zerarSistema() {
    empregadosRepository.zeraRepository();
  }

  /**
   * Salva o estado atual dos empregados.
   */
  public void encerrarSistema() {
    Utils.salvarEmXML(empregadosRepository.getAllEmpregados(), "./listaEmpregados.xml");
    listaIdMembros = new ArrayList<>();
  }

  /**
   * Remove empregado.
   *
   * @param idEmpregado id do empregado.
   * @throws IdentificacaoMembroNulaException   é lançada quando o idEmpregado é nulo.
   * @throws EmpregadoNaoEncontradoException é lançada quando não é encontrado o empregado.
   */
  public void removerEmpregado(String idEmpregado) throws EmpregadoNaoEncontradoException, IdentificacaoMembroNulaException {
    var empregado = empregadosRepository.getEmpregadoById(idEmpregado);
    empregadosRepository.removeEmpregado(empregado);
  }

  /**
   * Captura atributo de um empregado.
   *
   * @param idEmpregado id do empregado.
   * @param atributo    atributo do empregado a ser recuperado.
   * @throws Exception é lançada quando não é possível recuperar o atributo do empregado.
   */
  public String getAtributoEmpregado(String idEmpregado, String atributo) throws Exception {
    Empregado empregado = empregadosRepository.getEmpregadoById(idEmpregado);
    return sistemaEmpregados.getAtributoEmpregado(empregado, atributo);
  }
```

### Padrão de "Camadas"

Uma arquitetura em camadas é um estilo arquitetural que busca destacar e dividir as responsabilidades dos segmentos da aplicação, geralmente criando um isolamento e dando um propósito bem específico para cada camada e, desse modo, facilitando a manutenção, evolução e até mesmo o reaproveitamento de código presente no sistema. O padrão para a divisão de camadas foi o seguinte: Modelos, "Repositories" e "Services".

- Vantagens da utilização dessa arquitetura:

  1. Padronização do código: a implementação de uma estrutura
     que é bem definida e segmentada facilita não só a implementação de novas funcionalidades como também corrobora para uma consistência de escrita do código.
  2. Modularidade: visto que a aplicação é dividida em segmentos com escopos de atuação pré definidos, cada camada pode ser testada de forma independente (testes unitários), além de que a manutenção da aplicação torna-se mais fácil, visto que mudanças em uma camada, necessariamente, não afetam as outras. Outro benefício relacionado a esse contexto seria a implementação de camadas de segurança, uma vez que o acesso aos componentes da aplicação pode ser dividido por interfaces que permitem acesso a apenas uma ou outra camada específica.

#### Padrão "Repository"

O conceito de "Repositories" é um padrão de design de aplicações que busca concentrar/centralizar a manipulação/acesso aos dados de uma aplicação em um segmento de código isolado com a finalidade de encapsular as regras de negócio da aplicação.

- Vantagens da utilização desse padrão:

  1. Abstração do acesso aos Dados e Desacoplamento: esse
     padrão fornece uma camada de abstração sobre as operações de manipulação dos dados visto que a lógica dessas operações não precisam conhecer necessariamente como os dados da aplicação são armazenados.
  2. Encapsulamento/Reutilização do código: uma vez que a lógica de manipulação dos dados é encapsulada, a manutenção dos "repositories" como também a execução de testes unitários tornam-se mais fáceis/flexíveis de realização.

- Exemplo de implementação:

```java
public class EmpregadosRepository {
    List<Empregado> empregados = Utils.carregarEmpregadosDeXML("./listaEmpregados.xml");
    DadosEmpregadoSistemaFolha dadosEmpregadoSistemaFolhas = Utils.carregarDadosFolhaDeXML("./listaDadosSistemaFolhas.xml");
    DadosEmpregadoSistemaVendas dadosEmpregadoSistemaVendas = Utils.carregarDadosVendasDeXML("./listaDadosSistemaVendas.xml");
    DadosEmpregadoSistemaTaxaSindical dadosEmpregadoSistemaTaxaSindical = new DadosEmpregadoSistemaTaxaSindical();
    public List<String> atributosEmpregados = new ArrayList<String>();
    public List<String> tiposEmpregados = new ArrayList<String>();
    public List<String> metodosPagamento = new ArrayList<String>();
    public List<Empregado> getAllEmpregados() {
        return empregados;
    }
    public DadosEmpregadoSistemaFolha getAllDadosSistemaFolha() {
        return dadosEmpregadoSistemaFolhas;
    }

    public EmpregadosRepository() {
        inicializaAtributos();
        inicializaTipo();
        inicializaMetodoPagamento();
    }

    /**
     * Adiciona um empregado à lista de empregados.
     *
     * @param empregado Empregado a ser adicionado.
     * @return Lista de empregados após adição.
     */
    public List<Empregado> addEmpregado(Empregado empregado) {
        empregados.add(empregado);
        Utils.salvarEmXML(empregados, "./listaEmpregados.xml");
        return empregados;
    }

    /**
     * Remove um empregado do repositório.
     *
     * @param empregado Empregado a ser removido.
     */
    public void removeEmpregado(Empregado empregado) {
        empregados.remove(empregado);
    }
```

### Padrão "Models"
O padrão "Models" refere-se à implementação de classes ou interfaces que representam os objetos principais de uma aplicação, geralmente são utilizados para a representação de entidades de negócios.

- Vantagens da utilização desse padrão:

  1. Validação dos Dados: os modelos podem incluir uma lógica
     para validar os dados que estão sendo atribuídos, garantindo
     uma consistência dos dados.
  2. Encapsulamento dos Dados: os modelos devem encapsular
     os seus dados, fornecendo métodos de acesso/modificação, promovendo uma integridade dos dados armazenados.

- Exemplo de implementação:

```java
public class EmpregadoHorista extends Empregado{
    private Double salarioPorHora = 0.0;
    public EmpregadoHorista(String nome, String endereco, String tipo, MetodoPagamento metodoPagamento, Double salarioPorHora, MembroSindicato sindicalizado) throws Exception {
        super(nome, endereco, tipo, sindicalizado, metodoPagamento);
        setSalarioPorHora(validarSalario(salarioPorHora));
    }
    public EmpregadoHorista(){}

    /**
     * Obtém o valor do salário por hora do empregado horista.
     *
     * @return Valor do salário por hora do empregado horista.
     */
    public double getSalarioPorHora() {
        return salarioPorHora;
    }

    /**
     * Define o valor do salário por hora do empregado horista, realizando validação.
     *
     * @param salarioPorHora Valor do salário por hora a ser atribuído.
     */
    public void setSalarioPorHora(Double salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }

    /**
     * Validação do salário do empregado horista.
     *
     * @param salario Valor do salário a ser validado.
     * @return Valor do salário se válido.
     * @throws Exception Exceção lançada se o salário for nulo, zero ou negativo.
     */
    public Double validarSalario(Double salario) throws Exception {
        if (salario.isNaN() || salario == 0)
            throw new Exception("Salario nao pode ser nulo.");
        if (salario < 0)
            throw new Exception("Salario deve ser nao-negativo.");
        else return salario;
    }
}
```

### Padrão "Services"


O padrão "Services" é um conceito que é amplamente utilizado na arquitetura de aplicações, seu principal objetivo é encapsular a lógica de negócios de cada segmento de atuação da aplicação, com a finalidade de tornar o código modular, reutilizável e com a sua responsabilidade específica.

- Vantagens da utilização desse padrão:

  1. Separação de Responsabilidades: seguindo esse padrão,
     cada serviço acaba sendo responsável por uma única funcionalidade específica, mantendo uma organização do código, facilitando a compreensão do fluxo lógico da aplicação e reduzindo o acoplamento entre os componentes da aplicação.
  2. Modularidade: devido ao encapsulamento das funcionalidades, a reutilização dos serviços em diferentes partes da aplicação torna-se mais fácil, além disso a formalização de testes unitários para cada serviço também torna-se mais simples. Outro aspecto importante relacionado à modularidade seria o impacto positivo quanto a manutenção do código, visto que as alterações seriam feitas em contextos singulares.

- Exemplo de implementação:


```java
public class SistemaFolha {
    private final EmpregadosRepository empregadosRepository = new EmpregadosRepository();
    DadosEmpregadoSistemaFolha dadosEmpregadoSistemaFolha =  empregadosRepository.getAllDadosSistemaFolha();

    public SistemaFolha() {
    }

/**
     * Obtém o total de horas normais trabalhadas por um empregado no intervalo de datas especificado.
     *
     * @param idEmpregado Identificador único do empregado.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal   Data final do intervalo.
     * @return String representando o total de horas normais trabalhadas.
     * @throws Exception Lançada se ocorrer algum erro durante a operação.
     */
    public String getHorasNormaisTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        // Validar as datas fornecidas
        if (!validarData(dataFinal, "final"))
            throw new SistemaFolhaException(Mensagens.dataFinalInvalida);
        if (!validarData(dataInicial, "inicial"))
            throw new SistemaFolhaException(Mensagens.dataInicialInvalida);

        // Filtrar os cartões de ponto do empregado no intervalo de datas
        var dadosDoEmpregadoEmQuestao = dadosEmpregadoSistemaFolha.getCartoes().stream()
                .filter(dados -> dados.getIdEmpregado().equals(idEmpregado))
                .toList();

        // Se não houver dados, retornar 0 horas
        if (dadosDoEmpregadoEmQuestao.isEmpty()) {
            return "0";
        }

        // Calcular as horas trabalhadas no intervalo
        var horas = calcularHorasTrabalhadas(dadosDoEmpregadoEmQuestao, dataInicial, dataFinal);

        // Formatar e retornar o resultado
        return formatarSomaHoras(horas);
    }
```