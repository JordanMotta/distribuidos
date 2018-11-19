import time
import random

class LogsGenerator:

    def __init__ (self):
        self.Protocolo=['SOAP','POP3','IMAP']
        self.Tipo=['INFOR','WARM']

        self.User=['braggett9h@ucab.edu.ve','lcettellnw@ucab.edu.ve','bsurcomb6m@ucab.edu.ve','jcapellc4_21@ucab.edu.ve','nbeevisax@ucab.edu.ve'
            ,'tainscoughbu@ucab.edu.ve','misaksende_27@ucab.edu.ve','swreakesjq@ucab.edu.ve','crosonep1_27@ucab.edu.ve','tburhillmr_21@ucab.edu.ve'
            ,'mmarchettil8_28@ucab.edu.ve','kurenjp@ucab.edu.ve','gmcaleeseao@ucab.edu.ve','rstorckes_23@ucab.edu.ve']

        self.Https=['https://127.0.0.1:7071/service/admin/soap/AuthRequest] [name=zimbra;ip=127.0.0.1;ua=zmprov/8.6.0_GA_1229;] security - cmd=AdminAuth; account=zimbra;',
            'https://127.0.0.1:7071/service/admin/soap/AuthRequest] [name=zimbra;ip=127.0.0.1;ua=zmprov/8.6.0_GA_1229;] security - cmd=Auth; account=zimbra; protocol=soap;',
            'https://192.168.15.15:7071/service/admin/soap/] [name=cjordesonm8_20@ucab.edu.ve;ip=192.168.15.15;] security - cmd=Auth; account=cjordesonm8_20@ucab.edu.ve; protocol=soap;']

        self.via=['192.168.15.15(nginx/1.2.0-zimbra);ua=Zimbra/8.6.0_GA_1229;] security - cmd=Auth; account=',
            'com.android.email,192.168.15.15(nginx/1.2.0-zimbra);ua=Zimbra/8.6.0_GA_1229;] security - cmd=Auth; account=']

        self.error=['invalid password','must change password','locked']

    def gen (self, batch_number):
        i = 1
        j = 1
        h = 1
        logs = []
        for x in range(batch_number):
            Fecha= time.strftime("%x")
            Hora = time.strftime("%X")
            salida = ''
            resultado=''
            Error_Tipo= self.Tipo[random.randrange(2)]
            Error_Protocolo=self.Protocolo[random.randrange(3)]
            user_final=self.User[random.randrange(14)]
            self.error_final=self.error[random.randrange(3)]
            if Error_Tipo== 'INFOR':
                if Error_Protocolo== 'SOAP':
                    salida = Fecha+ ' '+  Hora + ','+ repr(random.randrange(100000))+' INFOR  [' + 'qtp509886383-' + repr(i)+' :'+ self.Https[random.randrange(3)] 
                    i=i+1
                elif  Error_Protocolo == 'POP3':
                    salida = Fecha+ ' '+  Hora + ','+ repr(random.randrange(100000))+' INFOR  [Pop3SSLServer- ' + repr(i) + ']' 
                    salida = salida + ' [ip=192.168.15.15;oip=209.85.' + repr (random.randrange(223)) + '.' + repr(random.randrange(200)) 
                    salida = salida + ';] security - cmd=Auth; account=' + self.User[random.randrange(14)]+ '; protocol=pop3;'
                    j=j+1
                else:
                    salida = Fecha+ ' '+  Hora + ','+ repr(random.randrange(100000))+'  INFOR  [ImapSSLServer- ' + repr(i) + ']'
                    salida = salida + '[ip=192.168.15.15;oip='+repr(random.randrange(223))+'.'+repr(random.randrange(223)) + '.'
                    salida = salida + repr (random.randrange(223))+'.'+repr (random.randrange(223))+';self.via='+self.via[random.randrange(2)]
                    salida = salida + self.User[random.randrange(14)]+'; protocol=imap;'
                    h=h+1
            else:
                if Error_Protocolo =='POP3':
                    if self.error_final=='invalid password':
                        resultado='], invalid password;'
                    elif self.error_final=='locked':
                        resultado== '], account(or domain) status is locked;'

                    salida= Fecha + ' ' + Hora + ',' + repr(random.randrange(100000)) + ' WARN  [Pop3SSLServer- '+repr(j) 
                    salida= salida + ']' +'[ip=192.168.15.15;oip='+repr(random.randrange(223))+'.'+repr(random.randrange(223))+'.'+ repr (random.randrange(223))
                    salida= salida + '.'+repr (random.randrange(223))+ ';] security - cmd=Auth; account= ' +  user_final
                    salida= salida + '; protocol=pop3; self.error=authentication failed for ['+ user_final + resultado
                elif Error_Protocolo == 'SOAP':
                    salida = Fecha + ' ' + Hora + ',' + repr(random.randrange(100000)) + ' WARN  [qtp509886383 -'+repr(j)
                    salida = salida + ':http://127.0.0.1:8080/service/soap/AuthRequest] [name=' + user_final
                    salida = salida + ';oip=192.168.15.15;ua=zclient/8.6.0_GA_1229;] security - cmd=Auth; account='+user_final
                    if self.error_final=='invalid password' :
                        resultado='; protocol=soap; self.error=authentication failed for [atorrecillalw], invalid password;'
                    elif self.error_final=='must change password' :
                        resultado== '; protocol=soap; self.error=must change password;'
                    salida = salida+resultado
                else:
                    salida=Fecha + ' ' + Hora + ',' + repr(random.randrange(100000)) + ' WARN  [ImapSSLServer- '+repr(j) + ']' 
                    salida= salida + '[ip=192.168.15.15;oip='+repr(random.randrange(223))+'.'+repr(random.randrange(223)) + '.'
                    salida=salida + repr (random.randrange(223))+'.'+repr (random.randrange(223)) + ';self.via=192.168.15.15(nginx/1.2.0-zimbra);ua=Zimbra/8.6.0_GA_1229;] security - cmd=Auth; account='
                    salida=salida + user_final + '; protocol=imap; self.error=authentication failed for ['
                    if self.error_final == 'invalid password':
                        resultado= '], invalid password;'
                    elif self.error_final == 'locked':
                        resultado= '], account(or domain) status is locked;' 
                    salida= salida+user_final+resultado
                    logs.append (salida)
        return logs