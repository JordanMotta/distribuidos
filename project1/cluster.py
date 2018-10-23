
# main interface beetwen main computer and nodes.
class cluster:
    def __init__ (self):
        
        pass
    def is_main (self):
        return False
    
    def is_node (self):
        return False

    # After main computer collect some data. It will tranfer it to some node.
    def send_data_to_node (self, p_node_id, p_data):
        pass
    
