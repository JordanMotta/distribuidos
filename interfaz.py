from tkinter import *
from tkinter.ttk import *
 
window = Tk()
 
window.title("Welcome")
 
window.geometry('400x350')
 
lbl = Label(window, text="Calculadora",font=("Arial Bold", 15))
lbl.place(x=140, y=40)

lbl1 = Label(window, text="funcion ",font=("Arial Bold", 10))
lbl1.place(x=70, y=100)

combo = Combobox(window)

combo['values']= ("Suma","Resta","Multiplicacion","División","Factorial","Fibonacci")
 
combo.current(0) #set the selected item
 
combo.place(x=180, y=100)

lb2 = Label(window, text="parametros ",font=("Arial Bold", 10))
lb2.place(x=70, y=150)


txt = Entry(window,width=20)

txt.place(x=180, y=150)


lb3 = Label(window, text="respuesta ",font=("Arial Bold", 10))

lb3.place(x=70, y=200)

answer = Entry(window,width=20)

answer.place(x=180, y=200)

btn = Button(window, text="Calcular")
 
btn.place(x=140, y=250)


window.mainloop()
