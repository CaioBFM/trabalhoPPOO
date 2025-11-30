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

#### 🧍‍♂️ Hunter

- Predador de coelhos e raposas
- Come frutas para recarregar energia
- Não implementa HuntersPreys

#### 🌳 Tree

- Produz frutos ao longo do tempo
- Implementa Actors

---

### **5. Obstáculos**

Uma segunda interface marcadora, `Obstacles`, identifica objetos que bloqueiam movimento.

Atualmente:

- `Stone` representa uma pedra fixa no mapa.

Outros obstáculos podem ser adicionados facilmente (por exemplo, rios, montanhas).

---

## 🧠 Arquitetura Geral (Resumo)

- `Actors` e `Animal` são abstratas.
- `Animal` extende `Actors`.
- `Rabbit` e `Fox` estendem `Animal`.
- `Rabbit` e `Fox` implementam `HuntersPreys`.
- `Stone` implementa `Obstacles`.
- `Simulator` gerencia listas de `Actors` e `Obstacles`.
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

```powershell
javac -d bin src/*.java
```

2. Execute a aplicação pelo método `main`

```bash
java Principal
```

```powershell
java -cp bin Principal
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

```text
.
├── .vscode/                     # Configurações do VS Code
├── bin/                         # Arquivos compilados (.class)
├── src/                         # Código-fonte Java
│   ├── Actor.java
│   ├── Animal.java
│   ├── Counter.java
│   ├── Field.java
│   ├── FieldStats.java
│   ├── Fox.java
│   ├── Hunter.java
│   ├── HunterPreys.java
│   ├── Location.java
│   ├── Obstacles.java
│   ├── Principal.java
│   ├── Simulator.java
│   ├── SimulatorView.java
│   ├── Stone.java
│   └── Tree.java
├── PropostaTrabalhoPratico.pdf  # Documento original do trabalho
└── README.md                    # Este arquivo
```

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
