# 🦊🐇 Ecosystem Simulation – Java

Simulação de ecossistema com múltiplos agentes (animais, caçadores e obstáculos) em um grid bidimensional.

---

## 📌 Descrição Geral

Este projeto implementa um **simulador de vida selvagem**, onde diversos agentes interagem em um ambiente composto por células.  
A simulação evolui passo a passo, representando comportamentos naturais:

- Coelhos se movem e se reproduzem.
- Raposas caçam coelhos e sobrevivem por energia.
- Caçadores caçam raposas e coelhos.
- Obstáculos (pedras) bloqueiam movimento.
- O ambiente possui estações do ano que influenciam comportamento.

O sistema foi escrito em **Java**, utilizando orientação a objetos, abstração, herança, polimorfismo e o padrão de **interfaces marcadoras**.

---

## 🧱 Principais Componentes do Projeto

### **1. Simulator**

Classe central da aplicação. Controla:

- passo atual da simulação
- estação do ano
- lista de atores (animais e humanos)
- lista de obstáculos
- o grid (Field)
- fluxo da simulação

---

### **2. Field**

Representa a matriz onde todos os agentes vivem.

Responsabilidades:

- armazenar objetos por localização
- inserir/remover objetos
- obter células adjacentes
- fornecer posições alternativas para movimentação

---

### **3. Location**

Classe que representa uma posição do grid (`row`, `col`).  
Usada por todos os agentes para saber onde estão.

---

### **4. Animal**

Classe abstrata base para todos os seres vivos.

Define:

- idade
- estado de vida
- localização
- lógica de envelhecimento
- método abstrato `act(...)`
- acesso e modificação da posição

Subclasses:

#### 🐇 Rabbit

- Presa
- Reproduz quando atinge idade adequada
- Movimenta-se aleatoriamente
- Implementa **HuntersPreys** (pode ser caçado)

#### 🦊 Fox

- Predador de coelhos
- Perde energia por passo
- Procura comida
- Implementa **HuntersPreys** (pode ser caçada por caçadores)

#### 🧍‍♂️ Hunter (opcional)

- Predador de coelhos e raposas
- Não implementa HuntersPreys

---

### **5. Interface Marcadora: HuntersPreys**

Interface **intencionalmente vazia** que marca animais que podem ser caçados.

Segue o padrão **Marker Interface Pattern**, permitindo:

- adicionar novas presas sem alterar o código do simulador
- filtrar rapidamente animais caçáveis via `instanceof`
- manter a simulação simples e extensível

Uso típico:

```java
if (object instanceof HuntersPreys prey) {
    if (prey.isAlive()) {
        prey.setDead();
        killCount++;
        return where;
    }
}
```

---

### **6. Obstáculos**

Uma segunda interface marcadora, `Obstacles`, identifica objetos que bloqueiam movimento.

Atualmente:

- `Stone` representa uma pedra fixa no mapa.

Outros obstáculos podem ser adicionados facilmente (por exemplo, rios, árvores, montanhas).

---

## 🧠 Arquitetura Geral (Resumo)

- `Animal` é abstrata.
- `Rabbit` e `Fox` estendem `Animal`.
- `Rabbit` e `Fox` implementam `HuntersPreys`.
- `Stone` implementa `Obstacles`.
- `Simulator` gerencia listas de `Animal` e `Obstacles`.
- `Field` representa o ambiente.
- `Location` representa posições.

O projeto explora:

- Herança
- Polimorfismo
- Encapsulamento
- Interfaces marcadoras
- Organização modular

---

## ▶️ Como executar a simulação

1. Compile os arquivos Java:

```bash
javac */*.java
```

2. Execute a aplicação pelo método `main`

```bash
java SimulatorMain
```

3. A simulação será iniciada e o ambiente começará a evoluir passo a passo.
   A simulação inicia:

- animais se movem
- predadores caçam
- presas morrem
- idades aumentam
- mapa evolui passo a passo

---

## 📂 Estrutura dos Arquivos

.
├── .vscode/ # Configurações do VS Code
├── bin/ # Arquivos compilados (.class)
├── src/ # Código-fonte Java
│ ├── Animal.java
│ ├── Rabbit.java
│ ├── Fox.java
│ ├── Hunter.java
│ ├── Field.java
│ ├── Location.java
│ ├── HuntersPreys.java
│ ├── Obstacles.java
│ ├── Stone.java
│ ├── Simulator.java
│ └── SimulatorMain.java
├── PropostaTrabalhoPratico.pdf # Documento original do trabalho
└── README.md # Este arquivo

---

## 🧪 Possíveis Extensões

- Novas espécies
- Plantas com crescimento
- Fog of war
- Visualização gráfica
- Predadores com estratégia de caça
- Estatísticas avançadas (população, taxa de caça etc.)

---

## 📜 Licença

Projeto para fins acadêmicos.

---

## 👥 Autores

**Trabalho Prático — Grupo 5**  
Curso de Programação Orientada a Objetos
