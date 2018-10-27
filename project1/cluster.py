import logs_generator
# main interface beetwen main computer and nodes.

#Data structure to be returned by nodes
# {
    # countries : [(country name, number)],
    # cities : [(city name, number)],
    # ips : [(ip, number)],
    # hours : [(hour, number)],
    # emails : [(email, number)]
# }

# NOTE: The data must be sorted in main computer.
class cluster:
    def __init__ (self):
        self.id = None

    def is_main (self):
        return False
    
    def is_node (self):
        return False

    # After main computer collect some data. It will tranfer it to some node.
    def send_data_to_node (self, p_node_id, p_data):
        pass
    
    #run method collect the data to be send to nodes.
    def run_main (self):
        filename = "logs.txt"
        f = open (filename, 'r+')
        logs = f.readlines ()
        f.close ()

        nodes = 8
        batch = logs.count () / nodes

        for n in range (8):

            pass

        # Here i simulate a lot of logs
        lg = logs_generator.LogsGenerator ()
        while True:
            for node_id in range (nodes):
                if self.is_idle (node_id):
                    fake_logs = lg.gen ()
                    self.send_data_to_node (node_id, fake_logs)
    
    def is_idle (self, p_node_id):
        return False

    #for nodes use.
    def run_second (self):
        pass
    
