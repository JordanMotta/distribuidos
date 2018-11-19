import re
import mpi4py


class Node:
    def __init__ (self, p_id):
        self.id = p_id
    
    def process_data(self, p_logs):
        print ('logs len: ' + str(len (p_logs)))
        processed_data = {
            'countries' : {},
            'cities' : {},
            'ips' : {},
            'emails' : {}
        }

        # for l in logs:
        #     pl = self.process_log (l)

    def process_log (self, p_log):
        data = {
            'country' : {},
            'city' : {},
            'ip' : {},
            'email' : {}
        }
        return data