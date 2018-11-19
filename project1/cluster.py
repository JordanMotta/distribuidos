import logs_generator
from mpi4py import MPI
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

        # Here i simulate a lot of logs
        # lg = logs_generator.LogsGenerator ()
        # while True:
        #     for node_id in range (self.nodes):
        #         if self.is_idle (node_id):
        #             fake_logs = lg.gen (1000)
        #             self.send_data_to_node (node_id, fake_logs)

    
    def is_idle (self, p_node_id):
        return False

    #for nodes use.
    def run_second (self):      
        data = MPI.COMM_WORLD.recv (source=self.MAIN_NODE)
        print ("data: ", len (data))
        new_data = self.snode.process_data (data)
        # MPI.COMM_WORLD.send (new_data, self.MAIN_NODE)

    
