from tkinter import *
from tkinter.ttk import *
import caller
 

class GUI:
    def __init__ (self):
        self.window = Tk()
        self.window.title("Welcome")
        self.window.geometry('400x350')

        self.call = caller.Caller ()
        
        lbl = Label(self.window, text="Calculadora",font=("Arial Bold", 15))
        lbl.place(x=140, y=40)

        lbl1 = Label(self.window, text="funcion ",font=("Arial Bold", 10))
        lbl1.place(x=70, y=100)

        self.combo = Combobox(self.window)
        self.combo['values']= ("Suma","Resta","Multiplicacion","División","Factorial","Fibonacci", "foo")
        self.combo.current(0) #set the selected ite,
        self.combo.place(x=180, y=100)

        lb2 = Label(self.window, text="parametros ",font=("Arial Bold", 10))
        lb2.place(x=70, y=150)


        self.parameters_input = Entry(self.window,width=20)
        self.parameters_input.place(x=180, y=150)


        lb3 = Label(self.window, text="respuesta ",font=("Arial Bold", 10))
        lb3.place(x=70, y=200)

        self.answer_text = StringVar ()
        answer = Label(self.window, textvariable=self.answer_text ,width=20)
        answer.place(x=180, y=200)

        btn = Button(self.window, text="Calcular", command=self.make_call)
        btn.place(x=140, y=250)
    
    # def foo (self):
    #     print ("foo")
    #     pass

    def split_paramters_input (self):
        return self.parameters_input.get ().split (' ')
        pass

    def make_call (self):
        if self.parameters_input.get () == "":
            print ("Empty")
            return
        
        parameters = self.split_paramters_input ()

        c = self.combo.current ()
        r = 0
        if c == 6:
            r = self.call.methods[c]()
        elif c > 3:
            r = self.call.methods[c](int (parameters[0]))
        else:
            r = self.call.methods[c](int(parameters[0]), int(parameters[1]))
        
        self.answer_text.set (str (r))

    def run (self):
        self.window.mainloop()
