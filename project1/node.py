#!/usr/env/bin python3
import re
import mpi4py
import geoip2.database
import time


class Node:
    def __init__ (self, p_id):
        self.id = p_id
        self.geo = geoip2.database.Reader ('GeoLite2-City.mmdb')
    
    def process_data(self, p_logs):
        start_time = time.time ()
        # print ('logs len: ' + str(len (p_logs)))
        processed_data = {
            'countries' : {},
            'cities' : {},
            'ips' : {},
            'emails' : {},
            'hours' : {}
        }

        for l in p_logs:
            if l.find ('WARN') != -1:
                pl = self.process_log (l)
                self.add_item (processed_data['countries'], pl, 'country')
                self.add_item (processed_data['cities'], pl, 'city')
                self.add_item (processed_data['ips'], pl, 'ip')
                self.add_item (processed_data['emails'], pl, 'email')
                self.add_item (processed_data['hours'], pl, 'hour')
        elapsed_time = time.time () - start_time
        print ("Node ", self.id, " lasted ", elapsed_time, " seconds")
        return processed_data

    def add_item (self, p_dict, p_value, p_key):
        if p_value[p_key] in p_dict:
            p_dict[p_value[p_key]] += 1
        else:
            p_dict[p_value[p_key]] = 1

    def process_log (self, p_log):
        data = {}

        ips = re.search ('oip=(\d+.\d+.\d+.\d+);', p_log)
        emails = re.search ('account=(\w+@[a-zA-Z\.]+);', p_log)
        times = re.search ('^\d+\-\d+\-\d+\s(\d+\:\d+\:\d+),', p_log)
        if not ips:
            data['country'] = 'None'
            data['city'] = 'None'
            data['ip'] = 'None'
        else:
            # print (ips.group (1))
            try:
                r = self.geo.city (ips.group (1))
            except geoip2.errors.AddressNotFoundError as e:
                data['country'] = 'None'
                data['city'] = 'None'
                data['ip'] = 'None'
                pass
            else:
                data['country'] = r.country.name
                data['city'] = r.city.name if r.city.name != None else 'None'
                data['ip'] = ips.group (1)

        # print (emails.groups ())
        if not emails:
            data['email'] = 'None'
        else:
            data['email'] = emails.group (1)
        
        time = times.group (1)
        hour = time.split (':')[0]

        data['hour'] = hour
        # print (data)

        return data