import logs_generator
from mpi4py import MPI
import operator
import node

# main interface beetwen main computer and nodes.

#Data structure to be returned by nodes
# {

    # countries : {country name, number},
    # cities : {city name, number},
    # ips : {ip, number},
    # hours : {hour, number},
    # emails : {email, number}

# }

# NOTE: The data must be sorted in main computer.
class cluster:
    MAIN_NODE = 0
    def __init__ (self):
        self.id = MPI.COMM_WORLD.rank
        if self.id == self.MAIN_NODE: # cluster
            self.size = MPI.COMM_WORLD.size
        else:
            self.snode = node.Node (self.id)

    def is_main (self):
        return self.id == self.MAIN_NODE

    
    def is_node (self):
        return False

    # After main computer collect some data. It will tranfer it to some node.
    def send_data_to_node (self, p_node_id, p_data):
        print ("sending logs len: ", len (p_data))
        MPI.COMM_WORLD.send (p_data, p_node_id)
    
    #run method collect the data to be send to nodes.
    def run_main (self):
        filenames = ['log2', 'log3', 'log4']
        f = None
        logs = []
        for fn in filenames:
            f = open ('samples/' + fn, 'r')
            logs += f.readlines ()
            f.close ()

        print ("cluster logs len: ", len (logs))
        self.nodes = self.size
        # remain = len (logs) % nodes - 1
        # batchs = []
        batch = int (len (logs) / (self.nodes - 1))
        index = 0
        for n in range (1, self.nodes):
            print ("node: ", n)
            self.send_data_to_node (n, logs[index : index + batch - 1])
            index += batch


        data = {
            'countries' : {},
            'cities' : {},
            'ips' : {},
            'emails' : {},
            'hours' : {}
        }

        for n in range (1, self.nodes):
            dp = MPI.COMM_WORLD.recv (source=n)
            print ("received from : ", n)
            self.add (data['countries'], dp['countries'])
            self.add (data['cities'], dp['cities'])
            self.add (data['ips'], dp['ips'])
            self.add (data['emails'], dp['emails'])
            self.add (data['hours'], dp['hours'])
            print ('added')
        
        # print (data)
        # print ('-----------------------------')
        ordered_countries = sorted (data['countries'].items (), key=operator.itemgetter (1), reverse=True)
        ordered_cities = sorted (data['cities'].items (), key=operator.itemgetter (1), reverse=True)
        ordered_ips = sorted (data['ips'].items (), key=operator.itemgetter (1), reverse=True)
        ordered_emails = sorted (data['emails'].items (), key=operator.itemgetter (1), reverse=True)
        ordered_hours = sorted (data['hours'].items (), key=operator.itemgetter (1), reverse=True)
        print (ordered_countries)
        print ('------------------------------')
        print (ordered_cities)
        print ('------------------------------')
        print (ordered_ips)
        print ('------------------------------')
        print (ordered_emails)
        print ('------------------------------')
        print (ordered_hours)

    
    def is_idle (self, p_node_id):
        return False

    def add (self, p_acc, p_data_rocessed):
        dp = p_data_rocessed
        for k, v in dp.items ():
            try:
                p_acc[k] += v
            except KeyError as e:
                p_acc[k] = v

    #for nodes use.
    def run_second (self):      
        data = MPI.COMM_WORLD.recv (source=self.MAIN_NODE)
        print ("data: ", len (data))
        new_data = self.snode.process_data (data)
        MPI.COMM_WORLD.send (new_data, self.MAIN_NODE)
    
    def sort_data (self, p_data):
        for d in p_data:
            d.sort ()