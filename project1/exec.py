import os

nodes = input ("¿Cuántos nodos quieres?: ")
x = input ("Promedio de ataques por x minutos (Especifique x): ")

command = "mpirun -np " + nodes + " python3 main.py"

os.system (command)