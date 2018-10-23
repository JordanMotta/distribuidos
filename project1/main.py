import cluster

if __name__ == "__main__":
    c = cluster.cluster()

    if c.is_main ():
        c.run ()
    else:
        c.process_data ()