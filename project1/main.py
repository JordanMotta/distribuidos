#!/usr/env/bin python3
import cluster

if __name__ == "__main__":
    c = cluster.cluster()

    if c.is_main ():
        c.run_main ()
    else:
        c.run_second ()