# Projeto prático - Raposas e Coelhos
Capítulo 10 do livro Programação Orientada a Objetos com Java – Uma Introdução Prática Usando o BlueJ, 4ºedição, dos autores Barnes e Kolling.

## Diagrama de classes do código inicial do projeto
<p align="center">
    <img width="1032" height="582" alt="image" src="https://github.com/user-attachments/assets/a8461d31-3845-4f9c-a005-bf64e0950e40" />
</p>

## Diagrama de classes para implementações futuras no projeto
<p align="center">
    <img width="871" height="805" alt="image" src="https://github.com/user-attachments/assets/d5f1797b-7fb6-4850-bd94-4edcfdf4866c" />
</p>

### Resumo das Alterações adicionadas:

Classe `Tree`: Implementada como um Actor estático. Ela cresce frutos (simulando a estação/tempo) e permite que sejam pegos. Ela se "recoloca" no updatedField automaticamente em cada turno para não desaparecer.

Classe `Hunter`: Implementada como um Animal (para mover e ter idade). Possui lógica de energia e reprodução baseada em abates (killCount). Se alimenta de frutos se a energia estiver baixa (< 30%).

